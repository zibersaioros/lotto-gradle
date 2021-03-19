package com.rs.lottogradle.lotto.service;

import com.rs.lottogradle.lotto.service.analysis.*;
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
        ExclusionAnalysis exclusionAnalysis = new ExclusionAnalysis(roundService, 20);
        List<Integer> nums = exclusionAnalysis.getNumbers(RoundService.getLastRound() -25);
        System.out.println(nums);
        System.out.println(nums.size());
    }

    @Test
    void analyzeFrequency() {
        FrequencyAnalysis frequencyAnalysis = new FrequencyAnalysis(roundService, 20);
        List<Integer> nums = frequencyAnalysis.getNumbers(RoundService.getLastRound());
//        System.out.println(nums);
//        System.out.println(nums.size());
    }


    @Test
    void verifyExclusionNumbers() {
        // 무난
        Analysis exclusionAnalysis = new ExclusionAnalysis(roundService, 5);
        String result = lottoService.verify(52, exclusionAnalysis);
        System.out.println(result);
    }

    @Test
    void verifyFrequencyNumbers() {
        // 아직
        Analysis frequencyAnalysis = new FrequencyAnalysis(roundService, 12);
        String result = lottoService.verify(52, frequencyAnalysis);
        System.out.println(result);
    }

    @Test
    void verifyExclusionInvertNumbers() {
        // 아직
        Analysis exclusionAnalysis = new ExclusionInvertAnalysis(roundService, 104);
        String result = lottoService.verify(52, exclusionAnalysis);
        System.out.println(result);
    }

    @Test
    void verifyFrequencyInvertNumbers() {
        // 아직
        Analysis frequencyAnalysis = new FrequencyInvertAnalysis(roundService, 12);
        String result = lottoService.verify(52, frequencyAnalysis);
        System.out.println(result);
    }

    @Test
    void verifyRetainNumbers() {
        // 아직
        RetainAnalysis retainAnalysis = new RetainAnalysis();
        retainAnalysis.setAnalysisList(
                new ExclusionAnalysis(roundService, 8)
                , new FrequencyAnalysis(roundService, 52));
        String result = lottoService.verify(52, retainAnalysis);
        System.out.println(result);
    }
}