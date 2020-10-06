package in.kay.internbazar.UI.Intro;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sdsmdg.tastytoast.TastyToast;
import com.tapadoo.alerter.Alerter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import in.kay.internbazar.Api.RetrofitClient;
import in.kay.internbazar.R;
import in.kay.internbazar.UI.HomeUI.MainActivity;
import in.kay.internbazar.Utils.Preference;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends Fragment {
    Context mcontext;
    LinearLayout ll;
    View view;
    EditText etEmail, etPassword;
    Button btnLogin;
    String password, email;
    TextView forgetPass;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        Initz();

    }

    private void Initz() {
        ll = view.findViewById(R.id.llSignup);
        btnLogin = view.findViewById(R.id.button);
        etEmail = view.findViewById(R.id.etEmail);
        forgetPass = view.findViewById(R.id.tv_forget);
        etPassword = view.findViewById(R.id.etPassword);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mFragment = null;
                mFragment = new Signup();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mFragment).commit();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPass.setClickable(false);
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                if (email.isEmpty()) {
                    etEmail.setError("Email cannot be empty");
                    etEmail.requestFocus();
                    forgetPass.setClickable(true);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Please enter a valid email id");
                    etEmail.requestFocus();
                    forgetPass.setClickable(true);
                    return;
                } else {
                    Call<ResponseBody> call = RetrofitClient.getInstance().getApi().forgotPassword(email, "student");
                    final ProgressDialog pd = new ProgressDialog(mcontext);
                    pd.setMax(100);
                    pd.setMessage("Working...");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setCancelable(false);
                    pd.show();
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                TastyToast.makeText(mcontext, "Send password reset link on your mail.", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            } else {
                                TastyToast.makeText(mcontext, "User doesn't exist.", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            }
                            pd.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            TastyToast.makeText(mcontext, "Error occured " + t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            pd.dismiss();
                        }
                    });
                    forgetPass.setClickable(true);
                }
            }
        });
    }

    private void LoginUser() {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
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
        if (password.isEmpty()) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password should atleast 6 char long");
            etPassword.requestFocus();
            return;
        } else {
            RetroWork();
        }
    }

    private void RetroWork() {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().loginUser(email, password);
        final ProgressDialog pd = new ProgressDialog(mcontext);
        pd.setMax(100);
        pd.setMessage("Logging you in...");
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                String string;
                try {
                    if (response.code() == 200) {
                        TastyToast.makeText(mcontext, "Successfully logged in", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        string = response.body().string();
                        final JSONObject jsonObject = new JSONObject(string);
                        String token = jsonObject.getString("token");
                        String uid = jsonObject.getString("userId");
                        Preference.setSharedPreferenceBoolean(mcontext, "isLoggedIn", true);
                        Preference.setSharedPreferenceString(mcontext, "uid", uid);
                        Preference.setSharedPreferenceString(mcontext, "token", token);
                        Preference.setSharedPreferenceString(mcontext, "email", email);
                        GetSaveName(uid, token);
                        startActivity(new Intent(mcontext, MainActivity.class));
                    } else {
                        string = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(string);
                        JSONObject child = jsonObject.getJSONObject("data");
                        String message = child.getString("msg");
                        String id = child.getString("id");
                        Toast.makeText(mcontext, "" + message, Toast.LENGTH_SHORT).show();
                        if (message.equalsIgnoreCase("otp sent please verify yourself")) {
                            OTPPopup(id);
                        }
                        Alerter.create(getActivity())
                                .setTitle("Error ")
                                .setBackgroundColorRes(R.color.colorPrimary)
                                .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                .setTitleTypeface(Typeface.createFromAsset(mcontext.getAssets(), "sans_bold.ttf"))
                                .setText(message)
                                .setTextAppearance(R.style.AlertTextAppearance_Text)
                                .setTextTypeface(Typeface.createFromAsset(mcontext.getAssets(), "sans_regular.ttf"))
                                .show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                TastyToast.makeText(mcontext, "Error occurred " + t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        });


    }

    private void GetSaveName(String uid, String token) {
        Call<ResponseBody> name = RetrofitClient.getInstance().getApi().view(uid, "student", "Bearer " + token);
        name.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String string;
                if (response.code() == 200) {
                    try {
                        string = response.body().string();
                        JSONObject jsonObject1 = new JSONObject(string);
                        JSONObject child = jsonObject1.getJSONObject("user");
                        String name = child.getString("name");
                        Preference.setSharedPreferenceString(mcontext, "name", name);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void OTPPopup(final String id) {
        Dialog dialog = new Dialog(mcontext);
        TextView msg;
        final OtpTextView otpTextView;
        dialog.setContentView(R.layout.otp_diag);
        otpTextView = dialog.findViewById(R.id.otp_view);
        msg = dialog.findViewById(R.id.tv_2);
        msg.setText("Please type the verification code sent to " + email);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                Call<ResponseBody> call = RetrofitClient.getInstance().getApi().verifyOTP(id, otp);
                final ProgressDialog pd = new ProgressDialog(mcontext);
                pd.setMax(100);
                pd.setCancelable(false);
                pd.setMessage("Verifying");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        pd.dismiss();
                        String string;
                        try {
                            if (response.code() == 200) {
                                string = response.body().string();
                                JSONObject jsonObject = new JSONObject(string);
                                String userID = jsonObject.getString("userId");
                                String token = jsonObject.getString("token");
                                Preference.setSharedPreferenceBoolean(mcontext, "isLoggedIn", true);
                                Preference.setSharedPreferenceString(mcontext, "uid", userID);
                                Preference.setSharedPreferenceString(mcontext, "token", token);
                                Preference.setSharedPreferenceString(mcontext, "email", email);
                                GetSaveName(userID, token);
                                TastyToast.makeText(mcontext, "You are successfully registered.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                startActivity(new Intent(getActivity(), MainActivity.class));
                            } else {
                                otpTextView.showError();
                                Alerter.create(getActivity())
                                        .setTitle("Error ")
                                        .setBackgroundColorRes(R.color.colorPrimary)
                                        .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                        .setTitleTypeface(Typeface.createFromAsset(mcontext.getAssets(), "sans_bold.ttf"))
                                        .setText("Invalid OTP")
                                        .setTextAppearance(R.style.AlertTextAppearance_Text)
                                        .setTextTypeface(Typeface.createFromAsset(mcontext.getAssets(), "sans_regular.ttf"))
                                        .show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pd.dismiss();
                    }
                });
            }
        });
    }
}