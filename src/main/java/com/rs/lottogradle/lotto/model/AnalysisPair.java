package com.rs.lottogradle.lotto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class AnalysisPair implements Comparable<AnalysisPair>{
    private int diff;
    private int index;
    private int cnt;

    @Override
    public int compareTo(@NotNull AnalysisPair o) {
        return Integer.compare(o.getCnt(), cnt);
    }
}
