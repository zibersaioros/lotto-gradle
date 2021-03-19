package com.rs.lottogradle.lotto.service;

import com.rs.lottogradle.common.rest.ApiCaller;
import com.rs.lottogradle.lotto.model.Round;
import com.rs.lottogradle.lotto.repository.RoundRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class RoundService {
    public static final int MIN = 262; //2007-12-08;

    public static final int YEAR = 2007;
    public static final int MONTH = 12;
    public static final int DAY = 8;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private ApiCaller apiCaller;

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

    public List<Round> findAllByOrderByRoundDesc(){
        return roundRepository.findAllByOrderByRoundDesc();
    }

    private void updateRound(int min){
        log.debug("마지막 회차 : {}" , getLastRound());
        int lastRound = getLastRound();

        for(int i = min; i <= lastRound; i++){
            Round r = apiCaller.getInfo(i);
            r.setRound(i);
            setNums(r);
            roundRepository.save(r);
        }
    }

    public static int getLastRound(){
        LocalDateTime old = LocalDateTime.of(YEAR, MONTH, DAY, 0, 0);
        long diff = System.currentTimeMillis() - old.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long diffRound = diff / (1000 * 60 * 60 * 24 * 7);
        return (int) diffRound + MIN;
    }

    private void setNums(Round r){
        List<Integer> nums = new ArrayList<Integer>();
        List<Integer> numsOrd = new ArrayList<>();
        nums.add(r.getNum1_ord());
        nums.add(r.getNum2_ord());
        nums.add(r.getNum3_ord());
        nums.add(r.getNum4_ord());
        nums.add(r.getNum5_ord());
        nums.add(r.getNum6_ord());

        numsOrd.addAll(nums);
        numsOrd.add(r.getNum7());
        r.setNums_ord(numsOrd);
        nums.sort(Comparator.naturalOrder());
        r.setNums(nums);
    }
}
