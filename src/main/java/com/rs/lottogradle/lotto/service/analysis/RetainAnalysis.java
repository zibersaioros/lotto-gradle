package com.rs.lottogradle.lotto.service.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RetainAnalysis implements Analysis{

    private List<Analysis> analysisList;

    public void setAnalysisList(Analysis... analysisList) {
        if(this.analysisList != null)
            this.analysisList.clear();

        this.analysisList = Arrays.asList(analysisList);
    }

    @Override
    public List<Integer> getNumbers(int targetRound) {
        if(analysisList == null)
            return null;

        List<Integer> set = new ArrayList<>();
        for(int i = 1; i <= 45; i++)
            set.add(i);

        for (Analysis analysis : analysisList) {
            set.retainAll(analysis.getNumbers(targetRound));
        }

        return set;
    }
}
