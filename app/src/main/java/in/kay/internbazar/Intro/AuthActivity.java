package in.kay.internbazar.Intro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import in.kay.internbazar.R;
import in.kay.internbazar.UI.MainActivity;
import in.kay.internbazar.Utils.Preference;

public class AuthActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Boolean isLoggedIn=Preference.getSharedPreferenceBoolean(this,"isLoggedIn",false);
        if (isLoggedIn)
        {
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            Fragment mFragment = null;
            mFragment = new Login();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mFragment).commit();
        }

    }
}