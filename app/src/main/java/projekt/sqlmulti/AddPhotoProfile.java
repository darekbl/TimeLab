package projekt.sqlmulti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ThemedSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import projekt.sqlmulti.helper.DatabaseHelper;
import projekt.sqlmulti.model.Photo_model;
import projekt.sqlmulti.model.TimesArray_model;


public class AddPhotoProfile extends AppCompatActivity {


    EditText etAddPhotoNameProfile,etAddPhotoSpeedProfile,etAddPhotoProcTimeProfile;
    SwitchCompat swDirection, swReturn, swShutter;
    Button btnAgreeAddPhotoActivity,btnAddNewArray;
    Spinner spinTimesArray;

    DatabaseHelper db;

    int tim_id, editedId, HowManyArrays=0;

    boolean direction, hold, return_mode, high_speed, shutter;

    String command;

    Photo_model editedPM;
    static ArrayAdapter<String> dataAdapter;

    // Constants
    public static final int max_speed = 100000;   // FRAMES AMOUNT
    public static final int max_proctime = 1000000;
    private static final String LOG = AddPhotoProfile.class.getName();

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("command", command);
        savedInstanceState.putInt("editedId", editedId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_profile);

        final long UserID = ((MyApplication) getApplication()).getUser();



        // EditTexts
        etAddPhotoNameProfile = (EditText) findViewById(R.id.etAddPhotoNameProfile);
        etAddPhotoSpeedProfile = (EditText) findViewById(R.id.etAddPhotoSpeedProfile);
        etAddPhotoProcTimeProfile = (EditText) findViewById(R.id.etAddPhotoProcTimeProfile);

        // Toggle buttons
        swDirection = (SwitchCompat) findViewById(R.id.swPhotoDirection);
        swShutter = (SwitchCompat) findViewById(R.id.swShutter);
        swReturn = (SwitchCompat) findViewById(R.id.swPhotoReturnMode);

        // Buttons
        btnAgreeAddPhotoActivity = (Button) findViewById(R.id.btnAgreeAddPhotoActivity);
        btnAddNewArray =  (Button) findViewById(R.id.btnAddNewArray);

        spinTimesArray = (Spinner) findViewById(R.id.spinTimesArray);

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

        //Toast.makeText(getApplicationContext(),"Which action? - " + command + "\nEditingID: " + editedId,Toast.LENGTH_LONG).show();

            // Create List
            List<String> Arrays = new ArrayList<String>();
            // Getting List<String> (names) of arrays
            Arrays = db.GetTimesArraysNotes(UserID);

            // Creating adapter for spinner
            dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arrays);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinTimesArray.setAdapter(dataAdapter);


        //****** If is from EDIT intent *****//
        if(command.equals("edit")) {
            if(editedId > 0)
                editedPM = db.getPhoto((long)editedId,UserID);
            etAddPhotoNameProfile.setText(editedPM.getName());
            //Disable to change the profil's name
            etAddPhotoNameProfile.setFocusable(false);
            etAddPhotoSpeedProfile.setText(Integer.toString(editedPM.getSpeed()));
            etAddPhotoProcTimeProfile.setText(Integer.toString(editedPM.getProcess_time()));
            //Toast.makeText(getApplicationContext(), "Times id:" + editedPM.getTimes_id(), Toast.LENGTH_SHORT).show();

            if(editedPM.getDirection())
                swDirection.setChecked(true);
            else
                swDirection.setChecked(false);

            if(editedPM.getReturnMode())
                swReturn.setChecked(true);
            else
                swReturn.setChecked(false);

            if (editedPM.getShutter())
                swShutter.setChecked(true);
            else
                swShutter.setChecked(false);

            String name = "bezimienny";
            try
            {
                name = db.getArrayNameByID((long)editedPM.getTimes_id(),UserID);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"ERROR\n" + e.getMessage(), Toast.LENGTH_LONG);
            }

            Toast.makeText(getApplicationContext(),"Name of Array :" + name, Toast.LENGTH_LONG);
            spinTimesArray.setSelection(dataAdapter.getPosition(name));
            dataAdapter.setNotifyOnChange(true);
        }

            spinTimesArray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    // On selecting a spinner item
                    String item = adapterView.getItemAtPosition(position).toString();

                    //Position of choosed array of times (usage when object is preparing)

                    tim_id = db.getArrayIDByName(item, UserID);

                    // Showing selected spinner item
                    //Toast.makeText(adapterView.getContext(), "Selected: " + item + "\n Id: " + tim_id, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        swDirection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    direction = true;
                } else {
                    direction = false;
                }
            }
        });

        swShutter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shutter = true;
                } else {
                    shutter = false;
                }
                Log.e(LOG,"Shutter: " + String.valueOf(shutter));
            }
        });

        swReturn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    return_mode = true;
                } else {
                    return_mode = false;
                }
            }
        });


            // Action after agree -> read input parametres and let's add a new profile!
            btnAgreeAddPhotoActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //New Profile algorithm.
                    try {

                        // Read and prepare input variables
                        int speed = 0;
                        String name = etAddPhotoNameProfile.getText().toString();

                        String speedString = etAddPhotoSpeedProfile.getText().toString();
                        if(speedString.isEmpty() || name.equals(""))
                                //Toast.makeText(getApplicationContext(), "Fill frames's field", Toast.LENGTH_SHORT).show();
                                speed = 0;
                            else
                        speed = Integer.parseInt(speedString);

                        int process_time = 0;
                        String proctimeString = etAddPhotoProcTimeProfile.getText().toString();
                        if(proctimeString.isEmpty() || proctimeString.equals(""))
                            process_time = 0;
                            else
                                process_time = Integer.parseInt(proctimeString);

                        int times_id = tim_id;

                        // Conditions
                        if (name.isEmpty() || name.equals(""))
                            Toast.makeText(getApplicationContext(), "Wrong profile name", Toast.LENGTH_LONG).show();
                        else if (speed > max_speed || speed <= 0)
                            Toast.makeText(getApplicationContext(), "Wrong frames value. Min. frames >0 and max: " + max_speed, Toast.LENGTH_LONG).show();
                        else if (process_time > max_proctime || process_time <= 0)
                            Toast.makeText(getApplicationContext(), "Wrong process time value. Min. time >0 and max: " + max_proctime, Toast.LENGTH_LONG).show();
                        else {
                            // Create new object
                            Photo_model new_photo_model = new Photo_model();
                            new_photo_model.setUId((int) UserID);
                            new_photo_model.setName(name);
                            new_photo_model.setSpeed(speed);
                            new_photo_model.setShutter(shutter);
                            new_photo_model.setDirection(direction);
                            new_photo_model.setProcess_time(process_time);
                            new_photo_model.setTimes_id(times_id);
                            new_photo_model.setReturnMode(return_mode);
                            Log.i(LOG, new_photo_model.getSummaryString());
                            // Create
                            if (command.equals("create")) {
                                // Send object parametres to database (newPhotoProfile)
                                long photo_id = db.createPhoto(new_photo_model);
                                Log.i(LOG, "New photo's profile ID: " + photo_id);
                                Toast.makeText(getApplicationContext(), "Profile \"" + new_photo_model.getName() + "\" created!", Toast.LENGTH_LONG).show();
                                PhotoActivity.adapterPhoto.add(new_photo_model.getName());
                                PhotoActivity.adapterPhoto.notifyDataSetChanged();
                            }
                            // Edit
                            else if (command.equals("edit")) {
                                new_photo_model.setId(editedId);
                                int affectedRows = db.updatePhoto(new_photo_model, UserID);
                                Toast.makeText(getApplicationContext(), "Succesfully edited.", Toast.LENGTH_SHORT).show();
                                Log.i(LOG, "Edited photo's profile ID: " + editedId + "\n Summary: " + new_photo_model.getSummaryString());
                                AddPhotoProfile.this.finish();
                            }
                            // ** Important ** - > close Database
                            db.closeDB();
                        }
                        }catch(Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                }
            });

        // Add new times array
        btnAddNewArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAddTimesArray = new Intent(AddPhotoProfile.this,
                        AddTimesArray.class);
                startAddTimesArray.putExtra("command","create");
                startActivity(startAddTimesArray);
            }
        });


    }
}
