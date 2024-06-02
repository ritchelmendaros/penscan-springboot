package com.softeng.penscan.controller;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
            @RequestParam("image") MultipartFile image) {
        try {
            String studentQuizId = studentQuizService.addStudentQuiz(quizid, image);
            return new ResponseEntity<>("Student quiz saved successfully with ID: " + studentQuizId, HttpStatus.OK);
        } catch (IOException | InterruptedException e) {
            return new ResponseEntity<>("Error saving student quiz: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getStudentQuizByStudentIdAndQuizId(
            @RequestParam("studentid") String studentId,
            @RequestParam("quizid") String quizId) {

        StudentQuiz studentQuiz = studentQuizService.getStudentQuizByStudentIdAndQuizId(studentId, quizId);
        if (studentQuiz != null) {
            Binary imageData = studentQuiz.getQuizimage();
            if (imageData != null) {
                byte[] imageBytes = imageData.getData();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                studentQuiz.setBase64Image(base64Image);
            }
            return new ResponseEntity<>(studentQuiz, HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No data found for the student ID and quiz ID");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}
