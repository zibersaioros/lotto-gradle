package com.rs.lottogradle.lotto.service;

import com.rs.lottogradle.lotto.model.Round;
import com.rs.lottogradle.lotto.repository.RoundRepository;
import com.rs.lottogradle.lotto.service.analysis.Analysis;
import com.rs.lottogradle.lotto.service.analysis.ExclusionAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LottoService {

    @Autowired
    private RoundService roundService;

    @Autowired
    private RoundRepository roundRepository;


    double standard5;
    double standard4;
    double standard3;

    double standardDenominator6;
    double standardDenominator5;
    double standardDenominator4;
    double standardDenominator3;

    double[] standardDenominator = {
            getHitRateDenominator(45, 6, 6),
            getHitRateDenominator(45, 6, 5),
            getHitRateDenominator(45, 6, 4),
            getHitRateDenominator(45, 6, 3)
    };

    @PostConstruct
    public void postConstruct(){
        standard5 = getHitRate(45, 6, 5);
        standard4 = getHitRate(45, 6, 4);
        standard3 = getHitRate(45, 6, 3);
        standardDenominator6 = getHitRateDenominator(45, 6, 6);
        standardDenominator5 = getHitRateDenominator(45, 6, 5);
        standardDenominator4 = getHitRateDenominator(45, 6, 4);
        standardDenominator3 = getHitRateDenominator(45, 6, 3);
    }

    public String verify(int testCount, Analysis analysis){
        double[] denominatorSum = {0, 0, 0, 0};
        int[] invalidCount = {0, 0, 0, 0};

        StringBuffer sb = new StringBuffer();
        sb.append("========verifyExclusionNumbers start========").append("\n");
        for(int i = 0; i < testCount; i++){

            int targetRound = roundService.getLastRound() - i;
            sb.append("---------- round : " + targetRound + " start ----------- ").append("\n");
            List<Integer> expectedNumbers = analysis.getNumbers(targetRound);
            int hitCount = getHitCount(expectedNumbers, targetRound);
            sb.append("count : " + expectedNumbers.size() + " hit : " + hitCount).append("\n");
            for(int win = 6 ; win > 2; win--){
                sb.append(win + " : " + getHitRateString(expectedNumbers.size(), hitCount, win)).append("\n");

                double denominator = getHitRateDenominator(expectedNumbers.size(), hitCount, win);
                if(denominator == -1 || denominator > standardDenominator[6-win]){
                    denominator = standardDenominator[6-win];
                    invalidCount[6-win]++;
                }
                denominatorSum[6-win] += denominator;
            }
            sb.append("---------- round : " + targetRound + " end ----------- ").append("\n");
        }

        for(int win = 6; win > 2; win--){
            if(invalidCount[6-win] == testCount)
                continue;
            sb.append(String.format("analysis%d : 1 / %.2f", win, denominatorSum[6-win] / testCount * (testCount / (testCount-invalidCount[6-win])))).append("\n");
            sb.append("standard" + win + " : " + getHitRateString(45, 6, win)).append("\n");
        }
        sb.append("========verifyExclusionNumbers end========").append("\n");
        return sb.toString();
    }

    public double getHitRate(int all, int hitCount, int expect){
        try {
            return CombinatoricsUtils.binomialCoefficientDouble(hitCount, expect)
                    * CombinatoricsUtils.binomialCoefficientDouble(all - hitCount, 6 - expect)
                    / CombinatoricsUtils.binomialCoefficientDouble(all, 6);
        } catch (Exception e) {
            return 0;
        }
    }

    public double getHitRateDenominator(int all, int hitCount, int expect){
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
    public String getHitRateString(int all, int hitCount, int expect){
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
