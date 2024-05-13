package com.softeng.penscan.repository;

import com.softeng.penscan.model.Classes;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassesRepository extends MongoRepository<Classes, String> {

    Classes findByTeacherid(String teacherid);

    List<Classes> findAllByTeacherid(String teacherid);

    boolean existsByClassnameAndTeacherid(String classname, String teacherid);
}
