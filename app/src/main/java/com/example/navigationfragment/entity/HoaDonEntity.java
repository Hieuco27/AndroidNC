package com.example.navigationfragment.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoadon")
public class HoaDonEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private String hoaDonId;

}
