package com.example.service;

import com.example.entity.Label;
import com.example.entity.RestBean;
import com.example.mapper.Label2UserMapper;
import com.example.mapper.LabelMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Label2UserService {
    @Autowired
    Label2UserMapper label2UserMapper;
    @Autowired
    LabelMapper labelMapper;
    public RestBean setLabel(int id, List<Integer> labelIds) {
        for (Integer labelId : labelIds) {
            label2UserMapper.setLabel(id, labelId);
        }
        return RestBean.success();
    }

    public RestBean getLabel(int id) {
        ArrayList<Integer> labelIds = label2UserMapper.getLabel(id);
        ArrayList<Pair<Integer, String>> re = new ArrayList<>();
        for (Integer labelId : labelIds) {
            re.add(Pair.of(labelId, labelMapper.getLabelById(labelId)));
        }
        return RestBean.success(re);
    }
}
