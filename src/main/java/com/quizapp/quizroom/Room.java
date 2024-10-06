package com.quizapp.quizroom;

public class Room {

    private String roomID;
    private String categoryID;

    public Room(String categoryID, String roomID){
        this.roomID = roomID;
        this.categoryID = categoryID;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getCategoryID() {
        return categoryID;
    }
}
