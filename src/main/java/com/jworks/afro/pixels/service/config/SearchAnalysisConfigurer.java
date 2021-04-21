package com.jworks.afro.pixels.service.config;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

public class SearchAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        context.analyzer( "english" ).custom()
                .tokenizer( "standard" )
                .charFilters( "html_strip" )
                .tokenFilters( "lowercase", "snowball_english", "asciifolding" );

        context.tokenFilter( "snowball_english" )
                .type( "snowball" )
                .param( "language", "English" );

        context.normalizer( "lowercase" ).custom()
                .tokenFilters( "lowercase", "asciifolding" );

        context.analyzer( "french" ).custom()
                .tokenizer( "standard" )
                .tokenFilters( "lowercase", "snowball_french", "asciifolding" );

        context.tokenFilter( "snowball_french" )
                .type( "snowball" )
                .param( "language", "French" );
    }
}