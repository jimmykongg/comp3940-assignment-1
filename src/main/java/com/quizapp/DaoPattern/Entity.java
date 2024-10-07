package com.quizapp.DaoPattern;

import java.util.List;
import java.util.Map;

public abstract class Entity {
    String entityType;
    public Entity(String entityType){
        this.entityType = entityType;
    }

    public String getType(){
        return entityType;
    }
    public abstract String serialize();
    public abstract Map<String, Object> getKeyValuePairs();
    public abstract List<String> getColumnsName();
}

