package com.rs.lottogradle.lotto.controller;

import com.rs.lottogradle.lotto.model.http.RecommendRequest;
import com.rs.lottogradle.lotto.model.http.RecommendResponse;
import com.rs.lottogradle.lotto.service.LottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@RestController
public class LottoController {

    @Autowired
    LottoService lottoService;

    @GetMapping("/recommend")
    public Object recommend(RecommendRequest request) throws Exception {

        List<Integer> verifiedNumbers = lottoService.getVerifiedNumbers(request.getTestCount(), request.getTargetWin());

        RecommendResponse response = new RecommendResponse();
        response.setRecommended(new ArrayList<>());
        response.setVerifiedNumbers(verifiedNumbers);
        response.setSize(verifiedNumbers.size());

        if(verifiedNumbers.size() < 6){
            return response;
        }

        Random random = new Random();
        for(int i = 0; i < request.getGameCount(); i++){
            List<Integer> nums = new ArrayList<>();
            for(int j = 0; j < 6; j++){
                int index = random.nextInt(verifiedNumbers.size());
                if(nums.contains(verifiedNumbers.get(index))){
                    j--;
                    continue;
                }
                nums.add(verifiedNumbers.get(index));
            }
            nums.sort(Comparator.naturalOrder());
            response.getRecommended().add(nums);
        }
        return response;
    }
}
