package com.example.navigationfragment;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.navigationfragment.DAO.ContractDAO;
import com.example.navigationfragment.DAO.HoaDonDAO;
import com.example.navigationfragment.DAO.KhachDAO;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.HoaDonEntity;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.RoomEntity;

@Database(entities = {RoomEntity.class, KhachEntity.class, ContractEntity.class, HoaDonEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {


    public abstract RoomDAO roomDao();
    public abstract KhachDAO khachDao();
    public abstract ContractDAO contractDao();
    public abstract HoaDonDAO hoaDonDao();
    private static volatile AppDatabase INSTANCE;


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "management_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
