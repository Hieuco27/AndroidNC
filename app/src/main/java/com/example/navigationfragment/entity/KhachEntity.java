package com.example.navigationfragment.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "khachthue",
        foreignKeys = @ForeignKey(
                entity = RoomEntity.class,
                parentColumns = "id",      // Khóa chính bên bảng RoomEntity
                childColumns = "roomId",   // Khóa ngoại bên bảng này
                onDelete = ForeignKey.CASCADE // Xóa phòng thì xóa luôn khách thuê phòng đó
        )
)
public class KhachEntity implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "khachId")  // Tên cột trong bảng
    private String khachId;

    @ColumnInfo(name = "roomId")   // Khóa ngoại ánh xạ với RoomEntity.id
    private String roomId;

    @ColumnInfo(name = "tenKhach")
    private String tenKhach;

    @ColumnInfo(name = "sdt")
    private String sdt;

    @ColumnInfo(name = "cccd")
    private String cccd;


    public KhachEntity() {
    }


    public KhachEntity(@NonNull String khachId, String roomId, String tenKhach, String sdt, String cccd) {
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

    public void setKhachId(@NonNull String khachId) {
        this.khachId = khachId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTenKhach() {
        return tenKhach;
    }

    public void setTenKhach(String tenKhach) {
        this.tenKhach = tenKhach;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    @Override
    public String toString() {
        return "KhachEntity{" +
                "khachId='" + khachId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", tenKhach='" + tenKhach + '\'' +
                ", sdt='" + sdt + '\'' +
                ", cccd='" + cccd + '\'' +
                '}';
    }
}
