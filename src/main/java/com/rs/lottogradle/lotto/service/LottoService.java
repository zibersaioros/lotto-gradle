package com.rs.lottogradle.lotto.service;

import com.google.common.collect.Maps;
import com.rs.lottogradle.lotto.model.AnalysisResult;
import com.rs.lottogradle.lotto.model.AnalysisSummary;
import com.rs.lottogradle.lotto.model.Round;
import com.rs.lottogradle.lotto.model.Verification;
import com.rs.lottogradle.lotto.repository.RoundRepository;
import com.rs.lottogradle.lotto.repository.VerificationRepository;
import com.rs.lottogradle.lotto.service.analysis.Analysis;
import com.rs.lottogradle.lotto.service.analysis.ExclusionAnalysis;
import com.rs.lottogradle.lotto.service.analysis.FrequencyAnalysis;
import com.rs.lottogradle.lotto.service.analysis.RetainAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LottoService {

    @Autowired
    private RoundService roundService;

    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private VerificationRepository verificationRepository;

    static Map<Integer, Double> standardDenominatorMap;
    static {
        log.info("======== static called =======");
        standardDenominatorMap = Maps.newHashMap();
        for(int win = 6; win > 2; win--){
            standardDenominatorMap.put(win, getHitRateDenominator(45, 6, win));
        }
    }

    public static double getStandardDenominator(int win){
        log.info("======== getStandardDenominator called =======");
        return standardDenominatorMap.get(win);
    }


    public AnalysisSummary verify(int testCount, Analysis analysis){
        Map<Integer, Double> denominatorSum = Maps.newHashMap();
        Map<Integer, Integer> invalidCount = Maps.newHashMap();

        AnalysisSummary analysisSummary = new AnalysisSummary();
        for(int i = 0; i < testCount; i++){
            AnalysisResult result = new AnalysisResult();
            result.setTargetRound(roundService.getLastRound() - i);
            List<Integer> expectedNumbers = analysis.getNumbers(result.getTargetRound());
            result.setCount(expectedNumbers.size());
            result.setHit(getHitCount(expectedNumbers, result.getTargetRound()));
            for(int win = 6 ; win > 2; win--){
                result.setRateString(win, getHitRateString(result.getCount(), result.getHit(), win));
                double denominator = getHitRateDenominator(result.getCount(), result.getHit(), win);
                if(denominator == -1 || denominator > standardDenominatorMap.get(win)){
                    denominator = standardDenominatorMap.get(win);
                    invalidCount.put(win, invalidCount.getOrDefault(win, 0) + 1);
                }
                denominatorSum.put(win, denominatorSum.getOrDefault(win, 0.0) + denominator);
            }
            analysisSummary.addResult(result);
        }

        for(int win = 6; win > 2; win--){
            if(invalidCount.getOrDefault(win, 0) == testCount)
                continue;
            analysisSummary.setDenominator(win, denominatorSum.getOrDefault(win, 0.0) / testCount * (testCount / (testCount-invalidCount.getOrDefault(win, 0))));
        }

        return analysisSummary;
    }

    public Verification saveVerificationResult(String type, int testCount, int targetWin){
        //테스트 변수영역
        int maxAnalysisCount = 105;

        Verification verification = verificationRepository.findByRoundAndTypeAndTestCountAndTargetWin(RoundService.getLastRound() + 1, type, testCount, targetWin);
        if(verification != null) {
            log.info("already analysed");
            return verification;
        }

        int bestCount = 5;
        double bestAnalysis = getStandardDenominator(targetWin);
        AnalysisSummary bestResult = null;
        for(int i = bestCount; i < maxAnalysisCount; i++){
            Analysis analysis = null;
            if(type.equals("ex"))
                analysis = new ExclusionAnalysis(roundService, i);
            else
                analysis = new FrequencyAnalysis(roundService, i);

            AnalysisSummary result = verify(testCount, analysis);

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
        } else {
            bestCount = -1;
        }
        verification = new Verification(null, RoundService.getLastRound() + 1, type, testCount, bestCount, targetWin);
        return verificationRepository.save(verification);
    }


    public List<Integer> getVerifiedNumbers(int testCount, int targetWin) throws Exception {
        if(!roundService.isLatestData())
            throw new Exception("is not latest data");
        Verification exVerification = saveVerificationResult("ex", testCount, targetWin);
        Verification frVerification = saveVerificationResult("fr", testCount, targetWin);

        RetainAnalysis retainAnalysis = new RetainAnalysis();
        retainAnalysis.setAnalysisList(
                new ExclusionAnalysis(roundService, exVerification.getBestCount())
                , new FrequencyAnalysis(roundService, frVerification.getBestCount()));
        return retainAnalysis.getNumbers(RoundService.getLastRound() + 1);
    }

    public static double getHitRate(int all, int hitCount, int expect){
        try {
            return CombinatoricsUtils.binomialCoefficientDouble(hitCount, expect)
                    * CombinatoricsUtils.binomialCoefficientDouble(all - hitCount, 6 - expect)
                    / CombinatoricsUtils.binomialCoefficientDouble(all, 6);
        } catch (Exception e) {
            return 0;
        }
    }

    public static double getHitRateDenominator(int all, int hitCount, int expect){
        try {
            return CombinatoricsUtils.binomialCoefficientDouble(all, 6)
                    / (CombinatoricsUtils.binomialCoefficientDouble(hitCount, expect)
                    * CombinatoricsUtils.binomialCoefficientDouble(all - hitCount, 6 - expect));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 주어진 숫자에 해당하는 확률을 분수 형태 문자열로 리턴.
     * @param all
     * @param hitCount
     * @param expect
     * @return
     */
    public static String getHitRateString(int all, int hitCount, int expect){
        if(hitCount < expect
                || all - hitCount < 6 - expect
                || all < 6)
            return "0";

        String rate = String.format("1 / %.2f", getHitRateDenominator(all, hitCount, expect));

        return rate;
    }


    public int getHitCount(List<Integer> nums, int round){
        Round r = roundRepository.findByRound(round);
        int count = 0;
        for(int i = 0; i < 6; i++){
            if(nums.contains(r.getNums().get(i)))
                count++;
        }
        return count;
    }


}
