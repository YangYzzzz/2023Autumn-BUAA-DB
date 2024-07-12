package com.example.service;

import com.example.entity.Label;
import com.example.entity.RestBean;
import com.example.entity.front.LikeNotice2Front;
import com.example.mapper.LabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LabelService {
    @Autowired
    private LabelMapper labelMapper;


    public RestBean getLabel() {
        ArrayList<Label> labels = labelMapper.getLabels();
        HashMap<String, ArrayList<Pair<Integer, String>>> reLabels = new HashMap<>();
        for (Label label : labels) {
            if (!reLabels.containsKey(label.getType())) {
                ArrayList<Pair<Integer, String>> names = new ArrayList<>();
                reLabels.put(label.getType(), names);
                names.add(Pair.of(label.getId(), label.getName()));
            } else {
                reLabels.get(label.getType()).add(Pair.of(label.getId(), label.getName()));
            }
        }
        return RestBean.success(reLabels);
    }

    public List<Label> getAllLabel() {
        List<Label> labels = labelMapper.getLabels();
        List<Label> results = new ArrayList<>();
        int cnt = -1;
        HashMap<String, Label> labelHashMap = new HashMap<>(); // 标注type到label的映射
        for (Label label : labels) {
            if (!labelHashMap.containsKey(label.getType())) {
                Label headLabel = new Label(cnt, label.getType(), null, null, new ArrayList<>(), 0);
                cnt--;
                labelHashMap.put(label.getType(), headLabel);
            }
            Integer usedTime = labelMapper.getUsedTimesByLid(label.getId());
            label.setUsedTime(usedTime);
            labelHashMap.get(label.getType()).getChildren().add(label);
            labelHashMap.get(label.getType()).setUsedTime(labelHashMap.get(label.getType()).getUsedTime() + usedTime);
        }
        for (Map.Entry<String, Label> entry : labelHashMap.entrySet()) {
            results.add(entry.getValue());
        }
        return results;
    }

    public List<Label> getLabelWithName(String name) {
        List<Label> labels = labelMapper.getLabelWithName(name);
        for (Label label : labels) {
            Integer usedTime = labelMapper.getUsedTimesByLid(label.getId());
            label.setUsedTime(usedTime);
        }
        return labels;
    }

    public void updateLabel(Label label) {
        labelMapper.updateLabel(label.getId(), label.getType(), label.getName(), label.getWeight());
    }

    public void delLabel(Integer id) {
        labelMapper.delLabel(id);
    }

    public void addNewLabel(Label label) {
        labelMapper.addNewLabel(label.getType(), label.getName(), label.getWeight());
    }

    public List<Label> export() {
        List<Label> labels = labelMapper.getLabels();
        for (Label label : labels) {
            Integer usedTime = labelMapper.getUsedTimesByLid(label.getId());
            label.setUsedTime(usedTime);
        }
        return labels;
    }

    public void delBatchLabel(List<Integer> ids) {
        labelMapper.delBatch(ids);
    }

    public List<Integer> getUserLabelList(Integer uid) {
        return labelMapper.getUserLabelList(uid);
    }

    public List<String> getUserLabelNameList(Integer uid) {
        return labelMapper.getUserLabelNameList(uid);
    }
}
