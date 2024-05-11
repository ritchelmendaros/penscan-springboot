package com.softeng.penscan.repository;

import com.softeng.penscan.model.Classes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassesRepository extends MongoRepository<Classes, String> {

    Classes findByTeacherid(String teacherid);
}
