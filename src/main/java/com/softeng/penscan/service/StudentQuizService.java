package com.softeng.penscan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;

import com.softeng.penscan.model.Quiz;
import com.softeng.penscan.model.StudentQuiz;
import com.softeng.penscan.model.User;
import com.softeng.penscan.repository.QuizRepository;
import com.softeng.penscan.repository.StudentQuizRepository;
import com.softeng.penscan.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StudentQuizService {

    @Autowired
    private StudentQuizRepository studentQuizRepository;

    @Autowired
    private AzureTextRecognitionService azureTextRecognitionService;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    public String addStudentQuiz(String quizid, MultipartFile image)
            throws IOException, InterruptedException {
        StudentQuiz studentQuiz = new StudentQuiz();
        studentQuiz.setQuizid(quizid);
        studentQuiz.setQuizimage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));

        String recognizedText = azureTextRecognitionService.recognizeText(image);

        String[] recognizedLines = recognizedText.split("\\n");

        String name = "";
        if (recognizedLines.length > 0) {
            name = recognizedLines[0].trim();
        }

        String[] nameParts = name.split("\\s+");
        if (nameParts.length < 2) {
            throw new IllegalArgumentException("Invalid name format in recognized text: " + name);
        }
        String firstname = nameParts[0];
        String lastname = nameParts[1];

        Optional<User> userOptional = userRepository.findByFirstnameAndLastname(firstname, lastname);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            studentQuiz.setStudentid(user.getUserid());
        } else {
            throw new IllegalArgumentException("User not found with name: " + name);
        }

        studentQuiz.setRecognizedtext(recognizedText);

        Optional<Quiz> quizOptional = quizRepository.findById(quizid);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            String answerKey = quiz.getQuizanswerkey();
            String[] answerKeyLines = answerKey.split("\\n");

            int matchingAnswersCount = 0;

            Pattern pattern = Pattern.compile("^(\\d+)\\.\\s(.*)$");
            for (String recognizedLine : recognizedLines) {
                Matcher matcher = pattern.matcher(recognizedLine.trim());
                if (matcher.find()) {
                    int lineNumber = Integer.parseInt(matcher.group(1));
                    String recognizedAnswer = matcher.group(2).trim();
                    if (lineNumber > 0 && lineNumber <= answerKeyLines.length) {
                        Matcher answerMatcher = pattern.matcher(answerKeyLines[lineNumber - 1].trim());
                        if (answerMatcher.find()) {
                            String answerKeyAnswer = answerMatcher.group(2).trim();
                            if (recognizedAnswer.equalsIgnoreCase(answerKeyAnswer)) {
                                matchingAnswersCount++;
                            }
                        }
                    }
                }
            }
            int score = matchingAnswersCount;
            studentQuiz.setScore(score);

        } else {
            throw new IllegalArgumentException("Quiz not found with quizid: " + quizid);
        }

        studentQuiz = studentQuizRepository.insert(studentQuiz);
        return studentQuiz.getStudentquizid();
    }

    public StudentQuiz getStudentQuiz(String id) {
        return studentQuizRepository.findById(id).orElse(null);
    }
}
