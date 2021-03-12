package com.rs.lottogradle.common.rest;

import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class CustomRestTemplateCustomizer implements RestTemplateCustomizer {
    private OkHttpClient okHttpClient;

    public CustomRestTemplateCustomizer(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new OkHttp3ClientHttpRequestFactory(okHttpClient)));
        restTemplate.getInterceptors().add(new CustomClientHttpRequestInterceptor());
    }
}
