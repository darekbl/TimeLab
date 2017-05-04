package projekt.sqlmulti;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Text;

import projekt.sqlmulti.helper.DatabaseHelper;

import static projekt.sqlmulti.ChooseMode.PhotoMode;
import static projekt.sqlmulti.ChooseMode.VideoMode;
import static projekt.sqlmulti.R.layout.row;

public class VideoActivity extends AppCompatActivity {

    DatabaseHelper db;
    private ListView lista_vid ;
    static ArrayAdapter<String> adapterVideo ;

    static List<String> VideoProfiles = new ArrayList<String>();

    private static final String LOG = VideoActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);

        final long UserID = ((MyApplication) getApplication()).getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        lista_vid = (ListView) findViewById(R.id.lista_vid);
        db = new DatabaseHelper(getApplicationContext());


        if (db.GetVideoProfilesNotes(UserID) != null)
            VideoProfiles = db.GetVideoProfilesNotes(UserID);
        else Log.e(LOG, "NO PROFILES HERE");

        adapterVideo = new ArrayAdapter<String>(this,R.layout.row, R.id.row, VideoProfiles);

        lista_vid.setAdapter(adapterVideo);

        Log.i(LOG, "VideoCount: " + db.getVideoCount());

        lista_vid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Tag = parent.getAdapter().getItem(position).toString();

                Intent PopupActivity = new Intent (VideoActivity.this, PopupVideo.class);
                PopupActivity.putExtra("Profile_id",db.getVideoIdByName(Tag, UserID));
                startActivity(PopupActivity);
            }
        });
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Add new video's profile", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                Intent startAddVideoProfile = new Intent(VideoActivity.this,
                        AddVideoProfile.class);
                startAddVideoProfile.putExtra("command","create");
                startActivity(startAddVideoProfile);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
