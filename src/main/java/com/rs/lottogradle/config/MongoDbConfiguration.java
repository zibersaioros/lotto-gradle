package com.rs.lottogradle.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.rs.lottogradle.lotto.repository.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.rs.lottogradle.lotto.repository")
public class MongoDbConfiguration
//        extends AbstractMongoClientConfiguration
{

//    @Override
//    public String getDatabaseName() {
//        return "lotto";
//    }
//
//    @Override
//    protected void configureClientSettings(Builder builder) {
//
//        builder
//                .credential(MongoCredential.createCredential("name", "db", "pwd".toCharArray()))
//                .applyToClusterSettings(settings  -> {
//                    settings.hosts(singletonList(new ServerAddress("127.0.0.1", 27017)));
//                });
//    }

    @Bean
    public MongoClientFactoryBean mongo(){
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setHost("localhost");
        mongo.setPort(27017);
        return mongo;
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(@Autowired MongoClient client){
        return new SimpleMongoClientDatabaseFactory(client, "lotto");
    }

    @Bean
    public MongoTemplate mongoTemplate(@Autowired MongoDatabaseFactory mongoDatabaseFactory){
        return new MongoTemplate(mongoDatabaseFactory);
    }
}
