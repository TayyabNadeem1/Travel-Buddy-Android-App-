package com.example.travel_buddy;

import android.graphics.Bitmap;

public class User {
    private int userid;
    private String Name;
    private String Email;
    private String Password;
    private String Source;
    private String Destination;
    private String Bio;
    private Bitmap pfp; // Bitmap for the profile image

    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String source, String destination, String bio, Bitmap pfp) {
        this.Name = name;

        this.Source = source;
        this.Destination = destination;
        this.Bio = bio;
        this.pfp = pfp;
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

    public User(int userid, String email, String password) {
        this.userid = userid;
        this.Email = email;
        this.Password = password;

    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        this.Bio = bio;
    }

//
//    public int getId() {
//        //return id;
//    }

    public Bitmap getPfp() {
        return pfp;
    }

    public void setPfp(Bitmap pfp) {
        this.pfp = pfp;
    }

    public void setId(int id) {
        //this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

//    public String getPassword() {
//      //  return password;
//    }

//    public void setPassword(String password) {
//       // this.password = password;
//    }

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

//    public int getLikes() {
//    // return likes;
//    }
//
//    public void setLikes(int likes) {
//      //  this.likes = likes;
//    }
//
//
//}
}