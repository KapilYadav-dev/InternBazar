package in.kay.internbazar.UI.HomeUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.sdsmdg.tastytoast.TastyToast;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.kay.internbazar.Adapter.InternshipAdapter;
import in.kay.internbazar.Api.RetrofitClient;
import in.kay.internbazar.Model.InternshipModel;
import in.kay.internbazar.R;
import in.kay.internbazar.Utils.CheckInternet;
import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail extends AppCompatActivity {
    List<InternshipModel> models = new ArrayList<>();
    RecyclerView recyclerView;
    InternshipAdapter adapter;
    String query, type;
    Call<ResponseBody> call;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        query = getIntent().getStringExtra("query");
        type = getIntent().getStringExtra("type");
        recyclerView = findViewById(R.id.rv);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (type.equalsIgnoreCase("")) {
            call = RetrofitClient.getInstance().getApi().getInternshipAll();
        } else if (type.equalsIgnoreCase("City")) {
            call = RetrofitClient.getInstance().getApi().getInternshipByLocation(query);
        } else if (type.equalsIgnoreCase("Category")) {
            call = RetrofitClient.getInstance().getApi().getInternshipByInternshipType(query);
        }
        DoWork(call);
        RefreshData();
    }

    private void RefreshData() {
        final PullToRefreshView mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        Intent intent = getIntent();
                        overridePendingTransition(0, 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                    }
                }, 2000);
            }
        });
    }

    private void DoWork(Call<ResponseBody> call) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMax(100);
        pd.setCancelable(false);
        pd.setMessage("Working...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String string;
                try {
                    pd.dismiss();
                    if (response.code() == 200) {
                        string = response.body().string();
                        JSONObject jsonObject = new JSONObject(string);
                        JSONArray jsonArray = jsonObject.getJSONArray("post");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject child = jsonArray.getJSONObject(i);
                            String _id = child.getString("_id");
                            String location = child.getString("location");
                            String skillsReq = child.getString("skillsReq");
                            String strskillsReq = skillsReq.replaceAll(",", "\n");
                            String title = child.getString("title");
                            String cap = title.substring(0, 1).toUpperCase() + title.substring(1);
                            String description = child.getString("description");
                            String stipend = "₹ " + child.getString("stipend");
                            String internshipPeriod = child.getString("internshipPeriod");
                            String companyName = child.getString("companyName");
                            String internshipType = child.getString("internshipType");
                            String applyBy = child.getString("applyBy");
                            String startDate = child.getString("startDate");
                            String whocanApply = child.getString("whocanApply");
                            String perks = child.getString("perks");
                            String __v = child.getString("__v");
                            Integer vacancy = child.getInt("vacancy");
                            if (!DateTimeUtils.isToday(applyBy + " 23:59:59")) {
                                models.add(new InternshipModel(_id, location, strskillsReq, cap, description, stipend, internshipPeriod, companyName, internshipType, applyBy, startDate, whocanApply, perks, __v, vacancy));
                            }
                            adapter = new InternshipAdapter(models, getBaseContext());
                            SlideInBottomAnimatorAdapter animatorAdapter = new SlideInBottomAnimatorAdapter(adapter, recyclerView);
                            recyclerView.setAdapter(animatorAdapter);
                        }
                    } else {
                        string = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(string);
                        String error = jsonObject.getString("message");
                        if (error.equalsIgnoreCase("No such internships found")) {
                            recyclerView.setVisibility(View.GONE);
                            findViewById(R.id.tv_not_found).setVisibility(View.VISIBLE);
                            findViewById(R.id.iv_not_found).setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            findViewById(R.id.tv_not_found).setVisibility(View.GONE);
                            findViewById(R.id.iv_not_found).setVisibility(View.GONE);
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                TastyToast.makeText(getBaseContext(), "Error : " + t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                onBackPressed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        CheckInternet checkInternet = new CheckInternet();
        checkInternet.Check(this);
    }
}