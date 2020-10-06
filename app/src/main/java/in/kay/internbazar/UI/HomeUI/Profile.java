package in.kay.internbazar.UI.HomeUI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import in.kay.internbazar.Api.RetrofitClient;
import in.kay.internbazar.R;
import in.kay.internbazar.UI.Intro.AuthActivity;
import in.kay.internbazar.Utils.Preference;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends Fragment {
    Context mcontext;
    View view;
    RelativeLayout rlLogout, rlHelp, rlProfile, rlResume, rlPassword;
    Dialog dialog, changeDiag;
    ImageView img;
    TextView tvname, tvemail;
    String uid, token;
    String baseUrl = "https://ui-avatars.com/api/?background=234&color=fff&size=256&rounded=true&name=";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        String name = Preference.getSharedPreferenceString(mcontext, "name", "");
        String email = Preference.getSharedPreferenceString(mcontext, "email", "");
        uid = Preference.getSharedPreferenceString(mcontext, "uid", "");
        token = Preference.getSharedPreferenceString(mcontext, "token", "");
        rlLogout = view.findViewById(R.id.rllogout);
        rlProfile = view.findViewById(R.id.rlProfile);
        rlPassword = view.findViewById(R.id.rl_change_password);
        rlResume = view.findViewById(R.id.rl_resume);
        img = view.findViewById(R.id.iv);
        tvemail = view.findViewById(R.id.email);
        tvname = view.findViewById(R.id.name);
        tvname.setText(name);
        tvemail.setText(email);
        String url = baseUrl + name;
        Picasso.get().load(url).into(img);
        rlPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlPassword.setClickable(false);
                ChangePasswordLogic();
            }
        });
        rlResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = RetrofitClient.getInstance().getApi().view(uid, "student", "Bearer " + token);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String string;
                        try {
                            if (response.code() == 200) {
                                string = response.body().string();
                                JSONObject jsonObject = new JSONObject(string);
                                JSONObject child = jsonObject.getJSONObject("user");
                                String resume = child.getString("resume");
                                String url = "http://192.168.1.7:8080/" + resume;
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                            } else {
                                string = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(string);
                                String errormsg = jsonObject.getString("message");
                                TastyToast.makeText(mcontext, "Error :" + errormsg, TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            TastyToast.makeText(mcontext, "Error :" + e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        TastyToast.makeText(mcontext, "Error :" + t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        dialog.dismiss();
                    }
                });
            }
        });
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new iOSDialogBuilder(mcontext)
                        .setTitle(getString(R.string.logout))
                        .setSubtitle(getString(R.string.logout_msg))
                        .setFont(Typeface.createFromAsset(getContext().getAssets(), "sans_regular.ttf"))
                        .setCancelable(false)
                        .setPositiveListener(getString(R.string.ok), new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                                String PREF_NAME = "PREF";
                                SharedPreferences sharedPrefs = mcontext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(mcontext, AuthActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
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
        });
        rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlProfile.setClickable(false);
                ProfileDiag();
            }
        });
    }

    private void ChangePasswordLogic() {
        changeDiag = new Dialog(mcontext);
        final EditText etOldPassword, etNewPassword, etConfirmPassword;
        Button submit;
        changeDiag.setContentView(R.layout.change_password_diag);
        submit = changeDiag.findViewById(R.id.btn_submit);
        etOldPassword = changeDiag.findViewById(R.id.etOldPassword);
        etNewPassword = changeDiag.findViewById(R.id.etNewPassword);
        etConfirmPassword = changeDiag.findViewById(R.id.etConfirmPassword);
        changeDiag.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changeDiag.setCanceledOnTouchOutside(false);
        changeDiag.setCancelable(false);
        changeDiag.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strOld, strNew, strConfirm;
                strOld = etOldPassword.getText().toString();
                strNew = etNewPassword.getText().toString();
                strConfirm = etConfirmPassword.getText().toString();
                if (strOld.isEmpty()) {
                    etOldPassword.setError("Mobile cannot be empty");
                    etOldPassword.requestFocus();
                    return;
                }
                if (strNew.isEmpty()) {
                    etOldPassword.setError("Mobile cannot be empty");
                    etOldPassword.requestFocus();
                    return;
                }
                if (strConfirm.isEmpty()) {
                    etOldPassword.setError("Mobile cannot be empty");
                    etOldPassword.requestFocus();
                    return;
                }
                if (strOld.length() < 6) {
                    etOldPassword.setError("Please check your old password");
                    etOldPassword.requestFocus();
                    return;
                }
                if (strNew.length() < 6) {
                    etNewPassword.setError("New Password should atleast 6 char long");
                    etNewPassword.requestFocus();
                    return;
                }
                if (strConfirm.length() < 6) {
                    etConfirmPassword.setError("New Password should atleast 6 char long");
                    etConfirmPassword.requestFocus();
                    return;
                }
                if (!strNew.equals(strConfirm)) {
                    etConfirmPassword.setError("Passwords do not match");
                    etNewPassword.setError("Passwords do not match");
                    etConfirmPassword.requestFocus();
                    etNewPassword.requestFocus();
                    return;
                } else {
                    Call<ResponseBody> call = RetrofitClient.getInstance().getApi().resetPassword(uid, "student", strOld, strNew, strConfirm);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                TastyToast.makeText(mcontext, "Successfully Changed your password ", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            } else {
                                String error = "";
                                try {
                                    String s = response.errorBody().string();
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONObject child = jsonObject.getJSONObject("data");
                                    String msg = child.getString("msg");
                                    error = msg.toUpperCase();

                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                                TastyToast.makeText(mcontext, "Error occured :" + error, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }
                            changeDiag.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            TastyToast.makeText(mcontext, "Error occured " + t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            changeDiag.dismiss();
                        }
                    });
                    rlPassword.setClickable(true);
                }
            }
        });

    }

    private void ProfileDiag() {
        final String uid, token;
        uid = Preference.getSharedPreferenceString(mcontext, "uid", "");
        token = Preference.getSharedPreferenceString(mcontext, "token", "");
        dialog = new Dialog(mcontext);
        final EditText etName, etEmail, etMobile, etAboutme, etLocation, etEducation, etSkills, etJobs, etLinks, etAdditional;
        ImageView close;
        Button submit;
        dialog.setContentView(R.layout.profile_diag);
        dialog.findViewById(R.id.textview).setVisibility(View.GONE);
        etAboutme = dialog.findViewById(R.id.etAboutme);
        etName = dialog.findViewById(R.id.etName);
        etEmail = dialog.findViewById(R.id.etEmail);
        etName.setText(Preference.getSharedPreferenceString(mcontext, "name", ""));
        etEmail.setText(Preference.getSharedPreferenceString(mcontext, "email", ""));
        etLocation = dialog.findViewById(R.id.etlocation);
        etEducation = dialog.findViewById(R.id.etEducation);
        etSkills = dialog.findViewById(R.id.etSkills);
        etJobs = dialog.findViewById(R.id.etJob);
        etLinks = dialog.findViewById(R.id.etLinks);
        etAdditional = dialog.findViewById(R.id.etAdditional);
        etMobile = dialog.findViewById(R.id.etContact);
        submit = dialog.findViewById(R.id.btn_submit);
        FetchValue(etName, etAboutme, etAdditional, etEducation, etSkills, etLinks, etMobile, etJobs, etLocation);
        close = dialog.findViewById(R.id.close);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    rlProfile.setClickable(true);
                    dialog.dismiss();
                }
                return true;
            }
        });
        dialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                rlProfile.setClickable(true);
            }
        });
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

    private void FetchValue(final EditText etName, final EditText etAboutme, final EditText etAdditional, final EditText etEducation, final EditText etSkills, final EditText etLinks, final EditText etMobile, final EditText etJobs, final EditText etLocation) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().view(uid, "student", "Bearer " + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String string;
                try {
                    if (response.code() == 200) {
                        string = response.body().string();
                        JSONObject jsonObject = new JSONObject(string);
                        JSONObject child = jsonObject.getJSONObject("user");
                        String about = child.getString("about");
                        String name = child.getString("name");
                        String education = child.getString("education");
                        String phone = child.getString("phone");
                        String location = child.getString("location");
                        String skills = child.getString("skills");
                        String links = child.getString("links");
                        String jobs = child.getString("jobs");
                        String additional = child.getString("additional");
                        etName.setText(name);
                        etAboutme.setText(about);
                        etAdditional.setText(additional);
                        etEducation.setText(education);
                        etMobile.setText(phone);
                        etLinks.setText(links);
                        etLocation.setText(location);
                        etSkills.setText(skills);
                        etJobs.setText(jobs);
                    } else {
                        string = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(string);
                        String errormsg = jsonObject.getString("message");
                        TastyToast.makeText(mcontext, "Error :" + errormsg, TastyToast.LENGTH_LONG, TastyToast.WARNING);
                        dialog.dismiss();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    TastyToast.makeText(mcontext, "Error :" + e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void DoWork(String aboutme, String additional, String education, String jobs, final String name, String skills, String links, String location, String mobile, String uid, String token) {
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
        final ProgressDialog pd = new ProgressDialog(mcontext);
        pd.setMax(100);
        pd.setCancelable(false);
        pd.setMessage("Saving");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String string;
                try {
                    if (response.code() == 200) {
                        TastyToast.makeText(mcontext, "Successfully updated profile. ", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Preference.setSharedPreferenceString(mcontext, "name", name);
                    } else {
                        string = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(string);
                        String errormsg = jsonObject.getString("message");
                        TastyToast.makeText(mcontext, "Error : " + errormsg, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    TastyToast.makeText(mcontext, "Error : " + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }
                dialog.dismiss();
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TastyToast.makeText(mcontext, "Error : " + t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                pd.dismiss();
                dialog.dismiss();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


}