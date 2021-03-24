package com.rs.lottogradle.lotto.model.http;

import lombok.Data;

import java.util.List;

@Data
public class RecommendResponse {
    private List<List<Integer>> recommended;
    private List<Integer> verifiedNumbers;
    private int size;
}
