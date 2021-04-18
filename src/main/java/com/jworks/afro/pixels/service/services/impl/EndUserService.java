package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.EndUser;
import com.jworks.afro.pixels.service.entities.EndUserFormOfIdentification;
import com.jworks.afro.pixels.service.entities.EndUserRole;
import com.jworks.afro.pixels.service.entities.FormOfIdentification;
import com.jworks.afro.pixels.service.enums.Role;
import com.jworks.afro.pixels.service.exceptions.*;
import com.jworks.afro.pixels.service.models.*;
import com.jworks.afro.pixels.service.repositories.EndUserRepository;
import com.jworks.afro.pixels.service.utils.EndUserReferenceGenerator;
import com.jworks.afro.pixels.service.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */
@Slf4j
@Service
public class EndUserService extends ServiceBluePrintImpl<EndUser,EndUserDto> implements UserDetailsService {


    private final EndUserRepository endUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final EndUserReferenceGenerator endUserReferenceGenerator;
    
    private final FormOfIdentificationService formOfIdentificationService;
    private final EndUserFormOfIdentificationService endUserFormOfIdentificationService;
    private final EndUserVerificationLevelService endUserVerificationLevelService;
    private final EndUserRoleService endUserRoleService;

    @Autowired
    public EndUserService(EndUserRepository endUserRepository, PasswordEncoder passwordEncoder,
                          EndUserReferenceGenerator endUserReferenceGenerator, FormOfIdentificationService formOfIdentificationService, EndUserFormOfIdentificationService endUserFormOfIdentificationService,
                          EndUserVerificationLevelService endUserVerificationLevelService, EndUserRoleService endUserRoleService) {
        super(endUserRepository);
        this.endUserRepository = endUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.endUserReferenceGenerator = endUserReferenceGenerator;
        this.formOfIdentificationService = formOfIdentificationService;
        this.endUserFormOfIdentificationService = endUserFormOfIdentificationService;
        this.endUserVerificationLevelService = endUserVerificationLevelService;
        this.endUserRoleService = endUserRoleService;
    }

    @Transactional(rollbackFor=Exception.class)
    public EndUserDto performSignUpProcess(EndUserDto endUserDto) throws NotFoundRestApiException, SystemServiceException {

        EndUser endUser = createUser(endUserDto);

        Optional<FormOfIdentification> optionalFormOfIdentification = verifyFormOfIdentificationDocumentUpload(endUserDto);

        if(optionalFormOfIdentification.isPresent()){
            saveEndUserFormOfIdentification(endUser,endUserDto.getFormOfIdentificationDocumentUrl(),optionalFormOfIdentification.get());
        }

        saveVerificationLevelAsNewlyOnBoardedEndUser(endUser);

        saveRolesForEndUser(endUser);

        sendVerificationEmailToUser(endUser);

        return convertEntityToDto(endUser);
    }

    @Override
    public EndUserDto convertEntityToDto(EndUser entity) {
        return EndUserDto.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .emailAddress(entity.getEmailAddress())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }

    private void saveRolesForEndUser(EndUser endUser) throws SystemServiceException {

        EndUserRole endUserRole = EndUserRole.builder()
                .endUser(endUser)
                .role(Role.USER)
                .build();

        endUserRoleService.save(endUserRole);

    }

    public void updateEndUserDetails(UpdateEndUserDto endUserDto, String endUserReference, Long userId) throws SystemServiceException, NotFoundRestApiException {

        EndUser endUserToUpdate = getUserById(userId);

        EndUser endUserLoggedIn = getUserByUsername(endUserReference);

        verifyThatTheOwnerOfUserAccountIsMakingTheUpdate(endUserToUpdate,endUserLoggedIn);

        Long persistedUserId = endUserToUpdate.getId();


        String emailAddress = endUserDto.getEmailAddress();

        String phoneNumber = endUserDto.getPhoneNumber();


        ensureEmailDoesNotExist(emailAddress,persistedUserId);

        ensurePhoneNumberDoesNotExist(phoneNumber,persistedUserId);

        endUserToUpdate.setFirstName(endUserDto.getFirstName());
        endUserToUpdate.setLastName(endUserDto.getLastName());
        endUserToUpdate.setEmailAddress(endUserDto.getEmailAddress());
        endUserToUpdate.setPhoneNumber(endUserDto.getPhoneNumber());
        update(endUserToUpdate);
    }

    private void verifyThatTheOwnerOfUserAccountIsMakingTheUpdate(EndUser endUserToUpdate, EndUser endUserLoggedIn) throws ForbiddenUserUpdateException, NotFoundRestApiException {

        Role roleOfEndUserToUpdate = endUserRoleService.getEndUserRole(endUserLoggedIn).getRole();

        if(Role.USER.equals(roleOfEndUserToUpdate) && !endUserLoggedIn.equals(endUserToUpdate)){

            throw new ForbiddenUserUpdateException(endUserLoggedIn.getUsername(),endUserToUpdate.getUsername());
        }
    }

    public void resetUserPassword(PasswordResetDto passwordResetDto ,String username, Long userId) throws SystemServiceException, NotFoundRestApiException {

        EndUser endUserToUpdate = getUserById(userId);

        EndUser endUserLoggedIn = getUserByUsername(username);

        if(!endUserLoggedIn.equals(endUserToUpdate)){

            throw new ForbiddenUserUpdateException(endUserLoggedIn.getUsername(),endUserToUpdate.getUsername());
        }

        String confirmPassword = passwordResetDto.getConfirmPassword();
        String password = passwordResetDto.getNewPassword();

        if(!password.equals(confirmPassword)){
            throw new BadRequestException("Passwords do not match");
        }


        String encodePassword = passwordEncoder.encode(password);

        endUserToUpdate.setPassword(encodePassword);

        update(endUserToUpdate);

    }


    public EndUser getUserByUsername(String endUserUsername) throws NotFoundRestApiException {

        return endUserRepository.findByUsername(endUserUsername)
                .orElseThrow(() -> new NotFoundRestApiException(String.format("User with username %s not found.", endUserUsername)));
    }

    public EndUser getUserById(Long userId) throws NotFoundRestApiException {

        return endUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundRestApiException(String.format("User with Id %s not found.", userId)));
    }

    public EndUserDto getUserDtoByUsername(String endUserUsername) throws NotFoundRestApiException {

       return convertEntityToDto(getUserByUsername(endUserUsername));
    }

    private EndUser createUser(EndUserDto endUserDto) throws SystemServiceException {

        String username = endUserDto.getUsername();

        String emailAddress = endUserDto.getEmailAddress();

        String phoneNumber = endUserDto.getPhoneNumber();

        ensureUsernameDoesNotExist(username);

        ensureEmailDoesNotExist(emailAddress);

        ensurePhoneNumberDoesNotExist(phoneNumber);

        String encodedPassword = passwordEncoder.encode(endUserDto.getPassword());

        String userReference = endUserReferenceGenerator.generateReference();



        EndUser endUser = EndUser.builder()
                .firstName(endUserDto.getFirstName())
                .lastName(endUserDto.getLastName())
                .emailAddress(emailAddress)
                .password(encodedPassword)
                .isActive(false)
                .username(username)
                .phoneNumber(phoneNumber)
                .userReference(userReference)
                .build();

        return endUserRepository.save(endUser);
    }

    private void ensureUsernameDoesNotExist(String username) throws DuplicateEntryException {

        if(endUserRepository.findIdByUsername(username).isPresent()) {
            throw new DuplicateEntryException(String.format("Username: %s is already taken.", username));
        }
    }

    private void ensureUsernameDoesNotExist(String username, Long userId) throws DuplicateEntryException {

        if(endUserRepository.findIdByUsernameAndIdNot(username, userId).isPresent()) {
            throw new DuplicateEntryException(String.format("Username: %s is already taken.", username));
        }
    }

    private void ensureEmailDoesNotExist(String email) throws DuplicateEntryException {

        if(endUserRepository.findIdByEmailAddress(email).isPresent()) {
            throw new DuplicateEntryException(String.format("Email address: %s is already taken.", email));
        }
    }

    private void ensureEmailDoesNotExist(String email, Long userId) throws DuplicateEntryException {

        if(endUserRepository.findIdByEmailAddressAndIdNot(email, userId).isPresent()) {
            throw new DuplicateEntryException(String.format("Email address: %s is already taken.", email));
        }
    }

    private void ensurePhoneNumberDoesNotExist(String phoneNumber) throws DuplicateEntryException {

        if(!StringUtils.isEmpty(phoneNumber) && endUserRepository.findIdByPhoneNumber(phoneNumber).isPresent()) {
            throw new DuplicateEntryException(String.format("Phone Number: %s is already taken.", phoneNumber));
        }
    }

    private void ensurePhoneNumberDoesNotExist(String phoneNumber, Long id) throws DuplicateEntryException {

        if(!StringUtils.isEmpty(phoneNumber) && endUserRepository.findIdByPhoneNumberAndIdNot(phoneNumber, id).isPresent()) {
            throw new DuplicateEntryException(String.format("Phone Number: %s is already taken.", phoneNumber));
        }
    }

    private void saveVerificationLevelAsNewlyOnBoardedEndUser(EndUser endUser) throws SystemServiceException {

        endUserVerificationLevelService.saveVerificationLevelAsNewlyOnBoardedEndUser(endUser);

    }
    private void saveEndUserFormOfIdentification(EndUser endUser, String documentUrl, FormOfIdentification formOfIdentification) throws SystemServiceException {
        EndUserFormOfIdentification endUserFormOfIdentification = EndUserFormOfIdentification.builder()
                .endUser(endUser)
                .documentImageUrl(documentUrl)
                .formOfIdentification(formOfIdentification)
                .isApproved(false)
                .build();
        endUserFormOfIdentificationService.save(endUserFormOfIdentification);
    }
    
    private Optional<FormOfIdentification> verifyFormOfIdentificationDocumentUpload(EndUserDto endUserDto) throws NotFoundRestApiException {

        String formOfIdentificationType = endUserDto.getFormOfIdentificationType();
        String formOfIdentificationDocumentUrl = endUserDto.getFormOfIdentificationDocumentUrl();
        /*
        * We only want to verify the formOfIdentificationType if a formOfIdentificationDocumentUrl is provided in the request.
        * */
        if(!StringUtils.isBlank(formOfIdentificationType) && !StringUtils.isBlank(formOfIdentificationDocumentUrl)){
            
            return Optional.of(formOfIdentificationService.verifyFormOfIdentificationType(formOfIdentificationType));
        }
        return Optional.empty();
    }

    private void sendVerificationEmailToUser(EndUser endUser) {
        //todo work on emailService to send email requests
    }

    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
        try{
            EndUser endUser = getUserByUsername(user);

            EndUserRole endUserRole = endUserRoleService.getEndUserRole(endUser);

            List<SimpleGrantedAuthority> roles= Collections.singletonList(new SimpleGrantedAuthority(endUserRole.getRole().name()));

            return new User(endUser.getUsername(),endUser.getPassword(),roles);

        }catch (NotFoundRestApiException ex){

            log.error("Exception occurred:",ex);

            throw new UsernameNotFoundException(String.format("User with username: %s not found.", user));
        }
    }
}
