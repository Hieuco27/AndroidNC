package com.example.navigationfragment.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class KhachWithContract {
    @Embedded
    public KhachEntity khach;
    @Relation(parentColumn = "id", entityColumn = "khachId")
    public List<ContractEntity> contracts;


}
