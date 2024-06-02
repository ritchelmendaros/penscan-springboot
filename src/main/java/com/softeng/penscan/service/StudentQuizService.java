package com.softeng.penscan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;

import com.softeng.penscan.model.Quiz;
import com.softeng.penscan.model.Student;
import com.softeng.penscan.model.StudentQuiz;
import com.softeng.penscan.model.User;
import com.softeng.penscan.repository.QuizRepository;
import com.softeng.penscan.repository.StudentQuizRepository;
import com.softeng.penscan.repository.StudentRepository;
import com.softeng.penscan.repository.UserRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private StudentRepository studentRepository;

    public String addStudentQuiz(String quizid, MultipartFile image) throws IOException, InterruptedException {

        StudentQuiz studentQuiz = new StudentQuiz();
        studentQuiz.setQuizid(quizid);
        studentQuiz.setQuizimage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));

        String recognizedText = azureTextRecognitionService.recognizeText(image);

        String normalizedText = normalizeRecognizedText(recognizedText);
        String[] recognizedLines = normalizedText.split("\\n");

        String name = recognizedLines.length > 0 ? recognizedLines[0].trim() : "";
        String[] nameParts = name.split("\\s+");
        if (nameParts.length < 2) {
            throw new IllegalArgumentException("Invalid name format in recognized text: " + name);
        }
        String firstname = nameParts[0];
        String lastname = nameParts[1];

        Optional<User> userOptional = userRepository.findByFirstnameAndLastname(firstname, lastname);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found with name: " + name);
        }
        User user = userOptional.get();
        studentQuiz.setStudentid(user.getUserid());
        studentQuiz.setRecognizedtext(normalizedText);

        Optional<Quiz> quizOptional = quizRepository.findById(quizid);
        if (!quizOptional.isPresent()) {
            throw new IllegalArgumentException("Quiz not found with quizid: " + quizid);
        }
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

        studentQuiz = studentQuizRepository.insert(studentQuiz);

        Optional<Student> studentOptional = studentRepository.findByUserid(user.getUserid());
        if (!studentOptional.isPresent()) {
            throw new IllegalArgumentException("Student not found with userid: " + user.getUserid());
        }
        Student student = studentOptional.get();
        List<String> quizIds = student.getQuizid();
        quizIds.add(quizid);
        student.setQuizid(quizIds);
        studentRepository.save(student);

        return studentQuiz.getStudentquizid();
    }

    private String normalizeRecognizedText(String text) {
        // Step 1: Insert newlines before question numbers
        String normalizedText = text.replaceAll("(\\d+\\.\\s*)", "\n$1");

        // Step 2: Ensure only one space after the number and dot
        normalizedText = normalizedText.replaceAll("\\s*\\.\\s*", ". ").replaceAll("\\s+", " ");

        // Step 3: Trim leading and trailing whitespace
        normalizedText = normalizedText.trim();

        // Step 4: Remove multiple newlines
        normalizedText = normalizedText.replaceAll("\\n{2,}", "\n");

        // Step 5: Ensure the name is on the first line
        if (normalizedText.startsWith("\n")) {
            normalizedText = normalizedText.substring(1);
        }
        Pattern pattern = Pattern.compile("(\\d+\\.\\s[A-Z])");
        Matcher matcher = pattern.matcher(normalizedText);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "\n" + matcher.group(1));
        }
        matcher.appendTail(sb);
        normalizedText = sb.toString().trim();

        return normalizedText;
    }

    public StudentQuiz getStudentQuiz(String id) {
        return studentQuizRepository.findById(id).orElse(null);
    }

    public StudentQuiz getStudentQuizByStudentIdAndQuizId(String studentId, String quizId) {
        return studentQuizRepository.findByStudentidAndQuizid(studentId, quizId).orElse(null);
    }

    public Map<String, Integer> getScoresAndStudentIdsByQuizId(String quizId) {
        List<StudentQuiz> studentQuizzes = studentQuizRepository.findByQuizid(quizId);
        Map<String, Integer> scoresAndStudentIds = new HashMap<>();

        for (StudentQuiz studentQuiz : studentQuizzes) {
            scoresAndStudentIds.put(studentQuiz.getStudentid(), studentQuiz.getScore());
        }

        return scoresAndStudentIds;
    }

}
