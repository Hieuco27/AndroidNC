package com.example.navigationfragment.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

@Entity(
        tableName = "rooms",
        indices = {@Index(value = "soPhong", unique = true)} // Đảm bảo soPhong không trùng lặp
)
public class RoomEntity implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "soPhong")
    private String soPhong;

    @ColumnInfo(name = "giaPhong")
    private double giaPhong;

    private double giaDien;
    private double giaNuoc;
    private double giaDichVu;
    private boolean trangThai;


    public RoomEntity() {
        this.id = UUID.randomUUID().toString();
    }


    public RoomEntity(@NonNull String id, String soPhong, double giaPhong, double giaDien, double giaNuoc, double giaDichVu, boolean trangThai) {
        this.id = UUID.randomUUID().toString();
        this.soPhong = soPhong;
        this.giaPhong = giaPhong;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.giaDichVu = giaDichVu;
        this.trangThai = trangThai;
    }

    // Nếu cần thêm constructor tuỳ biến, nhớ gán đầy đủ các thuộc tính
    public RoomEntity(String soPhong, double giaPhong, double giaDien, double giaNuoc, double giaDichVu, boolean trangThai) {
        this.soPhong = soPhong;
        this.giaPhong = giaPhong;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.giaDichVu = giaDichVu;
        this.trangThai = trangThai;
    }

    // Getter & Setter

    public double getGiaDichVu() {
        return giaDichVu;
    }

    public void setGiaDichVu(double giaDichVu) {
        this.giaDichVu = giaDichVu;
    }

    public double getGiaDien() {
        return giaDien;
    }

    public void setGiaDien(double giaDien) {
        this.giaDien = giaDien;
    }

    public double getGiaNuoc() {
        return giaNuoc;
    }

    public void setGiaNuoc(double giaNuoc) {
        this.giaNuoc = giaNuoc;
    }

    public double getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(double giaPhong) {
        this.giaPhong = giaPhong;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "RoomEntity{" +
                "giaDichVu=" + giaDichVu +
                ", id='" + id + '\'' +
                ", soPhong='" + soPhong + '\'' +
                ", giaPhong=" + giaPhong +
                ", giaDien=" + giaDien +
                ", giaNuoc=" + giaNuoc +
                ", trangThai=" + trangThai +
                '}';
    }
}
