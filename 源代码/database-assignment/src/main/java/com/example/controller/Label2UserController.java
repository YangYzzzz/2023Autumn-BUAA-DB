package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.Label2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/label2user")
public class Label2UserController {
    @Autowired
    Label2UserService label2UserService;

    @PostMapping("/set")
    public RestBean setLabel(@RequestParam int id, @RequestBody List<Integer> labelIds) {
        return label2UserService.setLabel(id, labelIds);
    }

    @GetMapping("/get")
    public RestBean getLabel(@RequestParam int id) {
        return label2UserService.getLabel(id);
    }
}
