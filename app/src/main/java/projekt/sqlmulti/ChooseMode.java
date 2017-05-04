package projekt.sqlmulti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ChooseMode extends AppCompatActivity {

    ImageView imgBtnVideo;
    ImageView imgBtnPhoto;
    ImageView imgBtnTimes;
    static boolean VideoMode = false;
    static boolean PhotoMode = false;
     Button btnLogTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);

        imgBtnPhoto = (ImageView) findViewById(R.id.imgBtnPhoto);
        imgBtnVideo = (ImageView) findViewById(R.id.imgBtnVideo);
        imgBtnTimes = (ImageView) findViewById(R.id.imgBtnTimes);


        imgBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startActivityPhoto = new Intent(ChooseMode.this,
                        PhotoActivity.class);
                startActivity(startActivityPhoto);
                PhotoMode = TRUE;
                VideoMode = FALSE;
            }
        });

        imgBtnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startActivityVideo = new Intent(ChooseMode.this,
                        VideoActivity.class);
                startActivity(startActivityVideo);
                PhotoMode = FALSE;
                VideoMode = TRUE;
            }
        });

        imgBtnTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startActivityVideo = new Intent(ChooseMode.this,
                        ArrayManagement.class);
                startActivity(startActivityVideo);
            }
        });

    }
}
