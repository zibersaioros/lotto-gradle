package com.rs.lottogradle.lotto.service;

import com.rs.lottogradle.lotto.model.AnalysisSummary;
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
        //테스트 변수영역
        int maxAnalysisCount = 105;
        int testCount = 12;
        int targetWin = 4;

        int bestCount = 5;
        double bestAnalysis = LottoService.getStandardDenominator(targetWin);
        AnalysisSummary bestResult = null;
        for(int i = bestCount; i < maxAnalysisCount; i++){
            Analysis exclusionAnalysis = new ExclusionAnalysis(roundService, i);
            AnalysisSummary result = lottoService.verify(testCount, exclusionAnalysis);

            double analysis5 = result.getDenominatorMap().getOrDefault(targetWin, bestAnalysis + 1);
            if(Math.min(analysis5, bestAnalysis) == analysis5){
                bestCount = i;
                bestAnalysis = analysis5;
                bestResult = result;
            }
        }
        if(bestResult != null){
            System.out.println(bestResult);
            System.out.printf("bestCount : %d\n", bestCount);
        }
        //11
    }

    @Test
    void verifyFrequencyNumbers() {
        //테스트 변수영역
        int maxAnalysisCount = 104;
        int testCount = 12;
        int targetWin = 4;


        int bestCount = 5;
        double bestAnalysis = LottoService.getStandardDenominator(targetWin);
        AnalysisSummary bestResult = null;
        for(int i = bestCount; i < maxAnalysisCount; i++){
            Analysis frequencyAnalysis = new FrequencyAnalysis(roundService, i);
            AnalysisSummary result = lottoService.verify(testCount, frequencyAnalysis);

            double analysis5 = result.getDenominatorMap().getOrDefault(targetWin, bestAnalysis + 1);
            if(Math.min(analysis5, bestAnalysis) == analysis5){
                bestCount = i;
                bestAnalysis = analysis5;
                bestResult = result;
            }
        }
        if(bestResult != null){
            System.out.println(bestResult);
            System.out.printf("bestCount : %d\n", bestCount);
        }
        //34
    }

    @Test
    void verifyExclusionInvertNumbers() {
        int bestCount = 5;
        double bestAnalysis = LottoService.getStandardDenominator(5);
        AnalysisSummary bestResult = null;
        for(int i = bestCount; i < 105; i++){
            Analysis exclusionInvertAnalysis = new ExclusionInvertAnalysis(roundService, i);
            AnalysisSummary result = lottoService.verify(52, exclusionInvertAnalysis);

            double analysis5 = result.getDenominatorMap().getOrDefault(5, bestAnalysis + 1);
            if(Math.min(analysis5, bestAnalysis) == analysis5){
                bestCount = i;
                bestAnalysis = analysis5;
                bestResult = result;
            }
        }
        if(bestResult != null){
            System.out.println(bestResult);
            System.out.printf("bestCount : %d\n", bestCount);
        } else {
            System.out.println("best is null");
        }
        //null
    }

    @Test
    void verifyFrequencyInvertNumbers() {
        int bestCount = 5;
        double bestAnalysis = LottoService.getStandardDenominator(5);
        AnalysisSummary bestResult = null;
        for(int i = bestCount; i < 105; i++){
            Analysis frequencyInvertAnalysis = new FrequencyInvertAnalysis(roundService, i);
            AnalysisSummary result = lottoService.verify(52, frequencyInvertAnalysis);

            double analysis5 = result.getDenominatorMap().getOrDefault(5, bestAnalysis + 1);
            if(Math.min(analysis5, bestAnalysis) == analysis5){
                bestCount = i;
                bestAnalysis = analysis5;
                bestResult = result;
            }
        }
        if(bestResult != null){
            System.out.println(bestResult);
            System.out.printf("bestCount : %d\n", bestCount);
        } else {
            System.out.println("best is null");
        }
        //null;
    }

    @Test
    void verifyRetainNumbers() {
        // 아직
        RetainAnalysis retainAnalysis = new RetainAnalysis();
        retainAnalysis.setAnalysisList(
                new ExclusionAnalysis(roundService, 11)
                , new FrequencyAnalysis(roundService, 27));
        AnalysisSummary result = lottoService.verify(12, retainAnalysis);
        System.out.println(result);
    }

    @Test
    void saveVerificationResult() {
        lottoService.saveVerificationResult("ex", 12, 4);

        lottoService.saveVerificationResult("fr", 12, 4);
    }
}