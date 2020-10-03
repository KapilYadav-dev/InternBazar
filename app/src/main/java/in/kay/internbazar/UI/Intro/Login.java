package in.kay.internbazar.UI.Intro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tapadoo.alerter.Alerter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                String string;
                try {
                    if (response.code() == 200) {
                        Toast.makeText(mcontext, "Successfully logged in", Toast.LENGTH_SHORT).show();
                        string = response.body().string();
                        JSONObject jsonObject = new JSONObject(string);
                        String token = jsonObject.getString("token");
                        String uid = jsonObject.getString("userId");
                        Preference.setSharedPreferenceBoolean(mcontext, "isLoggedIn", true);
                        Preference.setSharedPreferenceString(mcontext, "uid", uid);
                        Preference.setSharedPreferenceString(mcontext, "token", token);
                        startActivity(new Intent(mcontext, MainActivity.class));
                    } else {
                        string = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(string);
                        JSONObject child = jsonObject.getJSONObject("data");
                        String message = child.getString("msg");
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
                Toast.makeText(mcontext, "Error occurred " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}