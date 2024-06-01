package com.softeng.penscan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.softeng.penscan.model.StudentQuiz;

public interface StudentQuizRepository extends MongoRepository<StudentQuiz, String> {
}
