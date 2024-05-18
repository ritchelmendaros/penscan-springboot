package com.softeng.penscan.service;

import com.softeng.penscan.model.Class;
import com.softeng.penscan.model.Student;
import com.softeng.penscan.repository.ClassRepository;
import com.softeng.penscan.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Optional;

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

    public Student addClassToStudent(String userid, String classid) {
        Optional<Student> optionalStudent = studentRepository.findByUserid(userid);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            if (student.getClassid() == null) {
                student.setClassid(new ArrayList<>());
            }
            if (!student.getClassid().contains(classid)) {
                student.getClassid().add(classid);
                return studentRepository.save(student);
            }
            return student;
        } else {
            String errorMessage = "User with ID " + userid + " not found.";
            throw new IllegalArgumentException(errorMessage);
        }
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

    public String getStudentIdByUserId(String userid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudentIdByUserId'");
    }

}
