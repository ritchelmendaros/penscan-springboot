package com.softeng.penscan.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.softeng.penscan.model.StudentQuiz;

public interface StudentQuizRepository extends MongoRepository<StudentQuiz, String> {

    Optional<StudentQuiz> findByStudentid(String studentId);
}
