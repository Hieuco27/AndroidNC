package com.example.navigationfragment.entity;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class ContractWithDetails {
    @Embedded
    public ContractEntity contract;

    @ColumnInfo(name = "soPhong")
    public String soPhong;

    @ColumnInfo(name = "tenKhach")
    public String tenKhach;

    public ContractEntity getContract() {
        return contract;
    }

    public void setContract(ContractEntity contract) {
        this.contract = contract;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public String getTenKhach() {
        return tenKhach;
    }

    public void setTenKhach(String tenKhach) {
        this.tenKhach = tenKhach;
    }
}
