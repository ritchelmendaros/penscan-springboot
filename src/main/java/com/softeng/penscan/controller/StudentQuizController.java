package com.softeng.penscan.controller;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

import com.softeng.penscan.model.StudentQuiz;
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

    @GetMapping("/get")
    public ResponseEntity<StudentQuiz> getStudentQuiz(@RequestParam("id") String id) {
        StudentQuiz studentQuiz = studentQuizService.getStudentQuiz(id);
        if (studentQuiz != null) {
            Binary imageData = studentQuiz.getQuizimage();
            if (imageData != null) {
                byte[] imageBytes = imageData.getData();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                studentQuiz.setBase64Image(base64Image);
            }
            return new ResponseEntity<>(studentQuiz, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
