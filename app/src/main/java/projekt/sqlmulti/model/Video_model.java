package projekt.sqlmulti.model;

import android.util.Log;

/**
 * Created by Darek on 2016-12-02.
 */

public class Video_model {

    int id;
    int uid;
    String name;
    int speed;
    boolean shutter;
    boolean direction;
    boolean hold;
    boolean high_speed_mode;
    boolean return_mode;
    String created_at;

    // constructors
    public Video_model() {
    }

    public Video_model(int uid, String name, int speed, boolean shutter, boolean direction) {
        this.uid = uid;
        this.name = name;
        this.speed = speed;
        this.shutter = shutter;
        this.direction = direction;
    }


    // setters
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
        Log.e("VID MODEL", "int shutter: " + int_shutter + "bool shutter: " + String.valueOf(shutter));

    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public void setHoldByInt(int int_hold){
        switch(int_hold) {
            case 0:
            {
                this.hold = false;
                break;
            }
            case 1: {
                this.hold = true;
                break;
            }
        }
    }

    public void setHighSpeedMode(boolean high_speed_mode) {
        this.high_speed_mode = high_speed_mode;
    }

    public void setHighSpeedModeByInt(int int_hsm){
        switch(int_hsm) {
            case 0:
            {
                this.high_speed_mode = false;
                break;
            }
            case 1:
            {
                this.high_speed_mode = true;
                break;
            }
        }
        Log.e("VID MODEL", "int high speed: " + int_hsm + "bool hsm: " + String.valueOf(high_speed_mode));

    }

    public void setReturnMode(boolean return_mode) {
        this.return_mode = return_mode;
    }

    public void setReturnModeByInt(int int_rm){
        switch(int_rm) {
            case 0:
            {
                return_mode = false;
                break;
            }
            case 1:
            {
                return_mode = true;
                break;
            }
        }
        Log.e("VID MODEL", "int returnmode: " + int_rm + "bool rm: " + String.valueOf(return_mode));
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public void setDirectionByInt(int int_direction){
        switch(int_direction) {
            case 0:
            {
                direction = false;
                break;
            }
            case 1:
            {
                direction = true;
                break;
            }
        }
        Log.e("VID MODEL", "int direction: " + int_direction + "bool direction: " + String.valueOf(direction));
    }



    public void setCreatedAt(String created_at){
        this.created_at = created_at;
    }

    // getters
    public String getCreatedAt() { return this.created_at; }

    public long getId() {
        return this.id;
    }

    public long getUId() { return this.uid; }

    public String getName() {
        return this.name;
    }

    public int getSpeed() {
        return this.speed;
    }

    public boolean getShutter() {
        return this.shutter;
    }

    public int getShutterINT() { return shutter ? 1 : 0; }

    public boolean getDirection() {
        return this.direction;
    }

    public int getDirectionINT() { return direction ? 1 : 0; }

    public boolean getHighSpeed() {
        return this.high_speed_mode;
    }

    public int getHighSpeedINT() { return high_speed_mode ? 1 : 0; }

    public boolean getReturnMode() {
        return this.return_mode;
    }

    public int getReturnModeINT() { return return_mode ? 1 : 0; }

    public String getDirectionAsString() {
        if(direction)
            return "right";
        else
            return "left";

    }

    public String getShutterAsString() {
        if(shutter)
            return "external";
        else
            return "internal";

    }

    public String getSummaryString(){
        return "Name: " + this.name +
                "\nSpeed: " + this.speed +
                "\nDirection: " + this.getDirectionAsString() +
                "\nShutter: " + this.getShutterAsString() +
                //"\nProcess Time: " + this.process_time +
                //"\nTimes ID: " + this.times_id +
                //"\nHold Mode: " + Boolean.toString(hold) +
                "\nHigh Speed Mode: " + Boolean.toString(high_speed_mode) +
                "\nReturn Mode: " + Boolean.toString(return_mode) +
                "\nUser ID: " + this.uid +
                "\nCreated at: " + this.created_at;
    }
}
