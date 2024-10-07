package com.quizapp.DaoPattern.Entities;

import com.quizapp.DaoPattern.Entity;

import java.util.List;
import java.util.Map;

public class User extends Entity {

    public User(){
        super("app_user");
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
