package com.example.navigationfragment.entity;



import androidx.room.Embedded;
import androidx.room.Relation;


public class KhachWithRoom {
    @Embedded
    public KhachEntity khach;
    @Relation(
            parentColumn = "roomId",
            entityColumn = "id"
    )
    public RoomEntity room;

}
