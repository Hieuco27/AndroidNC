package com.example.navigationfragment.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.navigationfragment.entity.HoaDonEntity;
import com.example.navigationfragment.entity.HoaDonWithRoom;

import java.util.List;

@Dao
public interface HoaDonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HoaDonEntity hoaDon);

    @Update
    void update(HoaDonEntity hoaDon);
    @Delete
    void delete(HoaDonEntity hoaDon);
    @Query( "DELETE FROM hoadon")
    void deleteAllHoaDon();

    @Query("SELECT * FROM hoadon")
    List<HoaDonEntity> getAll();
    @Transaction
    @Query("SELECT * FROM hoadon ")
    LiveData<List<HoaDonEntity>> getAllHoaDon();
    @Query("SELECT * FROM hoadon WHERE hoaDonId = :id")
    HoaDonEntity getHoaDonById(String id);
    @Query("DELETE FROM hoadon WHERE hoaDonId = :id")
    void deleteHoaDonById(String id);
    @Query("DELETE FROM hoadon")
    void deleteAll();
    @Query("SELECT * FROM hoadon WHERE hopdongId = :hopdongId")
    LiveData<List<HoaDonEntity>> getHoaDonByHopDongId(String hopdongId);

    @Transaction
    @Query("SELECT * FROM hoadon")
    LiveData<List<HoaDonWithRoom>> getAllHoaDonWithRoom();



    /*@Transaction
    @Query("SELECT hoadon.hoaDonId, hoadon.tenHoaDon, hoadon.ngayTao, hoadon.soDien, hoadon.soNuoc, hoadon.tongTien, hoadon.ghiChu, hoadon.daThanhToan, rooms.soPhong, rooms.giaPhong, rooms.giaDien, rooms.giaNuoc, rooms.giaDichVu " +
            "FROM hoadon " +
            "INNER JOIN contracts ON hoadon.hopdongId = contracts.contractId " +
            "INNER JOIN rooms ON contracts.roomId = rooms.id")
    LiveData<List<HoaDonWithRoom>> getAllHoaDonWithRoom();*/


}
