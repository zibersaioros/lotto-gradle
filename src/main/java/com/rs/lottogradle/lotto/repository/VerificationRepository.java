package com.rs.lottogradle.lotto.repository;

import com.rs.lottogradle.lotto.model.Verification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends MongoRepository<Verification, String> {

    Verification findByRoundAndTypeAndTestCountAndTargetWin(int round, String type, int testCount, int targetWin);
}
