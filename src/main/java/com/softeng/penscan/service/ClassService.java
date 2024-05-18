package com.softeng.penscan.service;

import com.softeng.penscan.model.Class;
import com.softeng.penscan.repository.ClassRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classesRepository;

    public Class addClass(Class classes) {
        return classesRepository.save(classes);
    }

    public List<Class> getClassesByTeacherId(String teacherid) {
        return classesRepository.findAllByTeacherid(teacherid);
    }

    public boolean checkClass(String classname, String teacherid) {
        return classesRepository.existsByClassnameAndTeacherid(classname, teacherid);
    }

    public List<Class> getClassDetails(List<String> classIds) {
        List<Class> classes = classesRepository.findAllById(classIds);
        return classes;
    }

}
