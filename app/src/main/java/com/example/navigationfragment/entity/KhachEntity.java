package com.example.navigationfragment.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "khachthue",
        foreignKeys = {
                @ForeignKey(entity = RoomEntity.class,
                        parentColumns = "id",
                        childColumns = "roomId",
                        onDelete = ForeignKey.CASCADE)
        })
public class KhachEntity implements Serializable {

    @PrimaryKey
    @NonNull
    private String khachId;
    private int roomId;
    private String tenKhach;
    private String sdt;
    private String cccd;

    public KhachEntity(@NonNull String khachId, int roomId, String tenKhach, String sdt, String cccd) {
        this.khachId = khachId;
        this.roomId = roomId;
        this.tenKhach = tenKhach;
        this.sdt = sdt;
        this.cccd = cccd;
    }

    @NonNull
    public String getKhachId() {
        return khachId;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getTenKhach() {
        return tenKhach;
    }

    public String getSdt() {
        return sdt;
    }

    public String getCccd() {
        return cccd;
    }


    @Override
    public String toString() {
        return "KhachEntity{" +
                "khachId='" + khachId + '\'' +
                ", roomId=" + roomId +
                ", tenKhach='" + tenKhach + '\'' +
                ", sdt='" + sdt + '\'' +
                ", cccd='" + cccd + '\'' +
                '}';
    }
}
