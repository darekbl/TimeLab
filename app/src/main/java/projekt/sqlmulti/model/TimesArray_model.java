package projekt.sqlmulti.model;

import java.lang.reflect.Array;

/**
 * Created by Darek on 2016-12-03.
 */

public class TimesArray_model {
    int id;
    int uid;
    String name;
    String created_at;
    int[] times_array;
    //int tim1,tim2,tim3,tim4,tim5,tim6,tim7,tim8,tim9,tim10,tim11,tim12,tim13,tim14,tim15,tim16,tim17,tim18,tim19,tim20;

    // constructors
    public TimesArray_model() {
    }

    public TimesArray_model(long uid, String name, int[] times_array) {
        this.uid = (int) uid;
        this.name = name;
        this.times_array = times_array;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUId(int uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimes_array(int[] times_array) {
        this.times_array = times_array;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public long getUId() {
        return this.uid;
    }

    public String getName() {
        return this.name;
    }

    public int[] getTimes_array() {
        return times_array;
    }

    public int getTimeFromArray(int nr) {
        int time = times_array[nr];
        return time;
    }

    public String getTimesString() {
        String times = "";
        int times_length = this.times_array.length;
        for (int i = 0; i < times_length; i++) {
            if (i == 0)
                times = Integer.toString(times_array[0]);
            else
                times = times + "," + Integer.toString(times_array[i]);
        }
        return times;
    }

    public String getSummaryString() {
        String times = "";
        int times_length = this.times_array.length;
        for (int i = 0; i < times_length; i++) {
            if (i == 0)
                times = Integer.toString(times_array[0]);
            else
                times = times + "\n" + Integer.toString(times_array[i]);
        }
        return "Name: " + name +
                "\nTimes: \n" + times +
                "\nCreated at: " + created_at;
    }
}
