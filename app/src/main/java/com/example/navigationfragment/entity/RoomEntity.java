package com.example.navigationfragment.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "rooms",
        indices = {@Index(value = "soPhong", unique = true)} // Đảm bảo soPhong không trùng lặp
)
public class RoomEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "soPhong")
    private String soPhong; // Số phòng, phải là duy nhất
    @ColumnInfo(name = "giaPhong")
    private double giaPhong;
    @ColumnInfo(name = "giaDien")
    private double giaDien;
    @ColumnInfo(name = "giaNuoc")
    private double giaNuoc;
    @ColumnInfo(name = "giaDichVu")
    private double giaDichVu;
    @ColumnInfo(name = "trangThai")
    private boolean trangThai;

    // ⭐ Constructor (Có thể thêm constructor không tham số nếu cần)
    public RoomEntity(String soPhong, double giaPhong, double giaDien, double giaNuoc, double giaDichVu, boolean trangThai) {
        this.soPhong = soPhong;
        this.giaPhong = giaPhong;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.giaDichVu = giaDichVu;
        this.trangThai = trangThai;
    }
public RoomEntity() {

}
    // ⭐ Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public double getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(double giaPhong) {
        this.giaPhong = giaPhong;
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

    public double getGiaDichVu() {
        return giaDichVu;
    }

    public void setGiaDichVu(double giaDichVu) {
        this.giaDichVu = giaDichVu;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
