package com.example.faan.mongo.file.repository;

import com.example.faan.mongo.file.model.entity.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPhotoRepository extends MongoRepository<Photo, String> {
    Photo findByImageHash(String imageHash);
}
