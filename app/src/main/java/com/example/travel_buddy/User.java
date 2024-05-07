package com.example.travel_buddy;

import android.graphics.Bitmap;

public class User {


    String Name;
    String Source;
    String Destination;
    String Bio;

    public Bitmap getPfp() {
        return pfp;
    }

    public void setPfp(Bitmap pfp) {
        this.pfp = pfp;
    }

    private Bitmap pfp; // Bitmap for the profile image


    public User() {
    }



    public User(String name, String source, String destination, String bio, Bitmap profileImage) {
        this.Name = name;
        this.Source = source;
        this.Destination = destination;
        this.Bio = bio;
        this.pfp = profileImage;
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