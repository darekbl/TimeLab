package projekt.sqlmulti;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import projekt.sqlmulti.helper.DatabaseHelper;
import projekt.sqlmulti.model.Photo_model;
import projekt.sqlmulti.model.Video_model;

/**
 * Created by Darek on 2016-12-15.
 */

public class PopupVideo extends AppCompatActivity {


    private static final String LOG = PopupVideo.class.getName();

    private Button btnActivateVideo;
    int id;
    public DatabaseHelper db,dbhelper;
    private TextView tvPopupTitle;
    private Toolbar toolbar;
    //Photo_model pm;
    Video_model vm = new Video_model();

    static long UserID;
    static long ActivatedID;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("id", id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserID = ((MyApplication) getApplication()).getUser();
        ActivatedID = ((MyApplication) getApplication()).getActivatedId();

        setContentView(R.layout.popup_video);
        tvPopupTitle = (TextView) findViewById(R.id.tvPopupTitle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        // Catch sended variables from VideoActivity (other Intent)
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id = 0;
            } else {
                id = extras.getInt("Profile_id");
            }
        } else {
            id = savedInstanceState.getInt("id");
        }

        // Convert to long -> id should be long for objects
        long id_long = Long.valueOf(id);

        try {
            vm = db.getVideo(id_long, UserID);
            // Prepare string for information
//            String parametres =
//                    "Profile Name:" + pmPP.getName() +
//                    "\n Direction: " + pmPP.getDirectionAsString() +
//                    "\n Speed: " + pmPP.getSpeed() +
//                    "\n Process Time: " + pmPP.getProcess_time();

            // Set this informations
            tvPopupTitle.setText(vm.getSummaryString());

            //db.closeDB();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this,"Some problems:" + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        // Prepare Window with information
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // Window width -> 80% && height -> 60%
        getWindow().setLayout((int)(width*.7),(int)(height*.7));

        initToolbars();

        db.closeDB();

    }

    // **** Show bottom toolbar with activate/edit/remove icons and handle them functions
    private void initToolbars() {

        final DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar);
        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.item_activate:
                        try{((MyApplication) getApplication()).setActivatedId(id);
                            Log.i(LOG,"id to activate " + id +
                                    "\nID z MyApp..: " + ((MyApplication) getApplication()).getActivatedId());
                            ((MyApplication) getApplication()).setJSONUrl(vm);
                            ((MyApplication) getApplication()).setActivatedType("video");
//                        Toast.makeText(getApplicationContext(),"Activated profile: " + pmPP.getName() + "\nDirection: " + pmPP.getDirectionAsString() + "\n Speed: " + pmPP.getSpeed() +
//                                "\n Process Time: " + pmPP.getProcess_time() + "\nTimesID: " + pmPP.getTimes_id(),Toast.LENGTH_SHORT).show();

                            Intent Main = new Intent(PopupVideo.this,MainActivity.class);
//                        Main.putExtra("activate_now",true);
                            startActivity(Main);
                            PopupVideo.this.finish();
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.item_edit:
                        Intent EditIntent = new Intent(PopupVideo.this,AddVideoProfile.class);
                        EditIntent.putExtra("command","edit");
                        EditIntent.putExtra("edited_id",id);
                        startActivity(EditIntent);
                        PopupVideo.this.finish();
                        break;
                    case R.id.item_delete:
                        try {
                            AlertDialog.Builder altBx = new AlertDialog.Builder(PopupVideo.this);
                            altBx.setTitle("Deleting..");
                            altBx.setMessage(
                                    "Profile \"" + vm.getName() + "\" will be removed.\n" +
                                            "Are you sure?");
                            altBx.setIcon(R.drawable.customremoveicon);

                            altBx.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // At first delete profil in database
                                    dbhelper.deleteVideoByID(id,(int)UserID);
                                    // Second - from the list ( that's why is that list is static )
                                    VideoActivity.adapterVideo.remove(vm.getName());
                                    VideoActivity.adapterVideo.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(), vm.getName() + " successfully removed.", Toast.LENGTH_SHORT).show();
                                    PopupVideo.this.finish();
                                }
                            });
                            altBx.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i(LOG, "Cancelled.");
                                }

                            });
                            altBx.show();
                        }
                        //catch(IllegalStateException | NullPointerException e){
                        catch(Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error:\n" + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                return true;
            }
        });
        // Inflate a menu to be displayed in the toolbar
        toolbarBottom.inflateMenu(R.menu.popup_toolbar);
        toolbarBottom.getTitleMarginStart();
    }
}