package com.softeng.penscan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;

import com.softeng.penscan.model.Quiz;
import com.softeng.penscan.model.StudentQuiz;
import com.softeng.penscan.repository.QuizRepository;
import com.softeng.penscan.repository.StudentQuizRepository;

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

    public String addStudentQuiz(String quizid, String studentid, MultipartFile image)
            throws IOException, InterruptedException {
        StudentQuiz studentQuiz = new StudentQuiz();
        studentQuiz.setQuizid(quizid);
        studentQuiz.setStudentid(studentid);
        studentQuiz.setQuizimage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));

        String recognizedText = azureTextRecognitionService.recognizeText(image);

        // Split the recognized text by lines
        String[] recognizedLines = recognizedText.split("\\n");

        studentQuiz.setRecognizedtext(recognizedText);

        // Fetch the quiz by quizid
        Optional<Quiz> quizOptional = quizRepository.findById(quizid);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            String answerKey = quiz.getQuizanswerkey();
            // Split the answer key by lines
            String[] answerKeyLines = answerKey.split("\\n");

            // Initialize matching answers count
            int matchingAnswersCount = 0;

            // Compare recognized lines with answer key lines
            Pattern pattern = Pattern.compile("^(\\d+)\\.\\s(.*)$");
            for (String recognizedLine : recognizedLines) {
                Matcher matcher = pattern.matcher(recognizedLine.trim());
                if (matcher.find()) {
                    int lineNumber = Integer.parseInt(matcher.group(1));
                    String recognizedAnswer = matcher.group(2).trim();
                    if (lineNumber > 0 && lineNumber <= answerKeyLines.length) {
                        // Extract the answer from the answer key
                        Matcher answerMatcher = pattern.matcher(answerKeyLines[lineNumber - 1].trim());
                        if (answerMatcher.find()) {
                            String answerKeyAnswer = answerMatcher.group(2).trim();
                            // Check if the recognized line matches the answer key line
                            if (recognizedAnswer.equalsIgnoreCase(answerKeyAnswer)) {
                                matchingAnswersCount++;
                            }
                        }
                    }
                }
            }

            // Calculate the score based on matching answers
            int score = matchingAnswersCount;
            studentQuiz.setScore(score);

        } else {
            // Handle case where quiz with given quizid is not found
            throw new IllegalArgumentException("Quiz not found with quizid: " + quizid);
        }

        studentQuiz = studentQuizRepository.insert(studentQuiz);
        return studentQuiz.getStudentquizid();
    }

    public StudentQuiz getStudentQuiz(String id) {
        return studentQuizRepository.findById(id).orElse(null);
    }
}
