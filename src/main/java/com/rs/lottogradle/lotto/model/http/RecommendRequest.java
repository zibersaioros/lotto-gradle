package com.rs.lottogradle.lotto.model.http;

import lombok.Data;

@Data
public class RecommendRequest {
    private int gameCount;
    private int testCount;
    private int targetWin;
}
