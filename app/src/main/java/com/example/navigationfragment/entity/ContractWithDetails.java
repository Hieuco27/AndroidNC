package com.example.navigationfragment.entity;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Relation;

public class ContractWithDetails {


        @Embedded
        public ContractEntity contract;

        @Relation(
                parentColumn = "roomId",
                entityColumn = "id"
        )
        public RoomEntity room;

        @Relation(
                parentColumn = "khachId",
                entityColumn = "khachId"
        )
        public KhachEntity khach;

}
