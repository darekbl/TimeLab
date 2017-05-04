package projekt.sqlmulti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;

import projekt.sqlmulti.helper.DatabaseHelper;
import projekt.sqlmulti.model.TimesArray_model;

public class AddTimesArray extends AppCompatActivity {

    EditText eTim1,eTim2,eTim3,eTim4,eTim5,eTim6,eTim7,eTim8,eTim9,eTim10,eTNameOfTimes;
    Button btnAddArray;
    boolean isTimesFilled;
    int[] NewTimesArray = {0,0,0,0,0,0,0,0,0,0};
    DatabaseHelper db;
    int checkINT;
    int editedId;
    String command;
    TimesArray_model newArray;
    TimesArray_model editedArray;

    private static final String LOG = AddTimesArray.class.getName();

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("command", command);
        savedInstanceState.putInt("editedId", editedId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_times_array);

        db = new DatabaseHelper(getApplicationContext());

        final long UserID = ((MyApplication) getApplication()).getUser();

        eTim1 = (EditText) findViewById(R.id.eTim1);
        eTim2 = (EditText) findViewById(R.id.eTim2);
        eTim3 = (EditText) findViewById(R.id.eTim3);
        eTim4 = (EditText) findViewById(R.id.eTim4);
        eTim5 = (EditText) findViewById(R.id.eTim5);
        eTim6 = (EditText) findViewById(R.id.eTim6);
        eTim7 = (EditText) findViewById(R.id.eTim7);
        eTim8 = (EditText) findViewById(R.id.eTim8);
        eTim9 = (EditText) findViewById(R.id.eTim9);
        eTim10 = (EditText) findViewById(R.id.eTim10);

        eTNameOfTimes = (EditText) findViewById(R.id.eTNameOfTimes);

        btnAddArray = (Button) findViewById(R.id.btnAddArray);


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

        if(command.equals("edit")) {
            if (editedId > 0){
                try {
                    editedArray = db.getArray((long) editedId, UserID);
                    eTim1.setText(String.valueOf(editedArray.getTimeFromArray(0)));
                    eTim2.setText(String.valueOf(editedArray.getTimeFromArray(1)));
                    eTim3.setText(String.valueOf(editedArray.getTimeFromArray(2)));
                    eTim4.setText(String.valueOf(editedArray.getTimeFromArray(3)));
                    eTim5.setText(String.valueOf(editedArray.getTimeFromArray(4)));
                    eTim6.setText(String.valueOf(editedArray.getTimeFromArray(5)));
                    eTim7.setText(String.valueOf(editedArray.getTimeFromArray(6)));
                    eTim8.setText(String.valueOf(editedArray.getTimeFromArray(7)));
                    eTim9.setText(String.valueOf(editedArray.getTimeFromArray(8)));
                    eTim10.setText(String.valueOf(editedArray.getTimeFromArray(9)));
                    eTNameOfTimes.setText(editedArray.getName());
                    eTNameOfTimes.setFocusable(false);
                }catch(Exception e){
                    e.printStackTrace();
                    Log.e(LOG,"ERRORS: " + e.getMessage());
                }
            }
            else
                Log.e(LOG,"No profile choosed... WTF??");
        }

        btnAddArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            if(     eTim1.getText().toString().equal("") ||
//                    eTim2.getText()!=null ||
//                    eTim3.getText()!=null ||
//                    eTim4.getText()!=null ||
//                    eTim5.getText()!=null ||
//                    eTim6.getText()!=null ||
//                    eTim7.getText()!=null ||
//                    eTim8.getText()!=null ||
//                    eTim9.getText()!=null ||
//                    eTim10.getText()!=null ||
//                    eTim11.getText()!=null ||
//                    eTim12.getText()!=null ||
//                    eTim13.getText()!=null ||
//                    eTim14.getText()!=null ||
//                    eTim15.getText()!=null ||
//                    eTim16.getText()!=null ||
//                    eTim17.getText()!=null ||
//                    eTim18.getText()!=null ||
//                    eTim19.getText()!=null ||
//                    eTim20.getText()!=null
//                    )
//            {
                // Let's pack it to array!
                try{
                NewTimesArray[0] = checkIfNull(eTim1.getText().toString());
                NewTimesArray[1] = checkIfNull(eTim2.getText().toString());
                NewTimesArray[2] = checkIfNull(eTim3.getText().toString());
                NewTimesArray[3] = checkIfNull(eTim4.getText().toString());
                NewTimesArray[4] = checkIfNull(eTim5.getText().toString());
                NewTimesArray[5] = checkIfNull(eTim6.getText().toString());
                NewTimesArray[6] = checkIfNull(eTim7.getText().toString());
                NewTimesArray[7] = checkIfNull(eTim8.getText().toString());
                NewTimesArray[8] = checkIfNull(eTim9.getText().toString());
                NewTimesArray[9] = checkIfNull(eTim10.getText().toString());

                    isTimesFilled = checkArray(NewTimesArray);
                    Log.i(LOG, "Czy poprawne czasy? -> " + isTimesFilled);

                    String name = eTNameOfTimes.getText().toString();

                if(!isTimesFilled)
                    Toast.makeText(getApplicationContext(), "Max. time's value is 999999 ms", Toast.LENGTH_LONG).show();
                    else
                if(name.isEmpty() || name.equals(""))
                    Toast.makeText(getApplicationContext(), "Please, fill the name's field.", Toast.LENGTH_LONG).show();
                    else
                {
                    if (command.equals("create")) {
                        newArray = new TimesArray_model(UserID, eTNameOfTimes.getText().toString(), NewTimesArray);
                        long idOfNewArray = db.createTimes(newArray);
                        Log.i(LOG, "Database got it!.");
                        // int countArrays = db.getArraysCount();
                        Toast.makeText(getApplicationContext(), "Succesfully created", Toast.LENGTH_SHORT).show();
                        AddPhotoProfile.dataAdapter.add(newArray.getName());
                        AddPhotoProfile.dataAdapter.notifyDataSetChanged();
                        ArrayManagement.adapterTimes.add(newArray.getName());
                        ArrayManagement.adapterTimes.notifyDataSetChanged();
                    } else if (command.equals("edit")) {
                        editedArray.setId(editedId);
                        editedArray.setTimes_array(NewTimesArray);
                        int affectedRows = db.updateArray(editedArray, UserID);
                        Log.e(LOG, "Edytowana tablica: " + editedArray.getSummaryString());
                        Toast.makeText(getApplicationContext(), "Succesfully edited. // " + affectedRows, Toast.LENGTH_SHORT).show();
                        Log.i(LOG, "Edited times's array ID: " + editedId);
                    }
                    db.closeDB();
                }
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    Log.e(LOG,"Error in trying to add or edit:\n" + e.getMessage());
                }
//            }
//                else
//                Toast.makeText(getApplicationContext(), "Fill some poles to add new array!", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Check if string is null => write 0 ***  " Anti-null writer "
    int checkIfNull(String checkingText){
        int number = 0;

        if(checkingText.isEmpty() || checkingText.equals(null))
        return number;
        else {
            try {
                number = Integer.parseInt(checkingText);
                if(number>0)
                    return number;
                else
                    return 0;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return number;
    }

    boolean checkArray(int [] array){
        for(int i=0;i<array.length;i++){
            if(array[i]>999999) {
                return false;
            }
        }
        return true;
    }
}