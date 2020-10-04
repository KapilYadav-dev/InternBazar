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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sdsmdg.tastytoast.TastyToast;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
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

public class Signup extends Fragment {
    Context mcontext;
    View view;
    EditText etEmail, etPassword, etConfirmPassword, etName;
    Button btnSignup;
    LinearLayout ll;
    String name, email, password, confirmpassword;
    Dialog dialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        Initz();
    }

    private void Initz() {
        dialog = new Dialog(mcontext);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        etName = view.findViewById(R.id.etName);
        ll = view.findViewById(R.id.llLogin);
        btnSignup = view.findViewById(R.id.button);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mFragment = null;
                mFragment = new Login();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mFragment).commit();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupUser();
            }
        });
    }

    private void SignupUser() {
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        confirmpassword = etConfirmPassword.getText().toString();
        if (name.isEmpty()) {
            etName.setError("Username is required");
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
        if (password.isEmpty()) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6 && confirmpassword.length() < 6) {
            etPassword.setError("Password should atleast 6 char long");
            etConfirmPassword.setError("Password should atleast 6 char long");
            etConfirmPassword.requestFocus();
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmpassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        } else {
            RetroWork();
        }
    }

    private void RetroWork() {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().createUser(email, password, "student", name);
        final ProgressDialog pd = new ProgressDialog(mcontext);
        pd.setMax(100);
        pd.setMessage("Sending you OTP");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                int code = response.code();
                String string = null, id, message;
                try {
                    if (code == 200) {
                        string = response.body().string();
                        JSONObject jsonObject = new JSONObject(string);
                        id = jsonObject.getString("id");
                        OTPPopup(id);
                    } else {
                        string = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(string);
                        JSONArray array = jsonObject.getJSONArray("data");
                        JSONObject child = array.getJSONObject(0);
                        message = child.getString("msg");
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
                TastyToast.makeText(mcontext, "Error : " +t.getMessage(), TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            }
        });
    }


    private void OTPPopup(final String id) {
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
                                TastyToast.makeText(mcontext, "You are successfully registered.", TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }
}