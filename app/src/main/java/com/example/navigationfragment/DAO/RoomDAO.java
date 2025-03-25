package com.example.navigationfragment.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.navigationfragment.entity.RoomEntity;

import java.util.List;

@Dao
public interface RoomDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RoomEntity room);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRooms(List<RoomEntity> rooms);

    // Cập nhật trạng thái phòng (Đã thuê hay chưa)
    @Query("UPDATE rooms SET trangThai = CASE WHEN :status THEN 1 ELSE 0 END WHERE soPhong = :soPhong")
    void updateRoomStatus(String soPhong, boolean status);

    // Lấy ID phòng theo số phòng
    @Query("SELECT id FROM rooms WHERE soPhong = :soPhong LIMIT 1")
    String getRoomIdBySoPhong(String soPhong);

    @Query("SELECT * FROM rooms WHERE id = :roomId LIMIT 1")
    RoomEntity getRoomByIdSync(String roomId);

    @Query("SELECT * FROM rooms WHERE id = :roomId")
    LiveData<RoomEntity> getRoomById(String roomId);

    @Query("SELECT * FROM rooms WHERE soPhong = :soPhong LIMIT 1")
    RoomEntity getRoomBySoPhongSync(String soPhong);

    // Lấy toàn bộ danh sách phòng (LiveData)
    @Query("SELECT * FROM rooms")
    LiveData<List<RoomEntity>> getAllRooms();

    // Lấy toàn bộ danh sách phòng (đồng bộ)
    @Query("SELECT * FROM rooms")
    List<RoomEntity> getAllRoomsSync();

    // Lấy thông tin phòng theo số phòng (LiveData)
    @Query("SELECT * FROM rooms WHERE soPhong = :soPhong LIMIT 1")
    LiveData<RoomEntity> getRoomBySoPhong(String soPhong);

    @Update
    void update(RoomEntity room);

    @Delete
    void delete(RoomEntity room);

    @Query("DELETE FROM rooms WHERE id = :id")
    void deleteRoomById(String id); // ✅ Đổi int thành String

    // Xóa phòng theo số phòng
    @Query("DELETE FROM rooms WHERE soPhong = :soPhong")
    void deleteRoomBySoPhong(String soPhong);

    @Query("DELETE FROM rooms")
    void deleteAllRooms();
}

