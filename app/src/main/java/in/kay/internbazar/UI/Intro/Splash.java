package in.kay.internbazar.UI.Intro;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import in.kay.internbazar.R;
import in.kay.internbazar.UI.HomeUI.MainActivity;
import in.kay.internbazar.Utils.Preference;

public class Splash extends AppCompatActivity {
    VideoView videoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        videoView = findViewById(R.id.videoView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
        videoView.setVideoURI(video);
        videoView.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { Boolean isLoggedIn = Preference.getSharedPreferenceBoolean(Splash.this, "isLoggedIn", false);
                if (isLoggedIn) {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                } else {
                    startActivity(new Intent(Splash.this, AuthActivity.class));
                }
                finish();
            }
        }, 4000);
    }
}