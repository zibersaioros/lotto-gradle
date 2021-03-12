package com.rs.lottogradle.common.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private static Logger LOGGER = LoggerFactory.getLogger(CustomClientHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response, false);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) throws UnsupportedEncodingException {

        LOGGER.info("===========================request start===========================");
        LOGGER.info("URI         : {}", request.getURI());
        LOGGER.info("Method      : {}", request.getMethod());
        LOGGER.info("Headers     : {}", request.getHeaders());
        LOGGER.info("Request body: {}", new String(body, "UTF-8"));
        LOGGER.info("===========================request end===========================");
    }

    private void logResponse(ClientHttpResponse response, boolean onError) throws IOException {
        LOGGER.info("===========================response start===========================");
        LOGGER.info("Status code  : {}", response.getStatusCode());
        LOGGER.info("Status text  : {}", response.getStatusText());
        LOGGER.info("Headers      : {}", response.getHeaders());
        if(onError && response.getStatusCode().isError()
                || !onError)
            logResponseBody(response);
        LOGGER.info("===========================response end===========================");
    }

    private void logResponseBody(ClientHttpResponse response) throws IOException {
        if(!response.getHeaders().getContentType().equals(MediaType.APPLICATION_OCTET_STREAM)) {
            LOGGER.info("Response body: {}", StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
        }
    }

}
