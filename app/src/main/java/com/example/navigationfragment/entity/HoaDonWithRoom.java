package com.example.navigationfragment.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class HoaDonWithRoom {
    @Embedded
    public HoaDonEntity hoaDon;


    @Relation(
            parentColumn = "hopdongId",
            entityColumn = "contractId",
            entity = ContractEntity.class
    )
    public ContractEntity contract;



    @Relation(
            parentColumn = "roomId",
            entityColumn = "id",
            entity = RoomEntity.class

    )
    public RoomEntity room;
}

