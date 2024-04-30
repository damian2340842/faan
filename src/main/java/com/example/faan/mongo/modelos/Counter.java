package com.example.faan.mongo.modelos;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Data
@Document(collection = "counters")
public class Counter {

        @Id
        private String id;
        private BigInteger value;

}
