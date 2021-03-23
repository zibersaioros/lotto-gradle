package com.rs.lottogradle.lotto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Verification {
    private String id;
    private int round;
    private String type;
    private int testCount;
    private int bestCount;
    private int targetWin;
}
