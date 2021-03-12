package com.rs.lottogradle.lotto.service;

import com.rs.lottogradle.common.rest.ApiCaller;
import com.rs.lottogradle.lotto.model.Round;
import com.rs.lottogradle.lotto.repository.RoundRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Slf4j
public class LottoService {
    public static final int MIN = 262; //2007-12-08;

    public static final int YEAR = 2007;
    public static final int MONTH = 12;
    public static final int DAY = 8;

    double standard5;
    double standard4;
    double standard3;

    double standardDenominator5;
    double standardDenominator4;
    double standardDenominator3;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private ApiCaller apiCaller;

    @PostConstruct
    public void postConstruct(){
        standard5 = getHitRate(45, 6, 5);
        standard4 = getHitRate(45, 6, 4);
        standard3 = getHitRate(45, 6, 3);
        standardDenominator5 = getHitRateDenominator(45, 6, 5);
        standardDenominator4 = getHitRateDenominator(45, 6, 4);
        standardDenominator3 = getHitRateDenominator(45, 6, 3);
    }

    public void setupData(){
        Round lastStoredRound = roundRepository.findTopByOrderByRoundDesc();

        if(lastStoredRound == null){
            //처음부터 다시 받아옴.
            log.debug("처음부터 다시 받아옴.");
            updateRound(MIN);
        } else {
            updateRound(lastStoredRound.getRound() + 1);
            log.debug("마지막 데이터: {}", lastStoredRound.getDat());
        }

    }

    private void updateRound(int min){
        log.debug("마지막 회차 : {}" , getLastRound());
        int lastRound = getLastRound();

        for(int i = min; i <= lastRound; i++){
            Round r = apiCaller.getInfo(i);
            r.setRound(i);
            roundRepository.save(r);
        }
    }

    private int getLastRound(){
        LocalDateTime old = LocalDateTime.of(YEAR, MONTH, DAY, 0, 0);
        long diff = System.currentTimeMillis() - old.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long diffRound = diff / (1000 * 60 * 60 * 24 * 7);
        return (int) diffRound + MIN;
    }


    public double getHitRate(int all, int hitCount, int expect){
        try {
            return CombinatoricsUtils.binomialCoefficientDouble(hitCount, expect)
                    * CombinatoricsUtils.binomialCoefficientDouble(all - hitCount, 6 - expect)
                    / CombinatoricsUtils.binomialCoefficientDouble(all, 6);
        } catch (Exception e) {
            return 0;
        }
    }

    public double getHitRateDenominator(int all, int hitCount, int expect){
        try {
            return CombinatoricsUtils.binomialCoefficientDouble(all, 6)
                    / (CombinatoricsUtils.binomialCoefficientDouble(hitCount, expect)
                    * CombinatoricsUtils.binomialCoefficientDouble(all - hitCount, 6 - expect));
        } catch (Exception e) {
            return -1;
        }
    }
}
