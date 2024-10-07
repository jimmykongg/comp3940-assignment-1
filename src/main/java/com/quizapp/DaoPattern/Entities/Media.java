package com.quizapp.DaoPattern.Entities;

import com.quizapp.DaoPattern.Entity;

import java.util.List;
import java.util.Map;

public class Media extends Entity {

    public Media(){
        super("media");
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
        return null;
    }
}
