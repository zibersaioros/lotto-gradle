package com.rs.lottogradle.common.rest;

import com.rs.lottogradle.lotto.model.Round;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ApiCaller {
    @Value("${api.url}")
    private String baseUrl;


    private RestTemplate restTemplate;

    @Autowired
    public ApiCaller(RestTemplateBuilder builder, OkHttpClient okHttpClient) {
        this.restTemplate = builder.customizers(new CustomRestTemplateCustomizer(okHttpClient)).build();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        this.restTemplate.setMessageConverters(messageConverters);
    }

    public Round getInfo(int cnt){
        return restTemplate.getForObject(baseUrl + cnt, Round.class);
    }
}
