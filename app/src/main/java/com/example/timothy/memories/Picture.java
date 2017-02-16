package com.example.timothy.memories;

/**
 * Created by Timothy on 12/12/2016.
 */

public class Picture {
    // private variables
    int id;
    String name;
    byte[] image;
    String location;


    public Picture() {

    }

    public Picture(int keyId, String name, String locations, byte[] image) {
        this.id = keyId;
        this.name = name;
        this.image = image;
        this.location = locations;

    }
    public Picture(String name, String locations, byte[] image) {
        this.name = name;
        this.image = image;
        this.location = locations;

    }
    public Picture(int keyId) {
        this.id = keyId;

    }

    public int getID() {
        return this.id;
    }


    public void setID(int keyId) {
        this.id = keyId;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String locations) {
        this.location = locations;
    }


    public byte[] getImage() {
        return this.image;
    }


    public void setImage(byte[] image) {
        this.image = image;
    }
}
