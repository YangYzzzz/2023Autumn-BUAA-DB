package com.example.service;

import com.example.mapper.AccessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessService {
    @Autowired
    private AccessMapper accessMapper;

    public void addAccess(Integer userId, String pageName) {
        accessMapper.addAccess(userId, pageName);
    }
}
