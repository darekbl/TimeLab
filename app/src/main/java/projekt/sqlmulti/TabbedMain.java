package projekt.sqlmulti;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

// ---------------------- //
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
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

import java.net.HttpURLConnection;
import java.net.URL;
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

// ----------------------------//

public class TabbedMain extends AppCompatActivity {

    // ----------- Variables ----------- //

    // Variables
    private static String SERVER_IP;
    private static String IP;
    private static String QUERY;

    static long UserID;

    // Timer
    static private long startTime = 0L;
    static private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    // Bottom toolbar bootom QUERYs
    private static final String MANUAL_STOP = "%7B'Mode':'photo','Control':'stop','Direction':'right','Speed':100,'Shutter':'external','ProcessTime':2000,'Time':[500,1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],'Hold':false,'HighSpeedMode':false,'ReturnMode':false%7D";
    private static final String GO_LEFT = "%7B'Mode':'film','Control':'run','Direction':'left','Speed':100,'Shutter':'external','ProcessTime':2000,'Time':[100,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],'Hold':false,'HighSpeedMode':false,'ReturnMode':false%7D";
    private static final String GO_RIGHT = "%7B'Mode':'film','Control':'run','Direction':'right','Speed':100,'Shutter':'external','ProcessTime':2000,'Time':[100,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],'Hold':false,'HighSpeedMode':false,'ReturnMode':false%7D";

    // LOG
    private static final String LOG = MainActivity.class.getName();

    // Database Helper
    static DatabaseHelper db;
    static TextView tvMainSummary, tvMainUserInfo, tvMainProfileInfo, tvResponse, tvMain;

    private Button btnGoChoose, btnActivate, btnRequest;

    private boolean isHoldOn;
    static boolean timerStart, timerRunning, timerStarted, timerStopped;


    static private Photo_model pm;
    static private Video_model vm;

    //static String UserName;
    //static int UserCount;

    static long ActivID;
    static String ActivType;

    static GetByJsoup GetIt;

    static Toolbar toolbarBottom, toolbarUser;

    // Toolbar toolbar;
    private long back_pressed;

    static Intent startChooseActivity, TabbedMain;




    // ----------- Variables ----------- //

    // Handle - press twice times to exit app.
    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()) {
            super.onBackPressed();
            System.exit(0);
        } else {
            Toast.makeText(getBaseContext(),
                    "Press once again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_main);

        Toolbar toolbarUser = (Toolbar) findViewById(R.id.user_toolbar);
        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        toolbarUser.inflateMenu(R.menu.user_toolbar);
        toolbarBottom.inflateMenu(R.menu.five_buttons_menu);
        setSupportActionBar(toolbarUser);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String UserName = "";
        int UserCount = 0;

        try {
            UserID = ((MyApplication) getApplication()).getUser();
            UserName = db.getUser(UserID).getName();
            UserCount = db.getUsersCount();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG, "error with getting user info: " + e.getMessage() + "\nUserID: " + UserID + "\nUserName: " + UserName + "\nUserCount: " + UserCount);
        }


        //final RequestQueue queue = Volley.newRequestQueue(this);

        if(timerStart){
            if(!timerRunning){
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
                timerStarted = true;
            }
        }
        else{
            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);
            timerStopped = true;
            timerRunning = false;
        }


        GetIt = new GetByJsoup();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // -- Intents -- //
        startChooseActivity = new Intent(TabbedMain.this,
                ChooseMode.class);

        TabbedMain = new Intent(TabbedMain.this,
                TabbedMain.class);
        // ## Intents ## //

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        //toolbarUser = (Toolbar) findViewById(R.id.toolbar_user);


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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // -------- ADDITIONAL -------- //

        try {
            ActivID = ((MyApplication) getApplication()).getActivatedId();
            ActivType = ((MyApplication) getApplication()).getActivatedType();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG, e.getMessage());
        }

        // Check server's ip and prepare string
        buildIP();

        db = new DatabaseHelper(getApplicationContext());

        // ---------- ADDITIONAL --------- //


        //
        // initToolbars();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_toolbar, menu);
//
//        if(((MyApplication) getApplication()).getActivatedId()>0)
//            menu.findItem(R.id.item_Hold).setVisible(true);
//        else
//            menu.findItem(R.id.item_Hold).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

//        toolbarUser.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user_logout:

                Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_SHORT).show();
                ((MyApplication) getApplication()).setUser(0);

                // Create object of SharedPreferences.
                SharedPreferences sharedPref = getSharedPreferences("RememberUser", 0);
                //now get Editor
                SharedPreferences.Editor editor = sharedPref.edit();
                //put your value
                editor.putLong("UserID", 0);
                //commits your edits
                editor.commit();

                Intent Login = new Intent(TabbedMain.this, LoginActivity.class);
                startActivity(Login);
                TabbedMain.this.finish();
                break;
            case R.id.action_user_settings:
                Intent settings = new Intent(TabbedMain.this, SettingsActivity.class);
                startActivity(settings);
                break;
            case R.id.action_export:
                Toast.makeText(getApplicationContext(), "Export action pressed!", Toast.LENGTH_SHORT).show();
                break;
        }
   //     return true;

        //});
        // Inflate a menu to be displayed in the toolbar
//        toolbarUser.inflateMenu(R.menu.user_toolbar);



        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            // --- //

            Button btnGoChoose = (Button) rootView.findViewById(R.id.btnGotoChoose);
            Button btnActivate = (Button) rootView.findViewById(R.id.btnActivate);
            Button btnRequest = (Button) rootView.findViewById(R.id.btnRequest);

            tvResponse = (TextView) rootView.findViewById(R.id.tvResponse);
            tvMainSummary = (TextView) rootView.findViewById(R.id.tvMainSummary);
            tvMainUserInfo = (TextView) rootView.findViewById(R.id.tvMainUserInfo);
            tvMainProfileInfo = (TextView) rootView.findViewById(R.id.tvMainProfileInfo);
            //tvMain = (TextView) rootView.findViewById(R.id.tvMain);


//        btnRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                startTime = SystemClock.uptimeMillis();
//                customHandler.postDelayed(updateTimerThread, 0);
//
//                GetIt.execute(SERVER_IP + QUERY);
//            }
//        });


        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                QUERY = ((MyApplication) getApplication()).getJSONUrl();
//                Activate();
                startActivity(TabbedMain);
            }
        });

            // ----------------------------------------------- //
            int SECTION = getArguments().getInt(ARG_SECTION_NUMBER);
            Toast.makeText(getContext(),"SEKCJA: " + SECTION,Toast.LENGTH_SHORT).show();
            Log.i(LOG, "NUMER SEKCJI: " + SECTION);
            String UserName = "";

            // ____________________SWITCH___________________ //
            switch(SECTION){
                case 1:
                        timerStart = false;
                        tvMainSummary.setVisibility(View.VISIBLE);
                        textView.setText(getString(R.string.section_format, SECTION));
                        Toast.makeText(getContext(),"timerStart: " + String.valueOf(timerStart),Toast.LENGTH_SHORT).show();
                        Log.i(LOG, "NUMER SEKCJI: " + 1);
                    try {
                        if (UserID > 0)
                            if(IP.equals("0"))
                                Log.e(LOG,"IP's WRONG!");
                                //Toast.makeText(getBaseContext(),"Set up the server's IP.",Toast.LENGTH_SHORT);
                            else
                                UserName = db.getUser(UserID).getName();
                                tvMainSummary.setText("User name: " + UserName + "\nServer's IP: " + IP );
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        tvMain.setText("Error: " + e.getMessage());
                    }
                        break;
                case 2:
                        Log.i(LOG, "NUMER SEKCJI: " + 2);
                        tvResponse.setVisibility(View.VISIBLE);
                        textView.setText(getString(R.string.section_format, SECTION) + "CZAS START!");
                        timerStart = true;
                        Toast.makeText(getContext(),"timerStart: " + String.valueOf(timerStart),Toast.LENGTH_SHORT).show();
                        if(timerStarted)
                            timerRunning = true;
                        break;
                case 3:
                        Log.i(LOG, "NUMER SEKCJI: " + 3);
                        tvMainProfileInfo.setVisibility(View.VISIBLE);
                        timerStart = false;
                        textView.setText(getString(R.string.section_format, SECTION));
                        Toast.makeText(getContext(),"timerStart: " + String.valueOf(timerStart),Toast.LENGTH_SHORT).show();
                        try{
                            if(ActivType.equals("photo")){
                                pm = db.getPhoto(ActivID,UserID);
                                tvMainProfileInfo.setText(
                                        pm.getSummaryString() + "\nTableName: " + db.getArray(pm.getTimes_id(),UserID).getName() + "\nTimes table: " + db.getArray(pm.getTimes_id(),UserID).getTimesString());
                            }
                            else
                            if (ActivType.equals("video")){
                                vm = db.getVideo(ActivID,UserID);
                                tvMainProfileInfo.setText(vm.getSummaryString());
                            }
                        }
                        catch(Exception e)
                        {
                            tvMainProfileInfo.setText("No profile choosed.");
                            e.printStackTrace();
                            Log.e(LOG,"ERRORS: " + e.getMessage());
                        }

                        btnGoChoose.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               startActivity(startChooseActivity);
                                                           }
                                                       }
                        );
                        break;
                    }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "General";
                case 1:
                    return "Response";
                case 2:
                    return "Profile";
            }
            return null;
        }
    }

    // ------- New Functions ------- //

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
            Log.e(LOG, result);
            //tvEncoded.setText(result);
        }
    }


    private class GetByJsoup extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String fullResponse = "";
            try {
                Document doc = Jsoup.connect("http://atmega32.republika.pl/").get();
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
                if(timerStart)
                {
                    //GetIt.execute(SERVER_IP + QUERY);
                    new GetByJsoup().execute(SERVER_IP + QUERY);
                    Log.i(LOG, "Checking parametres");
                }
                startTime = SystemClock.uptimeMillis();
            }
            //Log.e(LOG,"" + mins + ":"
            //               + String.format("%02d", secs) + ":"
            //              + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };


    // Toolbars initiate

    private void initToolbars() {

        // Inflate a menu to be displayed in the toolbar
        //toolbarBottom.getContentInsetStartWithNavigation();
        //toolbarBottom.inflateMenu(R.menu.five_buttons_menu);
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
        }
        catch(Exception e)
        {
            tvMainSummary.setText("At first - choose profile..");
            e.printStackTrace();
            Log.e(LOG, e.getMessage());
        }
    }

    // --------- New Functions -------- //
}
