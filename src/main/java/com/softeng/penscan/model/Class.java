package com.softeng.penscan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Class")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Class {

    @Id
    private String classid;
    private String classname;
    private String teacherid;

}
