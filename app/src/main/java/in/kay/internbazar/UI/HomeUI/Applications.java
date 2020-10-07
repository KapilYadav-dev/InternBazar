package in.kay.internbazar.UI.HomeUI;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.kay.internbazar.Adapter.ApplicationAdapter;
import in.kay.internbazar.Api.RetrofitClient;
import in.kay.internbazar.Model.ApplicationModel;
import in.kay.internbazar.R;
import in.kay.internbazar.Utils.Preference;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Applications extends Fragment {
    Context mcontext;
    View view;
    RecyclerView recyclerView;
    List<ApplicationModel> list = new ArrayList<>();
    ApplicationAdapter adapter;

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
        String uid = Preference.getSharedPreferenceString(mcontext, "uid", "");
        String token = Preference.getSharedPreferenceString(mcontext, "token", "");
        recyclerView = view.findViewById(R.id.rv);
        final ProgressDialog pd = new ProgressDialog(mcontext);
        pd.setMax(100);
        pd.setCancelable(false);
        pd.setMessage("Loading...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
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
                        JSONArray array = child.getJSONArray("applications");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String internshipId = object.getString("internshipId");
                            String status = object.getString("status");
                            String internshipProfile = object.getString("internshipProfile");
                            String companyName = object.getString("companyName");
                            list.add(new ApplicationModel(companyName, internshipProfile, status, internshipId));
                            adapter = new ApplicationAdapter(list);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        if (array.length()==0)
                        {
                            view.findViewById(R.id.iv_not_found).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.tv_not_found).setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else {
                            view.findViewById(R.id.iv_not_found).setVisibility(View.GONE);
                            view.findViewById(R.id.tv_not_found).setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

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
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TastyToast.makeText(mcontext, "Error :" + t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                pd.dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_applications, container, false);
    }
}