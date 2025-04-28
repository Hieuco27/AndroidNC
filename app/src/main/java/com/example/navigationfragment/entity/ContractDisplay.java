package com.example.navigationfragment.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ContractDisplay {

        public ContractEntity contract;
        public String khachName;

        public ContractEntity getContract() {
                return contract;
        }

        public void setContract(ContractEntity contract) {
                this.contract = contract;
        }

        public String getKhachName() {
                return khachName;
        }

        public void setKhachName(String khachName) {
                this.khachName = khachName;
        }
}
