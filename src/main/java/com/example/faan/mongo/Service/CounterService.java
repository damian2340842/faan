package com.example.faan.mongo.Service;

import com.example.faan.mongo.modelos.Counter;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.math.BigInteger;

@Service
public class CounterService {

    private final MongoOperations mongoOperations;

    public CounterService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public BigInteger getNextSequence(String sequenceName) {
        Counter counter = mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(sequenceName)),
                new Update().inc("value", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Counter.class
        );
        return counter.getValue();
    }
}