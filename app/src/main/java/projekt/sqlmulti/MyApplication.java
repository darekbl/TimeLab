package projekt.sqlmulti;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import projekt.sqlmulti.helper.DatabaseHelper;
import projekt.sqlmulti.model.Photo_model;
import projekt.sqlmulti.model.Video_model;

/**
 * Created by Darek on 2016-12-17.
 */
public class MyApplication extends Application {

    DatabaseHelper db;
    private int ActivatedId;
    private String ActivatedType;
    private long UserID;
    private String Command;
    private String JSONUrl;

    public int getActivatedId() {
        return ActivatedId;
    }
    public String getActivatedType() { return ActivatedType; }

    public void setActivatedId(int ActivatedId) {
        this.ActivatedId = ActivatedId;
    }
    public void setActivatedType(String ActivatedType) {
        if(ActivatedType.equals("video") || ActivatedType.equals("photo"))
        this.ActivatedType = ActivatedType;
        else{
            Log.e("MY APP", "TYP: " + ActivatedType);
            Toast.makeText(getApplicationContext(),"Błąd! Błędny typ profilu do aktywacji!",Toast.LENGTH_LONG).show();
        }
    }

    public String getCommand(){
        return Command;
    }

    public void setCommand(String Command){
        this.Command = Command;
    }

    public void setJSONUrl(Photo_model pm){
        db = new DatabaseHelper(getApplicationContext());
        String timesString = db.getArray(pm.getTimes_id(),UserID).getTimesString();
        this.JSONUrl = "%7B" +
                "'Mode':'photo'," +
                "'Control':'run'," +
                "'Direction':'" + pm.getDirectionAsString() +
                "','Speed':'" + pm.getSpeed() +
                "','Shutter':'" + pm.getShutterAsString() +
                "','ProcessTime':'" + pm.getProcess_time() +
                "','Time':[" + timesString +
                "],'Hold':false," +
                "'HighSpeedMode':false," +
                "'ReturnMode':" + pm.getReturnModeAsString() +
                "%7D";
        db.closeDB();
    }

    public void setJSONUrl(Video_model vm){
        this.JSONUrl = "%7B" +
                "'Mode':'film'," +
                "'Control':'run'," +
                "'Direction':'" + vm.getDirectionAsString() +
                "','Speed':'" + vm.getSpeed() +
                "','Shutter':'" + vm.getShutterAsString() +
                "','Shutter':'external'," +
                "'ProcessTime':'" + 0 +
                "','Time':[0,0,0,0,0,0,0,0,0,0]" +
                ",'Hold':false," +
                "'HighSpeedMode':" + String.valueOf(vm.getHighSpeed()) +
                ",'ReturnMode':" + String.valueOf(vm.getReturnMode()) +
                "%7D";
    }

    public String getJSONUrl(){
        return JSONUrl;
    }

    public void setUser(long userID)
    {
        this.UserID = userID;
    }

    public long getUser()
    {
        return this.UserID;
    }
}