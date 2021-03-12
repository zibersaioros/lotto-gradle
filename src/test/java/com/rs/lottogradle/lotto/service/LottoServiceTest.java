package com.rs.lottogradle.lotto.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LottoServiceTest {

    @Autowired
    LottoService lottoService;

    @Test
    void testSetData() {
        lottoService.setupData();
    }
}