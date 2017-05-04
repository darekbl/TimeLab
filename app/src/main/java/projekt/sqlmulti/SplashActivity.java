package projekt.sqlmulti;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView SplashText;
    private static final int CZAS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView SplashText = (TextView) findViewById(R.id.fullscreen_content);

        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(3000);
        //animation1.setStartOffset(5000);
        animation1.setFillAfter(true);
        SplashText.startAnimation(animation1);


        // Uruchom wątek otwierający główną aktywność
        ActivityStarter starter = new ActivityStarter();
        starter.start();
        // Refer the ImageView like this
        //imageView = (ImageView) findViewById(R.id.img);

// Load the animation like this
        //Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
        //        R.anim.slide);

// Start the animation like this
        //imageView.startAnimation(animSlide);
    }

    private class ActivityStarter extends Thread {

        long UserID;
        SharedPreferences sharedPref= getSharedPreferences("RememberUser", 0);
        @Override
        public void run() {
            try {
                UserID = sharedPref.getLong("UserID", 0);
                // tutaj wrzucamy wszystkie akcje potrzebne podczas ładowania aplikacji
                Log.i("SPLASH", "Zapamietany uzytkownik: " + UserID);
                Thread.sleep(CZAS);
            } catch (Exception e) {
                Log.e("SplashScreen", e.getMessage());
            }

            Intent intent;
            // Włącz główną aktywność
            if(UserID==0)
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
                ((MyApplication) getApplication()).setUser(UserID);
            }

            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
    }

}

