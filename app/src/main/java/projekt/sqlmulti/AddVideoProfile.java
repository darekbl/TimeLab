package projekt.sqlmulti;

import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import projekt.sqlmulti.helper.DatabaseHelper;
import projekt.sqlmulti.model.Video_model;

import static java.lang.Boolean.TRUE;

public class AddVideoProfile extends AppCompatActivity {

    EditText etAddVideoSpeedProfile, etAddVideoNameProfile;
    Button btnAgreeAddVideoActivity;
    SwitchCompat swDirection, swShutter, swReturnMode, swHighSpeed;
    boolean direction,returnMode, highSpeed, shutter;
    DatabaseHelper db;

    int editedId;
    String command;
    Video_model editedVM;

    private static final String LOG = AddVideoProfile.class.getName();
    public static final int max_speed = 100;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("command", command);
        savedInstanceState.putInt("editedId", editedId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_profile);

        final long UserID = ((MyApplication) getApplication()).getUser();

        etAddVideoNameProfile = (EditText) findViewById(R.id.etAddVideoNameProfile);
        etAddVideoSpeedProfile = (EditText) findViewById(R.id.etAddVideoSpeedProfile);
        btnAgreeAddVideoActivity = (Button) findViewById(R.id.btnAgreeAddVideoActivity);

        swDirection = (SwitchCompat) findViewById(R.id.tbDirection);
        //swShutter = (SwitchCompat) findViewById(R.id.swShutter);
        swReturnMode = (SwitchCompat) findViewById(R.id.swReturnMode);
        swHighSpeed = (SwitchCompat) findViewById(R.id.swHighSpeed);

        db = new DatabaseHelper(getApplicationContext());

        // Catch sended variables from PhotoActivity (other Intent)
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                command = "nothing";
                editedId = 0;
            } else {
                command = extras.getString("command");
                editedId = extras.getInt("edited_id");
            }
        } else {
            command = savedInstanceState.getString("command");
            editedId = savedInstanceState.getInt("editedId");
            //command = (String) savedInstanceState.getSerializable("command");
            //editedId = (int) savedInstanceState.getSerializable("edited_id");
        }

        //****** If is from EDIT intent *****//
        if(command.equals("edit")) {
         try{
            if (editedId > 0)
                editedVM = db.getVideo((long) editedId, UserID);
            etAddVideoNameProfile.setText(editedVM.getName());
            //Disable to change the profil's name
            etAddVideoNameProfile.setFocusable(false);
            etAddVideoSpeedProfile.setText(Integer.toString(editedVM.getSpeed()));
            //Toast.makeText(getApplicationContext(), "Times id:" + editedPM.getTimes_id(), Toast.LENGTH_SHORT).show();

            if (editedVM.getDirection())
                swDirection.setChecked(true);
            else
                swDirection.setChecked(false);

            if (editedVM.getReturnMode())
                swReturnMode.setChecked(true);
            else
                swReturnMode.setChecked(false);

            if (editedVM.getHighSpeed())
                swHighSpeed.setChecked(true);
            else
                swHighSpeed.setChecked(false);

//            if (editedVM.getShutter())
//                swShutter.setChecked(true);
//            else
//                swShutter.setChecked(false);
        }
            catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"ERROR\n" + e.getMessage(), Toast.LENGTH_LONG);
            }
        }

        swDirection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    direction = true;
                } else {
                    direction = false;
                }
                Log.e(LOG,"Direction: " + String.valueOf(direction));
            }
        });

//        swShutter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    shutter = true;
//                } else {
//                    shutter = false;
//                }
//                Log.e(LOG,"Shutter: " + String.valueOf(shutter));
//            }
//        });

        swReturnMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    returnMode = true;
                } else {
                    returnMode = false;
                }
                Log.e(LOG,"ReturnMode: " + String.valueOf(returnMode));
            }
        });

        swHighSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    highSpeed = true;
                } else {
                    highSpeed = false;
                }
                Log.e(LOG,"HighSpeed: " + String.valueOf(highSpeed));
            }
        });


        btnAgreeAddVideoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String name = etAddVideoNameProfile.getText().toString();
                    int speed = 0;

                    String speedString = etAddVideoSpeedProfile.getText().toString();
                    if(speedString.isEmpty() || name.equals(""))
                        speed = 0;
                    else
                        speed = Integer.parseInt(speedString);

                    if (name.isEmpty() || name.equals("")) {
                        Toast.makeText(getApplicationContext(), "Wrong profile name", Toast.LENGTH_LONG).show();
                    } else {
                        if (speed <= 0 || speed > max_speed)
                            Toast.makeText(getApplicationContext(), "Wrong speed value. Must be >0 and maximum: " + max_speed, Toast.LENGTH_LONG).show();
                        else {
                            // Create new db video profile
                            Video_model Vmodel = new Video_model();
                            Vmodel.setName(name);
                            Vmodel.setSpeed(speed);
                            Vmodel.setUId((int) UserID);
                            Vmodel.setDirection(direction);
                            //Vmodel.setShutter(shutter);
                            Vmodel.setReturnMode(returnMode);
                            Vmodel.setHighSpeedMode(highSpeed);
                            //Toast.makeText(getApplicationContext(), Vmodel.getSummaryString(),Toast.LENGTH_LONG).show();
                            Log.i(LOG, Vmodel.getSummaryString());
                                    //Toast.makeText(getApplicationContext(), "Command: " + command, Toast.LENGTH_LONG).show();

                            if (command.equals("create")) {
                                // Send object parametres to database (newPhotoProfile)
                                long vid_id = db.createVid(Vmodel);
                                Log.i(LOG, "New photo's profile ID: " + vid_id);
                                Toast.makeText(getApplicationContext(), "Profile \"" + Vmodel.getName() + "\" created!", Toast.LENGTH_LONG).show();
                                VideoActivity.adapterVideo.add(Vmodel.getName());
                                VideoActivity.adapterVideo.notifyDataSetChanged();

                                if (vid_id != 0)
                                    Toast.makeText(getApplicationContext(), "Successful! Profile id:" + vid_id, Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Error.." + vid_id, Toast.LENGTH_LONG).show();
                            } else if (command.equals("edit")) {
                                Vmodel.setId(editedId);
                                int affectedRows = db.updateVideo(Vmodel, UserID);
                                Toast.makeText(getApplicationContext(), "Succesfully edited. // " + affectedRows, Toast.LENGTH_SHORT).show();
                                Log.i(LOG, "Edited photo's profile ID: " + editedId);
                                AddVideoProfile.this.finish();
                            }
                            // ** Important ** - > close Database
                            db.closeDB();
                        }

                    }
                }
            catch (NumberFormatException Ne){
                Ne.printStackTrace();
                Toast.makeText(getApplicationContext(), Ne.getMessage(), Toast.LENGTH_LONG).show();
            }
            }
        });
    }
}
