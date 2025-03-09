package com.example.navigationfragment.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.navigationfragment.entity.ContractDetail;
import com.example.navigationfragment.entity.ContractEntity;

import java.util.List;

@Dao
public interface ContractDAO {
    @Insert
    void insert(ContractEntity contract);

    @Query("SELECT * FROM contract WHERE roomId = :roomId AND isStatus = 1 LIMIT 1")
    LiveData<ContractEntity> getActiveContractByRoom(int roomId);


    @Transaction
    @Query("SELECT * FROM contract")
    LiveData<List<ContractDetail>> getAllContracts();
}
