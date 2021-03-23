package com.rs.lottogradle.lotto.service;

import com.rs.lottogradle.lotto.model.Round;
import com.rs.lottogradle.lotto.repository.RoundRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class RoundServiceTest {

    @Autowired
    RoundService roundService;

    @Autowired
    RoundRepository roundRepository;

    @Test
    void testSetData() {
        roundService.setupData();
    }

    @Test
    void testSave(){

        Round r = new Round();
        r.setRound(955);
        r.setChucheomgi(3);
        r.setNum1_ord(23);
        r.setNum2_ord(26);
        r.setNum3_ord(33);
        r.setNum4_ord(29);
        r.setNum5_ord(4);
        r.setNum6_ord(9);
        r.setNum7(8);
        r.setNums(Arrays.asList(4, 9, 23, 26, 29, 33));
        r.setNums_ord(Arrays.asList(23, 26, 33, 29, 4, 9, 8));
        r.setDat("2021-03-20");
        roundRepository.save(r);
    }

}