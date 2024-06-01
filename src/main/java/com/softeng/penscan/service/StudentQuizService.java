package com.softeng.penscan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import com.softeng.penscan.model.StudentQuiz;
import com.softeng.penscan.repository.StudentQuizRepository;

import java.io.IOException;

@Service
public class StudentQuizService {

    @Autowired
    private StudentQuizRepository studentQuizRepository;

    public String addStudentQuiz(String quizid, String studentid, int score, MultipartFile image) throws IOException {
        StudentQuiz studentQuiz = new StudentQuiz();
        studentQuiz.setQuizid(quizid);
        studentQuiz.setStudentid(studentid);
        studentQuiz.setScore(score);
        studentQuiz.setQuizimage(new Binary(BsonBinarySubType.BINARY, image.getBytes())); // Set image as binary

        studentQuiz = studentQuizRepository.insert(studentQuiz); // Save the studentQuiz object to the database
        return studentQuiz.getStudentquizid();
    }

    public StudentQuiz getStudentQuiz(String id) {
        return studentQuizRepository.findById(id).orElse(null);
    }
}
