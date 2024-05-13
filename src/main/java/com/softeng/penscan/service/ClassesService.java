package com.softeng.penscan.service;

import com.softeng.penscan.model.Classes;
import com.softeng.penscan.repository.ClassesRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassesService {

    @Autowired
    private ClassesRepository classesRepository;

    public Classes addClass(Classes classes) {
        return classesRepository.save(classes);
    }

    public List<Classes> getClassesByTeacherId(String teacherid) {
        return classesRepository.findAllByTeacherid(teacherid);
    }

    public boolean checkClass(String classname, String teacherid) {
        return classesRepository.existsByClassnameAndTeacherid(classname, teacherid);
    }
}
