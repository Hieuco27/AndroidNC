package com.example.navigationfragment.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoadon",
        foreignKeys = {
                @ForeignKey(entity = ContractEntity.class,
                        parentColumns = "contractId",
                        childColumns = "hopdongId",
                        onDelete = ForeignKey.CASCADE)
        }
)

public class HoaDonEntity {

    @PrimaryKey
    @NonNull
    private String hoaDonId;
     private String hopdongId;
     @ColumnInfo(name="roomId")
     private String roomId;

     private String soPhong;
    private String tenHoaDon;
    private String ngayTao;
    private int soDien;
    private int soNuoc;
    private double tongTien;
    private String ghiChu;
    private boolean daThanhToan;

    public HoaDonEntity() {
    }

    public HoaDonEntity(@NonNull String hoaDonId, String hopdongId, String tenHoaDon, String ngayTao, int soDien, int soNuoc, double tongTien, String ghiChu, boolean daThanhToan) {
        this.hoaDonId = hoaDonId;
        this.hopdongId = hopdongId;
        this.tenHoaDon = tenHoaDon;
        this.ngayTao = ngayTao;
        this.soDien = soDien;
        this.soNuoc = soNuoc;
        this.tongTien = tongTien;
        this.ghiChu = ghiChu;
        this.daThanhToan = daThanhToan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    @NonNull
    public String getHoaDonId() {
        return hoaDonId;
    }

    public void setHoaDonId(@NonNull String hoaDonId) {
        this.hoaDonId = hoaDonId;
    }

    public String getHopdongId() {
        return hopdongId;
    }

    public void setHopdongId(String hopdongId) {
        this.hopdongId = hopdongId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public boolean isDaThanhToan() {
        return daThanhToan;
    }

    public void setDaThanhToan(boolean daThanhToan) {
        this.daThanhToan = daThanhToan;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public int getSoDien() {
        return soDien;
    }

    public void setSoDien(int soDien) {
        this.soDien = soDien;
    }

    public int getSoNuoc() {
        return soNuoc;
    }

    public void setSoNuoc(int soNuoc) {
        this.soNuoc = soNuoc;
    }

    public String getTenHoaDon() {
        return tenHoaDon;
    }

    public void setTenHoaDon(String tenHoaDon) {
        this.tenHoaDon = tenHoaDon;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}
