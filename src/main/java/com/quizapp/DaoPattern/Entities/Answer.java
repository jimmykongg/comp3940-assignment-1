package com.quizapp.DaoPattern.Entities;

import com.quizapp.DaoPattern.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Answer extends Entity {

    int quiz_id;
    String description;
    boolean right_answer;

    public Answer(){
        super("answers");
    }

    public Answer(String parameters){
        super("answers");
    }
    @Override
    public String serialize() {
        return null;
    }

    @Override
    public Map<String, Object> getKeyValuePairs() {
        return null;
    }

    @Override
    public List<String> getColumnsName() {
        return new ArrayList<>();
    }
}
