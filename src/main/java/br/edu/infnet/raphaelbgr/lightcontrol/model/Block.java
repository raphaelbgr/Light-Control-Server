
package br.edu.infnet.raphaelbgr.lightcontrol.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Block {

    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("apartments")
    @Expose
    private Integer apartments;
    @SerializedName("floors")
    @Expose
    private List<Floor> floors = new ArrayList<Floor>();

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Block withNumber(Integer number) {
        this.number = number;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Block withName(String name) {
        this.name = name;
        return this;
    }

    public Integer getApartments() {
        return apartments;
    }

    public void setApartments(Integer apartments) {
        this.apartments = apartments;
    }

    public Block withApartments(Integer apartments) {
        this.apartments = apartments;
        return this;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    public Block withFloors(List<Floor> floors) {
        this.floors = floors;
        return this;
    }

}
