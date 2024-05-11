package com.softeng.penscan.model;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Document(collection = "User")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    private String userid;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String userType;

}
