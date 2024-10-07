package com.quizapp.DaoPattern;

import com.quizapp.DaoPattern.Entities.Answer;
import com.quizapp.DaoPattern.Entities.Quiz;

class EntityFactory {

    public static Entity create(String entityType){
        Entity entity = null;
        switch(entityType){
            case "quizzes" -> entity = new Quiz();
            case "answers" -> entity = new Answer();
        }
        return entity;
    }
    public static Entity createWithParameters(String entityType, String parameters){
        Entity entity = null;
        switch(entityType){
            case "quizzes" -> entity = new Quiz(parameters);
            case "answers" -> entity = new Answer();

        }
        return entity;
    }
}
