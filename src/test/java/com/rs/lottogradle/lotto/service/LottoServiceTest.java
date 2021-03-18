package com.rs.lottogradle.lotto.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class LottoServiceTest {
    @Autowired
    LottoService lottoService;

    @Autowired
    RoundService roundService;

    @Test
    void analyzeExclusion() {
        List<Integer> nums = lottoService.getExclusionNumbers(roundService.getLastRound() -25);
        System.out.println(nums);
        System.out.println(nums.size());
    }


    @Test
    void verifyExclusionNumbers() {
        lottoService.verifyExclusionNumbers(10);
    }
}