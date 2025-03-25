package com.example.navigationfragment.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "contracts",
        foreignKeys = {
                @ForeignKey(
                        entity = RoomEntity.class,
                        parentColumns = "id",
                        childColumns = "roomId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = KhachEntity.class,
                        parentColumns = "khachId",
                        childColumns = "khachId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class ContractEntity implements Serializable {

    @PrimaryKey
    @NonNull
    private String contractId;

    @ColumnInfo(name = "roomId")
    private String roomId;

    @ColumnInfo(name = "khachId")
    private String khachId;
    private String startDate;
    private String endDate;
    private int numberOfGuests;
    private int numberOfCars;
    private double totalAmount;
    @ColumnInfo(name = "isStatus")
    private boolean isStatus;



    public ContractEntity() {

    }
    public ContractEntity(@NonNull String contractId, String roomId, String khachId, String startDate,
                          String endDate, int numberOfGuests, int numberOfCars,
                          double totalAmount, boolean isStatus) {
        this.contractId = contractId;
        this.roomId = roomId;
        this.khachId = khachId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.numberOfCars = numberOfCars;
        this.totalAmount = totalAmount;
        this.isStatus = isStatus;
    }


    @NonNull
    public String getContractId() {
        return contractId;
    }

    public void setContractId(@NonNull String contractId) {
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

    public String getKhachId() {
        return khachId;
    }

    public void setKhachId(String khachId) {
        this.khachId = khachId;
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
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

    @Override
    public String toString() {
        return "ContractEntity{" +
                "contractId='" + contractId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", khachId='" + khachId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", numberOfGuests=" + numberOfGuests +
                ", numberOfCars=" + numberOfCars +
                ", totalAmount=" + totalAmount +
                ", isStatus=" + isStatus +
                '}';
    }
}
