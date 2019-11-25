
package br.edu.infnet.raphaelbgr.lightcontrol.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wed {

    @SerializedName("set")
    @Expose
    private Integer set;
    @SerializedName("time")
    @Expose
    private String time;

    public Integer getSet() {
        return set;
    }

    public void setSet(Integer set) {
        this.set = set;
    }

    public Wed withSet(Integer set) {
        this.set = set;
        return this;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Wed withTime(String time) {
        this.time = time;
        return this;
    }

}
