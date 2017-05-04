package projekt.sqlmulti.model;

import android.util.Log;

import projekt.sqlmulti.helper.DatabaseHelper;

/**
 * Created by Darek on 2016-12-02.
 */

public class Photo_model {

    int id;
    int uid;
    String name;
    int speed;
    boolean shutter;
    boolean direction;
    boolean hold;
    boolean high_speed_mode;
    boolean return_mode;
    int process_time;
    int times_id;

    String created_at;

    // constructors
    public Photo_model() {}

    public Photo_model(String name) {
        this.name = name;
    }

    public Photo_model(int id, int uid, String name, int speed, boolean direction, int process_time, int times_id) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.speed = speed;
        this.direction = direction;
        this.process_time = process_time;
        this.times_id = times_id;
    }

    // setter
    public void setId(int id) {
        this.id = id;
    }

    public void setUId(int uid) { this.uid = uid; }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setShutter(boolean shutter) {
        this.shutter = shutter;
    }

    public void setShutterByInt(int int_shutter){
        switch(int_shutter) {
            case 0:
            {
                this.shutter = false;
                break;
            }
            case 1:
            {
                this.shutter = true;
                break;
            }
        }
        Log.e("PHOTO MODEL", "int shutter: " + int_shutter + " bool shutter: " + String.valueOf(shutter));

    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public void setHoldByInt(int int_hold){
        switch(int_hold) {
            case 0:
                this.hold = false;
            case 1:
                this.hold = true;
        }
    }

    public void setHighSpeedMode(boolean high_speed_mode) {
        this.high_speed_mode = high_speed_mode;
    }

    public void setHighSpeedModeByInt(int int_hsm){
        switch(int_hsm) {
            case 0:
                this.high_speed_mode = false;
            case 1:
                this.high_speed_mode = true;
        }
    }

    public void setReturnMode(boolean return_mode) {
        this.return_mode = return_mode;
    }

    public void setReturnModeByInt(int int_rm){
        switch(int_rm) {
            case 0:{
                this.return_mode = false;
                break;
            }
            case 1: {
                this.return_mode = true;
                break;
            }
        }
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public void setDirectionByInt(int int_direction){
        switch(int_direction) {
            case 0:{
                this.direction = false;
                break;
            }
            case 1: {
                this.direction = true;
                break;
            }
        }
    }



    public void setProcess_time(int process_time) {
        this.process_time = process_time;
    }

    public void setTimes_id(int times_id) {
        this.times_id = times_id;
    }

    public void setCreatedAt(String created_at) { this.created_at = created_at; }

    // getter
    public String getCreatedAt() { return this.created_at; }

    public int getId() {
        return this.id;
    }

    public int getUId() {
        return this.uid;
    }

    public String getName() {
        return this.name;
    }

    public boolean getDirection() {
        return this.direction;
    }

    public int getDirectionINT() { return direction ? 1 : 0; }

    public String getDirectionAsString() {
        if(this.direction)
            return "right";
        else
            return "left";

    }


    public boolean getHold() {
        return this.hold;
    }

    public int getHoldINT() { return hold ? 1 : 0; }

    public String getHoldAsString() {
        if(this.hold)
            return "true";
        else
            return "false";

    }

    public boolean getHighSpeedMode() {
        return this.high_speed_mode;
    }

    public int getHighSpeedINT() { return high_speed_mode ? 1 : 0; }

    public String getHighSpeedModeAsString() {
        if(this.high_speed_mode)
            return "true";
        else
            return "false";

    }

    public boolean getReturnMode() {
        return this.return_mode;
    }

    public int getReturnINT() { return return_mode ? 1 : 0; }

    public int getShutterINT() { return shutter ? 1 : 0; }

    public String getReturnModeAsString() {
        if(this.return_mode)
            return "true";
        else
            return "false";
    }

    public String getShutterAsString() {
        if(shutter)
            return "external";
        else
            return "internal";

    }

    public boolean getShutter() {
        return this.shutter;
    }

    public int getTimes_id() {
        return this.times_id;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getProcess_time() {
        return this.process_time;
    }

    public String getSummaryString(){
        return "Name: " + this.name +
                "\nSpeed: " + this.speed +
                "\nDirection: " + this.getDirectionAsString() +
                "\nProcess Time: " + this.process_time +
                "\nTimes ID: " + this.times_id +
                "\nShutter: " + this.getShutterAsString() +
                //"\nHold Mode: " + Boolean.toString(hold) +
                //"\nHigh Speed Mode: " + Boolean.toString(high_speed_mode) +
                "\nReturn Mode: " + Boolean.toString(return_mode) +
                "\nUser ID: " + this.uid +
                "\nCreated at: " + this.created_at;
    }
}