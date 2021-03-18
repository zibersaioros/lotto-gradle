package com.rs.lottogradle.lotto.service;

import com.google.common.collect.Maps;
import com.rs.lottogradle.lotto.model.AnalysisPair;
import com.rs.lottogradle.lotto.model.Round;
import com.rs.lottogradle.lotto.repository.RoundRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    public List<Integer> getExclusionNumbers(int targetRound){
        Map<String, Integer> exclusionsMap = Maps.newHashMap();
        //마지막 회차 가져오기!
        int lastRound = roundService.getLastRound();
        int offset = lastRound - targetRound + 1;

        //전체 기록표 가져옴.
        List<Round> rounds = roundRepository.findAllByOrderByRoundDesc();

        //최근 20주만 체크.
        for(int start = offset; start < 20 + offset; start++){
            // 각 회차별로 과거 몇회차에서 제외수가 나왔는지 체크
            Round startRound = rounds.get(start);
            for(int diff = start + 1; diff < rounds.size() && diff <= start + 52; diff++){
                Round diffRound = rounds.get(diff);
                for(int i = 0; i < 7; i++){
                    if(!startRound.getNums().contains(diffRound.getNums_ord().get(i))){
                        String key = diff + "_" + i;
                        exclusionsMap.put(key, exclusionsMap.getOrDefault(key, 0) + 1);
                    }
                }
            }
        }

        // 분석용 리스트 셋업.
        List<Integer> exclusionNumbers = exclusionsMap.entrySet().stream()
                .filter(entry -> entry.getValue() > (20 * 0.9)) //번호 추출 19번 이상 제외된 것 번호 추출.
                .map(entry -> { //엔트리를 AnalysisPair로 변경
                    String[] arrKey = entry.getKey().split("_");
                    return new AnalysisPair(Integer.valueOf(arrKey[0]), Integer.valueOf(arrKey[1]), entry.getValue());
                })
                .map(analysisPair -> rounds.get(analysisPair.getDiff() - 1).getNums_ord().get(analysisPair.getIndex()))
                //analysisPair를 Integer로 변경
                .sorted(Comparator.naturalOrder()) // sort
                .distinct() // 중복 제거
                .collect(Collectors.toList());

       return exclusionNumbers;
    }

    public void verifyExclusionNumbers(int testCount){

        double[] denominatorSum = {0, 0, 0, 0};
        int[] invalidCount = {0, 0, 0, 0};

        StringBuffer sb = new StringBuffer();
        sb.append("========verifyExclusionNumbers start========").append("\n");
        for(int i = 0; i < testCount; i++){

            int round = roundService.getLastRound() - i;
            sb.append("---------- round : " + round + " start ----------- ").append("\n");
            List<Integer> expectedNumbers = invert(getExclusionNumbers(round));
            int hitCount = getHitCount(expectedNumbers, round);
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
            sb.append("---------- round : " + round + " end ----------- ").append("\n");
        }

        for(int win = 6; win > 2; win--){
            if(invalidCount[6-win] == testCount)
                continue;
            sb.append(String.format("analysis%d : 1 / %.2f", win, denominatorSum[6-win] / testCount * (testCount / (testCount-invalidCount[6-win])))).append("\n");
            sb.append("standard" + win + " : " + getHitRateString(45, 6, win)).append("\n");
        }
        sb.append("========verifyExclusionNumbers end========").append("\n");
        System.out.println(sb);
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

    public List<Integer> invert(List<Integer> list) {
        List<Integer> invertList = new ArrayList<Integer>();
        for(int i = 1; i <= 45; i++){
            if(!list.contains(i))
                invertList.add(i);
        }
        return invertList;
    }

    public int getHitCount(List<Integer> nums, int round){
        int before = nums.size();
        Round r = roundRepository.findByRound(round);
        nums.removeAll(r.getNums());
        return before - nums.size();
    }
}
