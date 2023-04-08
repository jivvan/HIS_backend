package com.jira.health_information_system.api.model;

import java.util.Date;

public class Patient {

    public enum Gender {
        MALE,
        FEMALE,
        OTHERS
    }

    private int id;
    private int age;

    private Gender gender;
    private String name, address, phone;
    private Date dob;

    public Patient(int age, Gender gender, String name, String address, String phone, Date dob) {
        this.age = age;
        this.gender = gender;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.dob = dob;
    }

    public Date getDob() {
        return dob;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", age=" + age +
                ", gender=" + gender +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", dob=" + dob +
                '}';
    }
}
