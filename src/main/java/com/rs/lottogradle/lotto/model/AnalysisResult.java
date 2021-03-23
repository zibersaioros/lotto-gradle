package com.rs.lottogradle.lotto.model;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AnalysisResult {
    int targetRound;
    int count;
    int hit;

    Map<Integer, String> rateStringMap = Maps.newHashMap();

    public void setRateString(int win, String rateString){
        rateStringMap.put(win, rateString);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("---------- round : " + targetRound + " start ----------- ").append("\n");
        sb.append(String.format("count : %d hit : %d", count, hit)).append("\n");
        for(int win = 6; win > 2; win--){
            sb.append(win + " : " + rateStringMap.get(win)).append("\n");
        }
        sb.append("---------- round : " + targetRound + " end ----------- ").append("\n");
        return sb.toString();
    }
}
