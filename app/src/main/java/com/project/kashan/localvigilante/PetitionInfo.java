package com.project.kashan.localvigilante;

import java.util.ArrayList;
import java.util.List;

public class PetitionInfo {
    public static int count = 0;
    public int id;
    public String name;
    public String body;
    public int noSignatures;
    public int user_id;
    public byte[] img;
    public String address;
    public float lat;
    public float lng;

    public PetitionInfo() {
        this.id = count++;
    }

    public static List<PetitionInfo> generateData(){
        ArrayList<PetitionInfo> list = new ArrayList<>();
        PetitionInfo info = new PetitionInfo();

        info.id = count++;
        info.name = "Ban UBER from Bucharest";
        info.body = "We, the taxi drivers from Bucharest, want the UBER banned. " +
                "It's stealing our costumers, and they don't pay income taxes. STOP UBER!";
        info.noSignatures = 0;
        info.user_id = -1;
        info.img = null;
        info.address = "Bucharest";
        info.lat = 0;
        info.lng = 0;

        list.add(info);
        return list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getNoSignatures() {
        return noSignatures;
    }

    public void setNoSignatures(int noSignatures) {
        this.noSignatures = noSignatures;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}
