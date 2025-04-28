package com.example.navigationfragment.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class Asset {
    private String Id;
     private String name;
     private double compensationfee;
 ;
     public  Asset(){

     }

 public double getCompensationfee() {
  return compensationfee;
 }

 public void setCompensationfee(double compensationfee) {
  this.compensationfee = compensationfee;
 }

 public String getId() {
  return Id;
 }

 public void setId(String id) {
  Id = id;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }
}
