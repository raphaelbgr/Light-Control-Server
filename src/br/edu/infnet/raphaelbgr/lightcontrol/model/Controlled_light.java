
package br.edu.infnet.raphaelbgr.lightcontrol.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Controlled_light {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("connection")
    @Expose
    private String connection;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("stragegy")
    @Expose
    private String stragegy;
    @SerializedName("schedule")
    @Expose
    private Schedule schedule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Controlled_light withId(String id) {
        this.id = id;
        return this;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Controlled_light withArea(String area) {
        this.area = area;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Controlled_light withType(String type) {
        this.type = type;
        return this;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public Controlled_light withConnection(String connection) {
        this.connection = connection;
        return this;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Controlled_light withState(Integer state) {
        this.state = state;
        return this;
    }

    public String getStragegy() {
        return stragegy;
    }

    public void setStragegy(String stragegy) {
        this.stragegy = stragegy;
    }

    public Controlled_light withStragegy(String stragegy) {
        this.stragegy = stragegy;
        return this;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Controlled_light withSchedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }

}
