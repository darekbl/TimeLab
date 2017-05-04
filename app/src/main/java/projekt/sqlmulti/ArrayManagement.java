package projekt.sqlmulti;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import projekt.sqlmulti.helper.DatabaseHelper;
import projekt.sqlmulti.model.Photo_model;

public class ArrayManagement extends AppCompatActivity {

    private ListView arrayList;
    DatabaseHelper db;
    int howMany;

    static List<String> Arrays = new ArrayList<String>();
    static ArrayAdapter<String> adapterTimes;

    private static final String LOG = ArrayManagement.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array_management);


            final long UserID = ((MyApplication) getApplication()).getUser();

            arrayList = (ListView) findViewById(R.id.list_times);
            db = new DatabaseHelper(getApplicationContext());

            if (db.GetTimesArraysNotes(UserID) != null) {
                Arrays = db.GetTimesArraysNotes(UserID);
            } else Log.e(LOG, "NO ARRAYS HERE");

            //howMany = db.getArraysCount();
            adapterTimes = new ArrayAdapter<String>(this, R.layout.row, R.id.row, Arrays);

            arrayList.setAdapter(adapterTimes);

            arrayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String Tag = parent.getAdapter().getItem(position).toString();
                    //Toast.makeText(getBaseContext(),"Tag -> \"" + Tag + "\"\n" + id + "<- ID\nProfiles's count: " + howManyProfiles, Toast.LENGTH_LONG).show();

                    Intent PopupActivity = new Intent(ArrayManagement.this, PopupArray.class);
                    PopupActivity.putExtra("Profile_id", db.getArrayIDByName(Tag, UserID));
                    Log.i(LOG,"Choosed Array to popup (ID): " + String.valueOf(db.getArrayIDByName(Tag, UserID)));
                    startActivity(PopupActivity);
                }
            });



            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Snackbar.make(view, "Add new array ", Snackbar.LENGTH_INDEFINITE)
//                        .setAction("Action", null).show();
                    Intent startAddArray = new Intent(ArrayManagement.this,
                            AddTimesArray.class);
                    startAddArray.putExtra("command", "create");
                    startActivity(startAddArray);
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }
