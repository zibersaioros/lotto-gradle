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

public class ExclusionInvertAnalysis extends ExclusionAnalysis {

    public ExclusionInvertAnalysis(RoundService roundService, Integer analyseCount) {
        super(roundService, analyseCount);
    }

    @Override
    public List<Integer> getNumbers(int targetRound) {
        return invert(super.getNumbers(targetRound));
    }
}
