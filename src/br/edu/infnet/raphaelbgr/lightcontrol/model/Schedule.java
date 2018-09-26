
package br.edu.infnet.raphaelbgr.lightcontrol.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {

    @SerializedName("mon")
    @Expose
    private List<Mon> mon = new ArrayList<Mon>();
    @SerializedName("tue")
    @Expose
    private List<Tue> tue = new ArrayList<Tue>();
    @SerializedName("wed")
    @Expose
    private List<Wed> wed = new ArrayList<Wed>();
    @SerializedName("thu")
    @Expose
    private List<Thu> thu = new ArrayList<Thu>();
    @SerializedName("fri")
    @Expose
    private List<Fri> fri = new ArrayList<Fri>();
    @SerializedName("sat")
    @Expose
    private List<Sat> sat = new ArrayList<Sat>();
    @SerializedName("sun")
    @Expose
    private List<Sun> sun = new ArrayList<Sun>();

    public List<Mon> getMon() {
        return mon;
    }

    public void setMon(List<Mon> mon) {
        this.mon = mon;
    }

    public Schedule withMon(List<Mon> mon) {
        this.mon = mon;
        return this;
    }

    public List<Tue> getTue() {
        return tue;
    }

    public void setTue(List<Tue> tue) {
        this.tue = tue;
    }

    public Schedule withTue(List<Tue> tue) {
        this.tue = tue;
        return this;
    }

    public List<Wed> getWed() {
        return wed;
    }

    public void setWed(List<Wed> wed) {
        this.wed = wed;
    }

    public Schedule withWed(List<Wed> wed) {
        this.wed = wed;
        return this;
    }

    public List<Thu> getThu() {
        return thu;
    }

    public void setThu(List<Thu> thu) {
        this.thu = thu;
    }

    public Schedule withThu(List<Thu> thu) {
        this.thu = thu;
        return this;
    }

    public List<Fri> getFri() {
        return fri;
    }

    public void setFri(List<Fri> fri) {
        this.fri = fri;
    }

    public Schedule withFri(List<Fri> fri) {
        this.fri = fri;
        return this;
    }

    public List<Sat> getSat() {
        return sat;
    }

    public void setSat(List<Sat> sat) {
        this.sat = sat;
    }

    public Schedule withSat(List<Sat> sat) {
        this.sat = sat;
        return this;
    }

    public List<Sun> getSun() {
        return sun;
    }

    public void setSun(List<Sun> sun) {
        this.sun = sun;
    }

    public Schedule withSun(List<Sun> sun) {
        this.sun = sun;
        return this;
    }

}
