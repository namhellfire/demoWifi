package com.example.switchwifi;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nguyennam on 11/23/16.
 */

public class ModelWifi {

    private List<String> listSSID;
    private List<Integer> listLevel;
    private HashMap<String, Integer> listWifi;

    public List<String> getListSSID() {
        return listSSID;
    }

    public void setListSSID(List<String> listSSID) {
        this.listSSID = listSSID;
    }

    public List<Integer> getListLever() {
        return listLevel;
    }

    public void setListLever(List<Integer> listLever) {
        this.listLevel = listLever;
    }

    public HashMap<String, Integer> getListWifi() {
        return listWifi;
    }

    public void setListWifi(HashMap<String, Integer> listWifi) {
        this.listWifi = listWifi;
    }
}
