package projekt.sqlmulti;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import projekt.sqlmulti.helper.DatabaseHelper;
import projekt.sqlmulti.model.Photo_model;

public class PhotoActivity extends AppCompatActivity {

    private ListView photoList;
    DatabaseHelper db;
    static ArrayAdapter<String> adapterPhoto;
    int howManyProfiles;
    static List<String> PhotoProfiles = new ArrayList<String>();

    private static final String LOG = PhotoActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        final long UserID = ((MyApplication) getApplication()).getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        photoList = (ListView) findViewById(R.id.photoList);
        db = new DatabaseHelper(getApplicationContext());


        if (db.GetPhotoProfilesNotes(UserID) != null) {
            PhotoProfiles = db.GetPhotoProfilesNotes(UserID);
        } else Log.e(LOG, "NO PROFILES HERE");

        howManyProfiles = db.getPhotoCount();
        adapterPhoto = new ArrayAdapter<String>(this, R.layout.row, R.id.row, PhotoProfiles);

        photoList.setAdapter(adapterPhoto);

        photoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String Tag = parent.getAdapter().getItem(position).toString();
                //Toast.makeText(getBaseContext(),"Tag -> \"" + Tag + "\"\n" + id + "<- ID\nProfiles's count: " + howManyProfiles, Toast.LENGTH_LONG).show();

                Intent PopupActivity = new Intent(PhotoActivity.this, PopupPhoto.class);
                PopupActivity.putExtra("Profile_id", db.getPhotoIdByName(Tag, UserID));
                startActivity(PopupActivity);
            }
        });


        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Add new photo's profile ", Snackbar.LENGTH_INDEFINITE)
//                        .setAction("Action", null).show();
                Intent startAddPhotoProfile = new Intent(PhotoActivity.this,
                        AddPhotoProfile.class);
                startAddPhotoProfile.putExtra("command", "create");
                startActivity(startAddPhotoProfile);
            }
        });

        FloatingActionButton fab_times = (FloatingActionButton) findViewById(R.id.fab_times);
        fab_times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Times Manage ", Snackbar.LENGTH_INDEFINITE)
//                        .setAction("Action", null).show();
                Log.i(LOG,"FLOATING FAB_TIMES ONCLICK!");
                Intent startTimesManage = new Intent(PhotoActivity.this,
                        ArrayManagement.class);
                startActivity(startTimesManage);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.user_toolbar, menu);
//        return true;
//    }
}
