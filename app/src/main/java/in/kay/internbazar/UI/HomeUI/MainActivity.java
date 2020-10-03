package in.kay.internbazar.UI.HomeUI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.fxn.BubbleTabBar;
import com.fxn.OnBubbleClickListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import in.kay.internbazar.R;

public class MainActivity extends AppCompatActivity {
    BubbleTabBar bubbleTabBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bubbleTabBar=findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Home()).commit();
        bottomMenu();

    }

    private void bottomMenu() {
        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.btm_home:
                        fragment = new Home();
                        break;
                    case R.id.btm_profile:
                        fragment = new Profile();
                        break;
                    case R.id.btm_notification:
                        fragment = new Notification();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}