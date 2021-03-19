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

public class FrequencyInvertAnalysis extends FrequencyAnalysis {

    public FrequencyInvertAnalysis(RoundService roundService, int analyseCount) {
        super(roundService, analyseCount);
    }

    @Override
    public List<Integer> getNumbers(int targetRound) {
        return invert(super.getNumbers(targetRound));
    }
}
