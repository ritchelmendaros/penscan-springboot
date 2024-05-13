package com.softeng.penscan.repository;

import com.softeng.penscan.model.Student;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    List<Student> findByUserid(String userid);
}
