package com.softeng.penscan.service;

import com.softeng.penscan.model.Class;
import com.softeng.penscan.model.Student;
import com.softeng.penscan.repository.ClassRepository;
import com.softeng.penscan.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classesRepository;

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    // public List<String> getClassIdsByUserId(String userid) {
    // List<Student> students = studentRepository.findByUserid(userid);
    // return students.stream()
    // .map(Student::getClassesid)
    // .collect(Collectors.toList());
    // }

    public Class getClassDetails(String classId) {
        return classesRepository.findById(classId).orElse(null);
    }
}
