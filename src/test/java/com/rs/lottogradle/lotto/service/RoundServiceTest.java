package com.rs.lottogradle.lotto.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoundServiceTest {

    @Autowired
    RoundService roundService;

    @Test
    void testSetData() {
        roundService.setupData();
    }
}