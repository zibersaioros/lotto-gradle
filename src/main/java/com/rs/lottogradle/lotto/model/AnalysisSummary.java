package com.rs.lottogradle.lotto.model;

import com.google.common.collect.Maps;
import com.rs.lottogradle.lotto.service.LottoService;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AnalysisSummary {
    List<AnalysisResult> results = new ArrayList<>();
    Map<Integer, Double> denominatorMap = Maps.newHashMap();

    public void addResult(AnalysisResult result){
        results.add(result);
    }
    public void setDenominator(int win, double denominator){
        denominatorMap.put(win,  denominator);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (AnalysisResult result : results) {
            sb.append(result.toString());
        }
        for(int win = 6; win > 2; win--){
            sb.append(String.format("analysis%d : 1 / %.2f", win, denominatorMap.getOrDefault(win, 0.0))).append("\n");
            sb.append("standard" + win + " : " + LottoService.getHitRateString(45, 6, win)).append("\n");
        }
        return sb.toString();
    }
}
