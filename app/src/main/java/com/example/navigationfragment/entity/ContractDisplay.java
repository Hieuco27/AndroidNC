package com.example.navigationfragment.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;

public class ContractDisplay implements Serializable {

        public ContractEntity contract;
        private KhachEntity khach;
        private RoomEntity room;

        public ContractEntity getContract() {
                return contract;
        }

        public void setContract(ContractEntity contract) {
                this.contract = contract;
        }

        public KhachEntity getKhach() {
                return khach;
        }

        public void setKhach(KhachEntity khach) {
                this.khach = khach;
        }

        public RoomEntity getRoom() {
                return room;
        }

        public void setRoom(RoomEntity room) {
                this.room = room;
        }
}
