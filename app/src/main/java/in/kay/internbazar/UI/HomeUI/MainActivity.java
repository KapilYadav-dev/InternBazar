package in.kay.internbazar.UI.HomeUI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.fxn.BubbleTabBar;
import com.fxn.OnBubbleClickListener;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import in.kay.internbazar.Api.RetrofitClient;
import in.kay.internbazar.R;
import in.kay.internbazar.Utils.Preference;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    BubbleTabBar bubbleTabBar;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean isFirstTime = Preference.getSharedPreferenceBoolean(this, "isFirstTime", true);
        if (isFirstTime) {
            EditProfile();
            Preference.setSharedPreferenceBoolean(this, "isFirstTime", false);
        }
        setContentView(R.layout.activity_main);
        bubbleTabBar = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
        bottomMenu();

    }

    private void EditProfile() {
        final String uid, token;
        uid = Preference.getSharedPreferenceString(this, "uid", "");
        token = Preference.getSharedPreferenceString(this, "token", "");
        dialog = new Dialog(this);
        final EditText etName, etEmail, etMobile, etAboutme, etLocation, etEducation, etSkills, etJobs, etLinks, etAdditional;
        ImageView close;
        Button submit;
        dialog.setContentView(R.layout.profile_diag);
        etAboutme = dialog.findViewById(R.id.etAboutme);
        etName = dialog.findViewById(R.id.etName);
        etEmail = dialog.findViewById(R.id.etEmail);
        etName.setText(Preference.getSharedPreferenceString(this, "name", ""));
        etEmail.setText(Preference.getSharedPreferenceString(this, "email", ""));
        etLocation = dialog.findViewById(R.id.etlocation);
        etEducation = dialog.findViewById(R.id.etEducation);
        etSkills = dialog.findViewById(R.id.etSkills);
        etJobs = dialog.findViewById(R.id.etJob);
        etLinks = dialog.findViewById(R.id.etLinks);
        etAdditional = dialog.findViewById(R.id.etAdditional);
        etMobile = dialog.findViewById(R.id.etContact);
        submit = dialog.findViewById(R.id.btn_submit);
        close = dialog.findViewById(R.id.close);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        close.setVisibility(View.GONE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String location = etLocation.getText().toString();
                String aboutme = etAboutme.getText().toString();
                String name = etName.getText().toString();
                String education = etEducation.getText().toString();
                String skills = etSkills.getText().toString();
                String jobs = etJobs.getText().toString();
                String links = etLinks.getText().toString();
                String additional = etAdditional.getText().toString();
                String mobile = etMobile.getText().toString();
                if (name.isEmpty()) {
                    etName.setError("Name cannot be empty");
                    etName.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    etEmail.setError("Email cannot be empty");
                    etEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Please enter a valid email id");
                    etEmail.requestFocus();
                    return;
                }
                if (mobile.isEmpty()) {
                    etMobile.setError("Mobile cannot be empty");
                    etMobile.requestFocus();
                    return;
                }
                if (aboutme.isEmpty()) {
                    etAboutme.setError("About me cannot be empty");
                    etAboutme.requestFocus();
                    return;
                }
                if (location.isEmpty()) {
                    etLocation.setError("Location cannot be empty");
                    etLocation.requestFocus();
                    return;
                }
                if (education.isEmpty()) {
                    etEducation.setError("Education details cannot be empty");
                    etEducation.requestFocus();
                    return;
                }
                if (skills.isEmpty()) {
                    etSkills.setError("Skills cannot be empty (Type atleast one)");
                    etSkills.requestFocus();
                    return;
                } else {
                    DoWork(aboutme, additional, education, jobs, name, skills, links, location, mobile, uid, token);
                }


            }
        });
    }

    private void DoWork(String aboutme, String additional, String education, String jobs, String name, String skills, String links, String location, String mobile, final String uid, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", uid);
        jsonObject.addProperty("userType", "student");
        JsonObject data = new JsonObject();
        data.addProperty("about", aboutme);
        data.addProperty("education", education);
        data.addProperty("phone", mobile);
        data.addProperty("location", location);
        data.addProperty("name", name);
        data.addProperty("skills", skills);
        data.addProperty("links", links);
        data.addProperty("jobs", jobs);
        data.addProperty("additional", additional);
        jsonObject.add("data", data);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().edit(jsonObject, "Bearer " + token);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMax(100);
        pd.setMessage("Saving");
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String string;
                try {
                    if (response.code() == 200) {
                        TastyToast.makeText(getBaseContext(), "Successfully updated profile. ", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        CreateResume(uid);
                    } else {
                        string = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(string);
                        String errormsg = jsonObject.getString("message");
                        TastyToast.makeText(getBaseContext(), "Error : " + errormsg, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    TastyToast.makeText(getBaseContext(), "Error : " + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }
                dialog.dismiss();
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TastyToast.makeText(getBaseContext(), "Error : " + t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                pd.dismiss();
                dialog.dismiss();

            }
        });
    }

    private void CreateResume(String uid) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().createResume(uid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this, "Response Code is "+response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void bottomMenu() {
        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.btm_home:
                        fragment = new Home();
                        break;
                    case R.id.btm_profile:
                        fragment = new Profile();
                        break;
                    case R.id.btm_notification:
                        fragment = new Applications();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new iOSDialogBuilder(this)
                .setTitle("Exit")
                .setSubtitle("Ohh no! You're leaving...\nAre you sure?")
                .setCancelable(false)
                .setPositiveListener(getString(R.string.ok), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                        CloseApp();
                    }
                })
                .setNegativeListener(getString(R.string.dismiss), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

    private void CloseApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }
}