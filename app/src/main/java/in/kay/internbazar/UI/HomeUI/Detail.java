package in.kay.internbazar.UI.HomeUI;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail extends AppCompatActivity {
    List<InternshipModel> models = new ArrayList<>();
    RecyclerView recyclerView;
    InternshipAdapter adapter;
    String query,type;
    Call<ResponseBody> call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        query=getIntent().getStringExtra("query");
        type=getIntent().getStringExtra("type");
        recyclerView=findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (type.equalsIgnoreCase(""))
        {
            call = RetrofitClient.getInstance().getApi().getInternshipAll();
        }
        else if (type.equalsIgnoreCase("City"))
        {
            call = RetrofitClient.getInstance().getApi().getInternshipByLocation(query);

        }
        else if (type.equalsIgnoreCase("Category"))
        {
            call = RetrofitClient.getInstance().getApi().getInternshipByInternshipType(query);
        }
        DoWork(call);
    }

    private void DoWork(Call<ResponseBody> call) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMax(100);
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
                            String strskillsReq = skillsReq.replaceAll(",","\n");
                            String title = child.getString("title");
                            String description = child.getString("description");
                            String stipend = "₹ "+child.getString("stipend");
                            String internshipPeriod = child.getString("internshipPeriod");
                            String companyName = child.getString("companyName");
                            String internshipType = child.getString("internshipType");
                            String applyBy = child.getString("applyBy");
                            String startDate = child.getString("startDate");
                            String whocanApply = child.getString("whocanApply");
                            String perks = child.getString("perks");
                            String __v =child.getString("__v");
                            Integer vacancy = child.getInt("vacancy");
                            models.add(new InternshipModel(_id,location,strskillsReq,title,description,stipend,internshipPeriod,companyName,internshipType,applyBy,startDate,whocanApply,perks,__v,vacancy));
                            adapter=new InternshipAdapter(models);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        string=response.errorBody().string();
                        JSONObject jsonObject=new JSONObject(string);
                        String error=jsonObject.getString("message");
                        Toast.makeText(Detail.this, "Error :" +error, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(Detail.this, "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }
}