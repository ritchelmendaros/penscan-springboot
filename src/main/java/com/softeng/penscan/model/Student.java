package com.softeng.penscan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Student")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student {

    @Id
    private String studentid;
    private String userid;
    private List<String> classes;
    private List<String> quizId;
}
