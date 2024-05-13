package com.softeng.penscan.controller;

import com.softeng.penscan.model.Classes;
import com.softeng.penscan.service.ClassesService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classes")
public class ClassesController {

    @Autowired
    private ClassesService classesService;

    @PostMapping("/add")
    public Classes addClass(@RequestBody Classes classes) {
        return classesService.addClass(classes);
    }

    @GetMapping("/getclassesbyteacherid")
    public List<Classes> getClassesByTeacherId(@RequestParam("teacherid") String teacherid) {
        return classesService.getClassesByTeacherId(teacherid);
    }

    @GetMapping("/checkclass")
    public boolean checkClass(@RequestParam("classname") String classname,
            @RequestParam("teacherid") String teacherid) {
        return classesService.checkClass(classname, teacherid);
    }
}
