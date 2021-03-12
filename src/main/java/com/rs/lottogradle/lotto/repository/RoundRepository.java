package com.rs.lottogradle.lotto.repository;

import com.rs.lottogradle.lotto.model.Round;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends MongoRepository<Round, String> {
    Round findTopByOrderByRoundDesc();
}
