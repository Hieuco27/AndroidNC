package com.example.navigationfragment.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "contract"
,foreignKeys = {
        @ForeignKey(entity = RoomEntity.class,
                parentColumns = "id",
                childColumns = "roomId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = KhachEntity.class,
                parentColumns = "khachId",
                childColumns ="khachId",onDelete = ForeignKey.CASCADE),
})
public class ContractEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int contractId;

    private int roomId;
    private String khachId;
    private String startDate;
    private String endDate;
    private int numberOfGuests;
    private int numberOfCars;
    private double totalAmount;
    private boolean isStatus;

    public ContractEntity(int contractId, String endDate, boolean isStatus, String khachId, int numberOfCars, int numberOfGuests, int roomId, String startDate, double totalAmount) {
        this.contractId = contractId;
        this.endDate = endDate;
        this.isStatus = isStatus;
        this.khachId = khachId;
        this.numberOfCars = numberOfCars;
        this.numberOfGuests = numberOfGuests;
        this.roomId = roomId;
        this.startDate = startDate;
        this.totalAmount = totalAmount;
    }
    public String getKhachId() {
        return khachId;
    }

    public void setKhachId(String khachId) {
        this.khachId = khachId;
    }
    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isStatus() {
        return isStatus;
    }

    public void setStatus(boolean status) {
        isStatus = status;
    }

    public int getNumberOfCars() {
        return numberOfCars;
    }

    public void setNumberOfCars(int numberOfCars) {
        this.numberOfCars = numberOfCars;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }



    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ContractEntity() {
    }

    @Override
    public String toString() {
        return "ContractEntity{" +
                "contractId=" + contractId +
                ", roomId=" + roomId +
                ", tenantId=" + khachId+
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", numberOfGuests=" + numberOfGuests +
                ", numberOfCars=" + numberOfCars +
                ", totalAmount=" + totalAmount +
                ", isStatus=" + isStatus +
                '}';
    }
}
