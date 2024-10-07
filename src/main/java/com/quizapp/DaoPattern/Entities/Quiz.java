package com.quizapp.DaoPattern.Entities;

import com.quizapp.DaoPattern.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Quiz extends Entity {

    int media_id;
    int category_id;
    String description;


    public Quiz(){
        super("quizzes");
    }

    public Quiz(String constructorParams){
        super("quizzes");
        String[] parameters = constructorParams.split(",");
        for(String parameter : parameters){
            String[] keyvaluePair = parameter.split("=");
            String key = keyvaluePair[0];
            String value = keyvaluePair[1];
            switch (key) {
                case "media_id" -> this.media_id = Integer.parseInt(value);
                case "category_id" -> this.category_id = Integer.parseInt(value);
                case "description" -> this.description = value;
            }
        }
    }

    public void setMedia_id(int media_id){
        this.media_id = media_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String serialize() {
        return "description=" + this.description
        +",media_id" + this.media_id +",category_id=" + this.category_id;
    }

    @Override
    public Map<String, Object> getKeyValuePairs() {
        return Map.of("description", description, "category_id", category_id, "media_id", media_id);
    }

    @Override
    public List<String> getColumnsName() {
        return List.of("media_id", "category_id", "description");
    }
}
