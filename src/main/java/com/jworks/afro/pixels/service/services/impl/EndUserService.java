package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.EndUser;
import com.jworks.afro.pixels.service.entities.EndUserFormOfIdentification;
import com.jworks.afro.pixels.service.entities.FormOfIdentification;
import com.jworks.afro.pixels.service.exceptions.BadRequestException;
import com.jworks.afro.pixels.service.exceptions.NotFoundRestApiException;
import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.models.EndUserDto;
import com.jworks.afro.pixels.service.models.PasswordResetDto;
import com.jworks.afro.pixels.service.repositories.EndUserRepository;
import com.jworks.afro.pixels.service.utils.EndUserReferenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */
@Slf4j
@Service
public class EndUserService extends ServiceBluePrintImpl<EndUser,EndUserDto> {


    private final EndUserRepository endUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final EndUserReferenceGenerator endUserReferenceGenerator;
    
    private final FormOfIdentificationService formOfIdentificationService;
    private final EndUserFormOfIdentificationService endUserFormOfIdentificationService;
    private final EndUserVerificationLevelService endUserVerificationLevelService;

    @Autowired
    public EndUserService(EndUserRepository endUserRepository, PasswordEncoder passwordEncoder,
                          EndUserReferenceGenerator endUserReferenceGenerator, FormOfIdentificationService formOfIdentificationService, EndUserFormOfIdentificationService endUserFormOfIdentificationService,
                          EndUserVerificationLevelService endUserVerificationLevelService) {
        super(endUserRepository);
        this.endUserRepository = endUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.endUserReferenceGenerator = endUserReferenceGenerator;
        this.formOfIdentificationService = formOfIdentificationService;
        this.endUserFormOfIdentificationService = endUserFormOfIdentificationService;
        this.endUserVerificationLevelService = endUserVerificationLevelService;
    }
    
    @Transactional
    public EndUser performSignUpProcess(EndUserDto endUserDto) throws NotFoundRestApiException, SystemServiceException {

        EndUser endUser = createUser(endUserDto);

        Optional<FormOfIdentification> optionalFormOfIdentification = verifyFormOfIdentificationDocumentUpload(endUserDto);

        if(optionalFormOfIdentification.isPresent()){
            saveEndUserFormOfIdentification(endUser,endUserDto.getFormOfIdentificationDocumentUrl(),optionalFormOfIdentification.get());
        }

        saveVerificationLevelAsNewlyOnBoardedEndUser(endUser);

        return endUser;
    }

    public void updateEndUserDetails(EndUserDto endUserDto, String endUserReference) throws SystemServiceException, NotFoundRestApiException {

        EndUser endUser = getUserByUserReference(endUserReference);

        endUser.setFirstName(endUserDto.getFirstName());
        endUser.setLastName(endUserDto.getLastName());
        endUser.setEmailAddress(endUserDto.getEmailAddress());
        endUser.setPhoneNumber(endUserDto.getPhoneNumber());
        endUser.setUsername(endUserDto.getUsername());
        update(endUser);
    }

    public void resetUserPassword(PasswordResetDto passwordResetDto ,String endUserReference) throws BadRequestException, NotFoundRestApiException {

        String confirmPassword = passwordResetDto.getConfirmPassword();
        String password = passwordResetDto.getNewPassword();

        if(!password.equals(confirmPassword)){
            throw new BadRequestException("Passwords do not match");
        }

        EndUser endUser = getUserByUserReference(endUserReference);

        String encodePassword = passwordEncoder.encode(password);

        endUser.setPassword(encodePassword);

    }


    public EndUser getUserByUserReference(String endUserReference) throws NotFoundRestApiException {

        return endUserRepository.findByUserReference(endUserReference)
                .orElseThrow(() -> new NotFoundRestApiException(String.format("User with reference %s not found.", endUserReference)));
    }

    private void saveVerificationLevelAsNewlyOnBoardedEndUser(EndUser endUser) throws SystemServiceException {

        endUserVerificationLevelService.saveVerificationLevelAsNewlyOnBoardedEndUser(endUser);

    }


    private EndUser createUser(EndUserDto endUserDto) throws SystemServiceException {
        String encodedPassword = passwordEncoder.encode(endUserDto.getPassword());

        String userReference = endUserReferenceGenerator.generateReference();

        EndUser endUser = EndUser.builder()
                .firstName(endUserDto.getFirstName())
                .lastName(endUserDto.getLastName())
                .emailAddress(endUserDto.getEmailAddress())
                .password(encodedPassword)
                .isActive(false)
                .username(endUserDto.getUsername())
                .phoneNumber(endUserDto.getPhoneNumber())
                .userReference(userReference)
                .build();

        return endUserRepository.save(endUser);
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

        if(!StringUtils.isBlank(formOfIdentificationType) && !StringUtils.isBlank(formOfIdentificationDocumentUrl)){
            
            return Optional.of(formOfIdentificationService.verifyFormOfIdentificationType(formOfIdentificationType));
        }
        return Optional.empty();
    }


}
