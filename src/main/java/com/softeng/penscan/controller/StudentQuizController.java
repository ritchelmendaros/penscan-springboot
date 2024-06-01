package com.softeng.penscan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.softeng.penscan.service.StudentQuizService;

import java.io.IOException;

@RestController
@RequestMapping("/api/studentquiz")
public class StudentQuizController {

    @Autowired
    private StudentQuizService studentQuizService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadStudentQuiz(@RequestParam("quizid") String quizid,
            @RequestParam("studentid") String studentid,
            @RequestParam("score") int score,
            @RequestParam("image") MultipartFile image) {
        try {
            String studentQuizId = studentQuizService.addStudentQuiz(quizid, studentid, score, image);
            return new ResponseEntity<>("Student quiz saved successfully with ID: " + studentQuizId, HttpStatus.OK);
        } catch (IOException | InterruptedException e) {
            return new ResponseEntity<>("Error saving student quiz: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
