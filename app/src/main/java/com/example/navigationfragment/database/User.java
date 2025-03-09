package com.example.navigationfragment.database;

public class User {
    private  int id;
    private  String name;
    private Job job;
    private String Address;


    public User(int id, String name, Job job) {
        this.id = id;
        this.name = name;
        this.job = job;
    }

    public User(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", job=" + job +
                '}';
    }
}
