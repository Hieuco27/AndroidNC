package com.example.navigationfragment.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Map;
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
    private Map<String, Boolean> assets;
    private String khachId;
    private String status;//ko co khach: null hoac la
    // "NoCustomer" "hop dong co hieu luc" la "ContractValid"
    // "hop dong ko hieu luc" "ContractNotValid"
    //co hoa don" hasBill khong hoa don "No bill"

    public String getKhachId() {
        return khachId;
    }

    public void setKhachId(String khachId) {
        this.khachId = khachId;
    }

    public Map<String, Boolean> getAssets() {
        return assets;
    }

    public void setAssets(Map<String, Boolean> assets) {
        this.assets = assets;
    }

    public RoomEntity() {}

    public RoomEntity(@NonNull String id, String soPhong, double giaPhong, double giaDien, double giaNuoc, double giaDichVu, boolean trangThai) {
        this.id = id;
        this.soPhong = soPhong;
        this.giaPhong = giaPhong;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.giaDichVu = giaDichVu;
    }

    // Nếu cần thêm constructor tuỳ biến, nhớ gán đầy đủ các thuộc tính
    public RoomEntity(String soPhong, double giaPhong, double giaDien, double giaNuoc, double giaDichVu) {
        this.soPhong = soPhong;
        this.giaPhong = giaPhong;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.giaDichVu = giaDichVu;
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



}
