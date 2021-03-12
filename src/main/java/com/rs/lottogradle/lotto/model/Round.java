package com.rs.lottogradle.lotto.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
public class Round {

    private String id;
    private Integer round;
    private String dat;
    private Integer chucheomgi;
    private Integer num1_ord;
    private Integer num2_ord;
    private Integer num3_ord;
    private Integer num4_ord;
    private Integer num5_ord;
    private Integer num6_ord;
    private Integer num7;
}
