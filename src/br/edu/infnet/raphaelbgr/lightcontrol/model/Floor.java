
package br.edu.infnet.raphaelbgr.lightcontrol.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Floor {

    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("controlled_lights")
    @Expose
    private List<ControlledLight> controlled_lights = new ArrayList<ControlledLight>();

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Floor withNumber(Integer number) {
        this.number = number;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Floor withName(String name) {
        this.name = name;
        return this;
    }

    public List<ControlledLight> getControlledLights() {
        return controlled_lights;
    }

    public void setControlled_lights(List<ControlledLight> controlled_lights) {
        this.controlled_lights = controlled_lights;
    }

    public Floor withControlled_lights(List<ControlledLight> controlled_lights) {
        this.controlled_lights = controlled_lights;
        return this;
    }

}
