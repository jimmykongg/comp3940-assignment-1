package com.quizapp.quizroom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoomManager {

    private static Map<String, Room> roomMap = new HashMap<>();

    public static boolean hasID(String roomID){
        return roomMap.containsKey(roomID);
    }

    public static void addRoom(String roomID, Room newRoom) { roomMap.put(roomID, newRoom); }

    public static Map<String, Room> getRoomMap() { return roomMap; }
}
