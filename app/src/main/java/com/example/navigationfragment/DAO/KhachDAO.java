package com.example.navigationfragment.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.KhachWithRoom;

import java.util.List;

@Dao
public interface KhachDAO {

    // Chèn một khách thuê mới, trả về ID của bản ghi vừa chèn
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(KhachEntity khach);
    @Update
    void update(KhachEntity khach);
    @Query("DELETE FROM khachthue WHERE khachId = :khachId")
    void delete(String khachId);

    // Chèn danh sách khách thuê (dùng khi cần nhập dữ liệu hàng loạt)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertKhachList(List<KhachEntity> khachs);

    @Query("SELECT * FROM khachthue")
    List<KhachEntity> getAllKhachSync();
    // Lấy khách thuê theo khachId (đồng bộ)
    @Query("SELECT * FROM khachthue WHERE khachId = :khachId LIMIT 1")
    KhachEntity getKhachByIdSync(String khachId);

    // Lấy một khách thuê theo khachId dưới dạng LiveData
    @Query("SELECT * FROM khachthue WHERE khachId = :khachId LIMIT 1")
    LiveData<KhachEntity> getTenantById(String khachId);

    // Lấy danh sách khách thuê kèm thông tin phòng theo roomId dưới dạng LiveData
    @Transaction
    @Query("SELECT * FROM khachthue WHERE roomId = :roomId")
    LiveData<List<KhachWithRoom>> getKhachWithRoom(String roomId);

    @Query("SELECT * FROM khachthue WHERE tenKhach = :tenKhach LIMIT 1")
    KhachEntity getKhachByTenSync(String tenKhach);

    // Lấy tất cả khách thuê kèm thông tin phòng dưới dạng LiveData
    @Transaction
    @Query("SELECT * FROM khachthue")
    LiveData<List<KhachWithRoom>> getAllKhachWithRoom();


}