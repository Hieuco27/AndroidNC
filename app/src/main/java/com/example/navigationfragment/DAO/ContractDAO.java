package com.example.navigationfragment.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.navigationfragment.entity.ContractDetail;
import com.example.navigationfragment.entity.ContractEntity;

import java.util.List;

@Dao
public interface ContractDAO {
    @Insert
    void insert(ContractEntity contract);

    @Query("SELECT * FROM contract WHERE roomId = :roomId AND isStatus = 1 LIMIT 1")
    LiveData<ContractEntity> getActiveContractByRoom(int roomId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContracts(List<ContractEntity> contracts);

    @Query("SELECT * FROM contract WHERE contractId = :contractId")
    LiveData<ContractEntity> getContractById(int contractId);
    @Transaction
    @Query("SELECT * FROM contract")
    LiveData<List<ContractEntity>> getAllContracts();
    @Update
    void update(ContractEntity contract);
    @Query("DELETE FROM contract")
    void deleteAll();
    @Delete
    void delete(ContractEntity contract);
}
