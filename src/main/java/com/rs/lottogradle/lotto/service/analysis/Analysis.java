package com.rs.lottogradle.lotto.service.analysis;

import java.util.ArrayList;
import java.util.List;

public interface Analysis {
    default List<Integer> invert(List<Integer> list) {
        List<Integer> invertList = new ArrayList<>();
        for(int i = 1; i <= 45; i++){
            if(!list.contains(i))
                invertList.add(i);
        }
        return invertList;
    }

    List<Integer> getNumbers(int targetRound);
}
