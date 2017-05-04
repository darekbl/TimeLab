package projekt.sqlmulti;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import projekt.sqlmulti.helper.DatabaseHelper;
import projekt.sqlmulti.model.Photo_model;
import projekt.sqlmulti.model.User_model;
import projekt.sqlmulti.model.Video_model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.prefs.Preferences;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import android.support.design.widget.CoordinatorLayout;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MainActivity extends AppCompatActivity {

    // Variables
    private static String SERVER_IP;
    private static String IP;
    private static String QUERY;
    long UserID;

    // Timer
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    // Request Codes

    private static final int IMPORT_DATABASE_REQUEST_CODE = 1000;
    private static final int PHOTO_REQUEST_CODE = 100;
    private static final int TIMES_REQUEST_CODE = 101;
    private static final int VIDEO_REQUEST_CODE = 102;


    // Bottom toolbar bootom QUERYs
    private static final String MANUAL_STOP = "%7B'Mode':'photo','Control':'stop','Direction':'right','Speed':100,'Shutter':'external','ProcessTime':2000,'Time':[500,1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],'Hold':false,'HighSpeedMode':false,'ReturnMode':false%7D";
    private static final String GO_LEFT = "%7B'Mode':'film','Control':'run','Direction':'left','Speed':100,'Shutter':'external','ProcessTime':2000,'Time':[100,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],'Hold':false,'HighSpeedMode':false,'ReturnMode':false%7D";
    private static final String GO_RIGHT = "%7B'Mode':'film','Control':'run','Direction':'right','Speed':100,'Shutter':'external','ProcessTime':2000,'Time':[100,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],'Hold':false,'HighSpeedMode':false,'ReturnMode':false%7D";

    // LOG
    private static final String LOG = MainActivity.class.getName();

    // Database Helper
    private DatabaseHelper db;
    static TextView tvMainSummary,tvMainUserInfo,tvMainProfileInfo, tvResponse;

    private Button btnGotoChoose, btnActivate, btnRequest;

    private boolean isHoldOn, Requesting;

    private Photo_model pm;
    private Video_model vm;

    static long ActivID;
    static String ActivType, DBname;

   // Toolbar toolbar;
    private long back_pressed;

    // Handle - press twice times to exit app.
    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()){
            super.onBackPressed();
            System.exit(0);
        }
        else{
            Toast.makeText(getBaseContext(),
                    "Press once again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.five_buttons_menu, menu);
        if(((MyApplication) getApplication()).getActivatedId()>0)
            menu.findItem(R.id.item_Hold).setVisible(true);
        else
            menu.findItem(R.id.item_Hold).setVisible(false);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            ActivID = ((MyApplication) getApplication()).getActivatedId();
            ActivType = ((MyApplication) getApplication()).getActivatedType();
        }
        catch(Exception e){
            e.printStackTrace();
            Log.e(LOG,e.getMessage());
        }

        // Check server's ip and prepare string
        buildIP();

        db = new DatabaseHelper(getApplicationContext());
        DBname = db.getDbName();

        btnGotoChoose = (Button) findViewById(R.id.btnGotoChoose);
        btnActivate = (Button) findViewById(R.id.btnActivate);
        btnRequest = (Button) findViewById(R.id.btnRequest);

        tvResponse = (TextView) findViewById(R.id.tvResponse);
        tvMainSummary = (TextView) findViewById(R.id.tvMainSummary);
        tvMainUserInfo = (TextView) findViewById(R.id.tvMainUserInfo);
        tvMainProfileInfo = (TextView) findViewById(R.id.tvMainProfileInfo);

        // Queue -> for server requesting
        final RequestQueue queue = Volley.newRequestQueue(this);

        String UserName = "";
        int UserCount = 0;

        // Show informations
        try {
            UserName = db.getUser(((MyApplication) getApplication()).getUser()).getName();
            UserCount = db.getUsersCount();
            UserID = ((MyApplication) getApplication()).getUser();
            if (((MyApplication) getApplication()).getUser() > 0)
                if(IP.equals("0"))
                    Toast.makeText(getBaseContext(),"Set up the server's IP.",Toast.LENGTH_SHORT);
                    else
                    tvMainUserInfo.setText("User name: " + UserName +
                            //"\nUser count: " + UserCount +
                            "\nServer's IP: " + IP );
        }
        catch(Exception e){
            e.printStackTrace();
            tvMainUserInfo.setText("Error: " + e.getMessage());
            Toast.makeText(getBaseContext(),"You need to log in.", Toast.LENGTH_SHORT).show();
            Intent logIn = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(logIn);
            MainActivity.this.finish();
        }

        // Show informations about profile
        try{
            if(ActivType.equals("photo")){
                pm = db.getPhoto(ActivID,UserID);
                tvMainProfileInfo.setText(
                        pm.getSummaryString() + "\nTableName: " + db.getArray(pm.getTimes_id(),UserID).getName() + "\nTimes table: " + db.getArray(pm.getTimes_id(),UserID).getTimesString());
                tvMainProfileInfo.setVisibility(TextView.VISIBLE);
            }
            else
            if (ActivType.equals("video")){
                vm = db.getVideo(ActivID,UserID);
                tvMainProfileInfo.setText(vm.getSummaryString());
                tvMainProfileInfo.setVisibility(TextView.VISIBLE);
            }
        }
        catch(Exception e)
        {
            tvMainProfileInfo.setVisibility(TextView.GONE);
            tvMainProfileInfo.setText("No profile choosed.");
            e.printStackTrace();
            Log.e(LOG,"ERRORS: " + e.getMessage());
        }

        // Handle button - choose mode - onClick
        btnGotoChoose.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent startChooseActivity = new Intent(MainActivity.this,
                                                         ChooseMode.class);
                                                 startActivity(startChooseActivity);
                                             }
                                         }
        );

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Requesting) {
                    tvResponse.setVisibility(TextView.VISIBLE);
                    view.setBackgroundResource(R.drawable.btn_active);
                    // Get the present time
                    startTime = SystemClock.uptimeMillis();
                    // Start timer - cycled requesting
                    customHandler.postDelayed(updateTimerThread, 0);
                    // Request for response
//                    int color = ((ColorDrawable)btnRequest.getBackground()).getColor();
                    new GetByJsoup().execute(SERVER_IP + QUERY);
                } else
                {
                    view.setBackgroundResource(R.drawable.simplebutton);
                    tvResponse.setVisibility(TextView.GONE);
                }
                Requesting = !Requesting;
            }
        });


        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Prepare Query for send
                QUERY = ((MyApplication) getApplication()).getJSONUrl();
                // Send => Activate profile
                Activate();
            }
        });

        // Initialize top and bottom toolbar
        initToolbars();

    }

    // Send prepared Query ~~ Response probably doesn't work or need to be better handled
    private class Background_get extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    result.append(inputLine).append("\n");

                in.close();
                connection.disconnect();
                return result.toString();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG,e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }


    // Get the response from server
    private class GetByJsoup extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String fullResponse = "";
            try {
                Document doc = Jsoup.connect(IP).get();
                Elements paragraphs = doc.select("p");

                for (Element p : paragraphs) {
                    fullResponse += "\n" + p.text();
                }
            } catch (IOException | RuntimeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e(LOG,e.getMessage());
            }
            //Log.e(LOG,fullResponse);
            return fullResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            tvResponse.setText(result);
        }
    }

    // Timer
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
                if(timeInMilliseconds>2000){
                    if(Requesting){
                    new GetByJsoup().execute(SERVER_IP + QUERY);
                    startTime = SystemClock.uptimeMillis();
                    Log.i(LOG, "Checking parametres");
                    }
                }
            //Log.e(LOG,"" + mins + ":"
             //               + String.format("%02d", secs) + ":"
              //              + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };

    // Toolbars initialize
    private void initToolbars() {

        final DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar);

        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.item_Hold:
                        isHoldOn = !isHoldOn;
                        if(isHoldOn) {
                            try{
                                if(((MyApplication) getApplication()).getActivatedId()==0)
                                    QUERY = MANUAL_STOP;
                                else
                                    QUERY = ((MyApplication) getApplication()).getJSONUrl();

                                    item.setIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.holdon));
                                    QUERY = QUERY.replace("'Hold':false","'Hold':true");
                                new Background_get().execute(SERVER_IP + QUERY);
                            }
                            catch(Exception e){
                                e.printStackTrace();
                                Log.e(LOG, "PROBLEM: " + e.getMessage());
                            }
                        }
                        else
                            {
                                if(((MyApplication) getApplication()).getActivatedId()==0)
                                    QUERY = MANUAL_STOP;
                                else
                                    QUERY = ((MyApplication) getApplication()).getJSONUrl();
                            item.setIcon(ContextCompat.getDrawable(getBaseContext(),R.drawable.holdoff));
                            QUERY = ((MyApplication) getApplication()).getJSONUrl();
                                new Background_get().execute(SERVER_IP + QUERY);
                            }
                        break;
                    case R.id.item_GotoLeft:
                        new Background_get().execute(SERVER_IP + GO_LEFT);
                        Toast.makeText(getApplicationContext(),"Go left!",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_PlayStop:
                        new Background_get().execute(SERVER_IP + MANUAL_STOP);
                        Toast.makeText(getApplicationContext(),"Stop!",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_GoToRight:
                        new Background_get().execute(SERVER_IP + GO_RIGHT);
                        Toast.makeText(getApplicationContext(),"Go right!",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }


        });
        // Inflate a menu to be displayed in the toolbar
        toolbarBottom.getContentInsetStartWithNavigation();
        toolbarBottom.inflateMenu(R.menu.five_buttons_menu);


        //-------------------
        // User's top toolbar
        //-------------------

        Toolbar toolbarUser = (Toolbar) findViewById(R.id.toolbar_user);
        //toolbar.getPaddingLeft();
        toolbarUser.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_user_logout:
                        Toast.makeText(getApplicationContext(),"Logged out!",Toast.LENGTH_SHORT).show();
                        ((MyApplication) getApplication()).setUser(0);

                        // Create object of SharedPreferences.
                        SharedPreferences sharedPref= getSharedPreferences("RememberUser", 0);
                        //now get Editor
                        SharedPreferences.Editor editor = sharedPref.edit();
                        //put your value
                        editor.putLong("UserID", 0);
                        //commits your edits
                        editor.commit();

                        Intent Login = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(Login);
                        MainActivity.this.finish();
                        break;
                    case R.id.action_user_settings:
                        Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(settings);
                        break;
                    case R.id.action_import:
                        openFile(IMPORT_DATABASE_REQUEST_CODE);
                        break;
                    case R.id.action_export:
                        exportDB();
                        break;
                }
                return true;
            }
        });
        // Inflate a menu to be displayed in the toolbar
        toolbarUser.inflateMenu(R.menu.user_toolbar);

    }

    // Prepare IP
    public void buildIP(){

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        IP = sharedPrefs.getString("server_ip", "192.168.137.1");

        SERVER_IP = "http://" + IP + "/";
        Log.i(LOG, "IP Serwera: " + SERVER_IP);
    }

    // Activating profile
    public void Activate() {

            Log.i(LOG,"AKTYWOWANO (ID):" + ActivID + "TYPE: " + ActivType);

            //Toast.makeText(getApplicationContext(),"ActivatedType: " + QUERY.toUpperCase().contains("photo".toUpperCase()),Toast.LENGTH_LONG).show();
        try {
            if (ActivType.equals("photo")) {
                Photo_model pm = db.getPhoto((long) ActivID, UserID);
            } else if (ActivType.equals("video")) {
                Video_model vm = db.getVideo((long) ActivID, UserID);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
         //Toast.makeText(getApplicationContext(),"JSON's query:\n" + QUERY, Toast.LENGTH_LONG).show();

        try{
            new Background_get().execute(SERVER_IP + QUERY);
            tvMainSummary.setText("Condition: active\n" + "\n\nQUERY: " + QUERY);
            if(QUERY == null)
                tvMainSummary.setText("Select profile before start.");
            tvMainSummary.setVisibility(TextView.VISIBLE);
        }
        catch(Exception e)
        {
            tvMainSummary.setVisibility(TextView.GONE);
            tvMainSummary.setText("At first - choose profile..");
            e.printStackTrace();
            Log.e(LOG, e.getMessage());
        }
    }

    private void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "projekt.sqlmulti" + "/databases/"+DBname;
        String backupDBPath = DBname;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Database exported on SDcard storage ! File name: " + DBname, Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),"Something's wrong.",Toast.LENGTH_SHORT).show();
        }
    }

    private void importDB(String Fpath){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "projekt.sqlmulti" + "/databases/" + DBname;
        String newDBPath = Fpath;
        newDBPath = newDBPath.replace("file:/","");
        //newDBPath = newDBPath.replace(DBname + "Exported",DBname);
        //Toast.makeText(this, "New path: " + newDBPath, Toast.LENGTH_SHORT).show();
        File currentDB = new File(data, currentDBPath);
        File newDB = new File(newDBPath);
        try {
            source = new FileInputStream(newDB).getChannel();
            destination = new FileOutputStream(currentDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Database sucessfully imported ! Log in now.", Toast.LENGTH_LONG).show();

            // Create object of SharedPreferences.
            SharedPreferences sharedPref= getSharedPreferences("RememberUser", 0);
            //now get Editor
            SharedPreferences.Editor editor = sharedPref.edit();
            //put your value
            editor.putLong("UserID", 0);
            //commits your edits
            editor.commit();

            Intent login = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(login);

            MainActivity.this.finish();
        } catch(IOException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),"Something's wrong.\n" + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    public void exportPhoto(ArrayList<Photo_model> listProfiles) throws IOException {
        //Create FileOutputStream to write file
        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/exportedPhotoProfiles");
        //Create ObjectOutputStream to write object
        ObjectOutputStream objOutputStream = new ObjectOutputStream(fos);
        //Write object to file
        for (Object obj : listProfiles) {

            objOutputStream.writeObject(obj);
            objOutputStream.reset();
        }
        objOutputStream.close();
    }

    public ArrayList<Photo_model> importPhoto(String path) throws ClassNotFoundException, IOException {
        ArrayList<Photo_model> listAccount = new ArrayList();
        //Create new FileInputStream object to read file
        FileInputStream fis = new FileInputStream(path);
        //Create new ObjectInputStream object to read object from file
        ObjectInputStream obj = new ObjectInputStream(fis);
        try {
            while (fis.available() != -1) {
                //Read object from file
                Photo_model pprof = (Photo_model) obj.readObject();
                listAccount.add(pprof);
            }
        } catch (EOFException ex) {
            //ex.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return listAccount;
    }

    private void openFile(int CODE) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i, CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    try {
        String Fpath = data.getDataString();
        Log.e(LOG, "FILE: " + Fpath);
        //Toast.makeText(getBaseContext(), Fpath, Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);
            //if(Fpath.contains("Photo")){
                if(requestCode == IMPORT_DATABASE_REQUEST_CODE){
                try {
                    //importPhoto(Fpath);
//                    boolean result = db.importDatabase(Fpath);
//                    Toast.makeText(this, "Importing...\nResult: " + result, Toast.LENGTH_SHORT).show();
//                    Log.e(LOG, "Importing...\nResult: " + result);
                    importDB(Fpath);
                    Toast.makeText(this, Fpath, Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            else
                Toast.makeText(this, "Wrong file name.", Toast.LENGTH_SHORT).show();
        }
        catch(NullPointerException e){
            Toast.makeText(this, "No file choosed.", Toast.LENGTH_SHORT).show();
        }
    }
}