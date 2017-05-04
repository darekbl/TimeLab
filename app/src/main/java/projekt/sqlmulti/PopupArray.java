package projekt.sqlmulti;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import projekt.sqlmulti.helper.DatabaseHelper;
import projekt.sqlmulti.model.Photo_model;
import projekt.sqlmulti.model.TimesArray_model;

public class PopupArray extends AppCompatActivity {

    private static final String LOG = PopupPhoto.class.getName();

    int id;
    public DatabaseHelper db,dbhelper;
    private TextView tvPopupTitle;
    private Toolbar toolbar;
    TimesArray_model tam = new TimesArray_model();

    static long UserID;
    static long ActivatedID;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("id", id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_array);


            UserID = ((MyApplication) getApplication()).getUser();
            ActivatedID = ((MyApplication) getApplication()).getActivatedId();

            tvPopupTitle = (TextView) findViewById(R.id.tvPopupTitle);
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            DatabaseHelper db = new DatabaseHelper(getApplicationContext());

            // Catch sended variables from PhotoActivity (other Intent)
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
                tam = db.getArray(id_long, UserID);
                // Prepare string for information
//            String parametres =
//                    "Profile Name:" + pmPP.getName() +
//                    "\n Direction: " + pmPP.getDirectionAsString() +
//                    "\n Speed: " + pmPP.getSpeed() +
//                    "\n Process Time: " + pmPP.getProcess_time();

                // Set this informations
                tvPopupTitle.setText(tam.getSummaryString());

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
                    case R.id.item_edit:
                        Log.i(LOG,"Array (ID): " + id);
                        Intent EditIntent = new Intent(PopupArray.this,AddTimesArray.class);
                        EditIntent.putExtra("command","edit");
                        EditIntent.putExtra("edited_id",id);
                        startActivity(EditIntent);
                        PopupArray.this.finish();
                        break;
                    case R.id.item_delete:
                        try {
                            AlertDialog.Builder altBx = new AlertDialog.Builder(PopupArray.this);
                            altBx.setTitle("Deleting..");
                            altBx.setMessage(
                                    "Profile \"" + tam.getName() + "\" will be removed.\n" +
                                            "Are you sure?");
                            altBx.setIcon(R.drawable.customremoveicon);

                            altBx.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // At first delete profil in database
                                    dbhelper.deleteTimesByID(id,(int)UserID);
                                    // Second - from the list ( that's why is that list is static )
                                    ArrayManagement.adapterTimes.remove(tam.getName());
                                    ArrayManagement.adapterTimes.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(), tam.getName() + " successfully removed.", Toast.LENGTH_SHORT).show();
                                    PopupArray.this.finish();
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
        toolbarBottom.inflateMenu(R.menu.popup_toolbar_simple);
        toolbarBottom.getTitleMarginStart();
    }
}

