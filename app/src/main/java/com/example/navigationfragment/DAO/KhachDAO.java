package com.example.navigationfragment.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.KhachWithRoom;

import java.util.List;

@Dao
public interface KhachDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(KhachEntity khach);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertKhachList(List<KhachEntity> khachs); // Đổi tên cho rõ ràng hơn

    @Query("SELECT * FROM khachthue WHERE roomId = :roomId LIMIT 1")
    KhachEntity getTenantByRoomIdSync(int roomId);

    @Query("SELECT * FROM khachthue")
    List<KhachWithRoom> getAllKhachSync();


    @Query("SELECT * FROM khachthue WHERE khachId = :khachId LIMIT 1")
    LiveData<KhachEntity> getTenantById(String khachId);


    @Transaction
    @Query("SELECT * FROM khachthue WHERE roomId = :roomId")
    LiveData<List<KhachWithRoom>> getKhachWithRoom(String roomId);


    @Transaction
    @Query("SELECT * FROM khachthue")
    LiveData<List<KhachWithRoom>> getAllKhachWithRoom();


}

