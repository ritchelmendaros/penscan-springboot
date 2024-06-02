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

    @Autowired
    private AzureTextRecognitionService azureTextRecognitionService;

    public String addStudentQuiz(String quizid, String studentid, MultipartFile image)
            throws IOException, InterruptedException {
        StudentQuiz studentQuiz = new StudentQuiz();
        studentQuiz.setQuizid(quizid);
        studentQuiz.setStudentid(studentid);
        studentQuiz.setQuizimage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));

        String recognizedText = azureTextRecognitionService.recognizeText(image);
        studentQuiz.setRecognizedtext(recognizedText);

        studentQuiz = studentQuizRepository.insert(studentQuiz);
        return studentQuiz.getStudentquizid();
    }

    public StudentQuiz getStudentQuiz(String id) {
        return studentQuizRepository.findById(id).orElse(null);
    }
}
