package com.example.travel_buddy;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable {
    private int userid;
    private String Name;
    private String Email;
    private String Password;
    private String Source;
    private String Destination;
    private String Gender;
    private String Age;
    private String DOB;
    private String About;
    private String Phone;
    private String Bio;
    private Bitmap pfp; // Bitmap for the profile image
    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String source, String destination, String bio, Bitmap pfp, String phone) {
        this.Name = name;
        this.Phone = phone;
        this.Source = source;
        this.Destination = destination;
        this.Bio = bio;
        this.pfp = pfp;
    }

    public User(int userid, String email, String password) {
        this.userid = userid;
        this.Email = email;
        this.Password = password;

    }
    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        this.Bio = bio;
    }
    public Bitmap getPfp() {
        return pfp;
    }

    public void setPfp(Bitmap pfp) {
        this.pfp = pfp;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        this.Source = source;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        this.Destination = destination;
    }
    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

}