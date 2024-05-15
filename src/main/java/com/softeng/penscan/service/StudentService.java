package com.softeng.penscan.service;

import com.softeng.penscan.model.Classes;
import com.softeng.penscan.model.Student;
import com.softeng.penscan.repository.ClassesRepository;
import com.softeng.penscan.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassesRepository classesRepository;

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    // public List<String> getClassIdsByUserId(String userid) {
    // List<Student> students = studentRepository.findByUserid(userid);
    // return students.stream()
    // .map(Student::getClassesid)
    // .collect(Collectors.toList());
    // }

    public Classes getClassDetails(String classId) {
        return classesRepository.findById(classId).orElse(null);
    }
}
