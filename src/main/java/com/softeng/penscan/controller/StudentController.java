package com.softeng.penscan.controller;

import com.softeng.penscan.model.Student;
import com.softeng.penscan.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/addstudent")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student addedStudent = studentService.addStudent(student);
        return new ResponseEntity<>(addedStudent, HttpStatus.CREATED);
    }

    // @GetMapping("/getclassidbyuserid")
    // public ResponseEntity<List<String>>
    // getClassesByUserId(@RequestParam("studentid") String userid) {
    // List<String> classIds = studentService.getClassIdsByUserId(userid);
    // if (!classIds.isEmpty()) {
    // return ResponseEntity.ok(classIds);
    // } else {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(classIds);
    // }
    // }

    // @GetMapping("/getclassdetailsbyuserid")
    // public ResponseEntity<List<Classes>>
    // getClassDetailsByUserId(@RequestParam("studentid") String userid) {
    // List<String> classIds = studentService.getClassIdsByUserId(userid);
    // List<Classes> classDetails = new ArrayList<>();
    // for (String classId : classIds) {
    // Classes classDetail = studentService.getClassDetails(classId);
    // if (classDetail != null) {
    // classDetails.add(classDetail);
    // }
    // }
    // if (!classDetails.isEmpty()) {
    // return ResponseEntity.ok(classDetails);
    // } else {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(classDetails);
    // }
    // }
}
