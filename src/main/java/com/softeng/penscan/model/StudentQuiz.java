package com.softeng.penscan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "StudentQuiz")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentQuiz {

    @Id
    private String studentquizid;
    private String quizid;
    private String studentid;
    private int score;
}
