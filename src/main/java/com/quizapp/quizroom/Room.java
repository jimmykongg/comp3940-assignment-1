package com.quizapp.quizroom;

import java.util.Set;

public class Room {

    private String roomID;
    private String categoryID;

    public Room(String categoryID, String roomID){
        this.roomID = roomID;
        this.categoryID = categoryID;
    }
}
