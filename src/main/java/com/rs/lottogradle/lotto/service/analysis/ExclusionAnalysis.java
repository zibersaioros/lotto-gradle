package com.rs.lottogradle.lotto.service.analysis;

import com.google.common.collect.Maps;
import com.rs.lottogradle.lotto.model.AnalysisPair;
import com.rs.lottogradle.lotto.model.Round;
import com.rs.lottogradle.lotto.service.RoundService;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ExclusionAnalysis implements Analysis {

    protected RoundService roundService;
    protected Integer analyseCount;

    @Override
    public List<Integer> getNumbers(int targetRound) {
        Map<String, Integer> exclusionsMap = Maps.newHashMap();
        //마지막 회차 가져오기!
        int lastRound = RoundService.getLastRound();
        int offset = lastRound - targetRound + 1;

        //전체 기록표 가져옴.
        List<Round> rounds = roundService.findAllByOrderByRoundDesc();

        //최근 analyseCount주만큼 체크.
        for(int start = offset; start < analyseCount + offset; start++){
            // 각 회차별로 과거 몇회차에서 제외수가 나왔는지 체크
            Round startRound = rounds.get(start);
            for(int diff = start + 1; diff < rounds.size() && diff <= start + analyseCount; diff++){
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
                .filter(entry -> entry.getValue() > (analyseCount * 0.9)) // 90% 확률위로 제외된 것 번호 추출.
                .map(entry -> { //엔트리를 AnalysisPair로 변경
                    String[] arrKey = entry.getKey().split("_");
                    return new AnalysisPair(Integer.valueOf(arrKey[0]), Integer.valueOf(arrKey[1]), entry.getValue());
                })
                .map(analysisPair -> rounds.get(analysisPair.getDiff() - 1 + offset).getNums_ord().get(analysisPair.getIndex()))
                //analysisPair를 Integer로 변경
                .sorted(Comparator.naturalOrder()) // sort
                .distinct() // 중복 제거
                .collect(Collectors.toList());

        return invert(exclusionNumbers);
    }
}
