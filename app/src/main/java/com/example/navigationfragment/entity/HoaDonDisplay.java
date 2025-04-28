package com.example.navigationfragment.entity;

import java.io.Serializable;

public class HoaDonDisplay implements Serializable {
    public HoaDonEntity hoaDon;

    public RoomEntity room;
    private KhachEntity khach;

    public KhachEntity getKhach() {
        return khach;
    }

    public void setKhach(KhachEntity khach) {
        this.khach = khach;
    }

    public HoaDonEntity getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDonEntity hoaDon) {
        this.hoaDon = hoaDon;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }
}

