package com.rs.lottogradle.lotto.repository;

import com.rs.lottogradle.lotto.model.Round;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundRepository extends MongoRepository<Round, String> {
    Round findTopByOrderByRoundDesc();
    List<Round> findAllByOrderByRoundDesc();

    Round findByRound(int round);
}
