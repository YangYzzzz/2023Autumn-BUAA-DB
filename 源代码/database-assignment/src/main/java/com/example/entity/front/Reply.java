package com.example.entity.front;

import org.springframework.data.relational.core.sql.In;

import java.util.ArrayList;

public class Reply {
    public Integer total;
    public ArrayList<Comment2Front> list;

    public Reply() {
        total = 0;
        list = new ArrayList<>();
    }
}
