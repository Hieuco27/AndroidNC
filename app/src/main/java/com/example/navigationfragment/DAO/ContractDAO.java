package com.example.navigationfragment.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.ContractWithDetails;

import java.util.List;

@Dao

public interface ContractDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ContractEntity contract);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContracts(List<ContractEntity> contracts);

    @Query("SELECT * FROM contracts WHERE roomId = :roomId AND isStatus = 1 LIMIT 1")
    LiveData<ContractEntity> getActiveContractByRoom(String roomId);

    @Query("SELECT * FROM contracts WHERE roomId = :roomId LIMIT 1")
    ContractEntity getContractByRoomId(String roomId);


    @Query("SELECT * FROM contracts WHERE isStatus = 1 LIMIT 1")
    ContractEntity getActiveContract();

    @Query("SELECT COUNT(*) FROM contracts WHERE roomId = :roomId AND isStatus = 1")
    int countActiveContractsByRoom(String roomId);

    @Query("SELECT * FROM contracts WHERE contractId = :id LIMIT 1")
    ContractEntity getContractById(String id);

    @Transaction
    @Query("SELECT * FROM contracts")
    LiveData<List<ContractEntity>> getAllContracts();
    // trả về danh sách đồng bộ ko dùng livedata
    @Query("SELECT * FROM contracts")
    List<ContractEntity> getAllContractsSync();

    @Update
    void update(ContractEntity contract);

    @Delete
    void delete(ContractEntity contract);

    @Query("DELETE FROM contracts")
    void deleteAll();



    @Transaction
    @Query("SELECT * FROM contracts")
    List<ContractWithDetails> getAllContractsSync1();
    @Transaction
    @Query("SELECT * FROM contracts")
    LiveData<List<ContractWithDetails>> getAllContractsWithDetails();
}

