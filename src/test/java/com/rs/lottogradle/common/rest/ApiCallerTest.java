package com.rs.lottogradle.common.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApiCallerTest {
    @Autowired
    private ApiCaller apiCaller;

    @Test
    void getInfo() {
        System.out.println(apiCaller.getInfo(580));
    }
}