
package br.edu.infnet.raphaelbgr.lightcontrol.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Payload {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("master_switch_state")
    @Expose
    private boolean masterSwitchState = true;
    @SerializedName("blocks")
    @Expose
    private List<Block> blocks = new ArrayList<Block>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Payload withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Payload withName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Payload withType(String type) {
        this.type = type;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Payload withAddress(Address address) {
        this.address = address;
        return this;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public Payload withBlocks(List<Block> blocks) {
        this.blocks = blocks;
        return this;
    }

    public boolean isMasterSwitchState() {
        return masterSwitchState;
    }

    public void setMasterSwitchState(boolean masterSwitchState) {
        this.masterSwitchState = masterSwitchState;
    }
}
