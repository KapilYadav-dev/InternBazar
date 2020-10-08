package in.kay.internbazar.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.thunder413.datetimeutils.DateTimeStyle;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.ramotion.foldingcell.FoldingCell;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.kay.internbazar.Api.RetrofitClient;
import in.kay.internbazar.Model.InternshipModel;
import in.kay.internbazar.R;
import in.kay.internbazar.Utils.Preference;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InternshipAdapter extends RecyclerView.Adapter<InternshipAdapter.ViewHolder> {
    List<InternshipModel> list = new ArrayList<>();
    String uid, token;
    Context context;
    Dialog dialog;

    public InternshipAdapter(List<InternshipModel> list, Context context) {
        this.list = list;
        this.context = context;
        uid = Preference.getSharedPreferenceString(context, "uid", "");
        token = Preference.getSharedPreferenceString(context, "token", "");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String startDate=list.get(position).getStartDate();
        String endDate=list.get(position).getApplyBy();
        String strStartDate= DateTimeUtils.formatWithStyle(startDate, DateTimeStyle.LONG);
        String strEndDate= DateTimeUtils.formatWithStyle(endDate, DateTimeStyle.LONG);
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvCompany.setText(list.get(position).getCompanyName());
        holder.tvLocation.setText(list.get(position).getLocation().toUpperCase());
        holder.tvStart.setText(strStartDate);
        holder.tvDuration.setText(list.get(position).getInternshipPeriod());
        holder.tvStipend.setText(list.get(position).getStipend());
        holder.tvEnd_date.setText(strEndDate);
        holder.tvDescription.setText(list.get(position).getDescription());
        holder.tvSkills.setText(list.get(position).getSkillsReq());
        holder.tvWhocanapply.setText(list.get(position).getWhocanApply());
        holder.tvPerks.setText(list.get(position).getPerks());
        holder.foldingCell.fold(true);
        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.foldingCell.toggle(false);
            }
        });
        holder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(view.getContext());
                Call<ResponseBody> call = RetrofitClient.getInstance().getApi().applyInternship(list.get(position).get_id(), uid, "Bearer " + token);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String string;
                        try {
                            if (response.code() == 200) {
                                ShowDiag();
                                TastyToast.makeText(context, "Successfully applied to " + list.get(position).getTitle(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            } else {
                                string = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(string);
                                String errormsg = jsonObject.getString("message");
                                TastyToast.makeText(context, "Error : " + errormsg, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        TastyToast.makeText(context, "Error : " + t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                });
            }
        });
    }

    private void ShowDiag() {
        Button done;
        dialog.setContentView(R.layout.succes);
        done = dialog.findViewById(R.id.btn_done);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        FoldingCell foldingCell;
        Button apply;
        TextView tvTitle, tvCompany, tvLocation, tvStart, tvDuration, tvStipend, tvEnd_date, tvDescription, tvSkills, tvWhocanapply, tvPerks, Button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvCompany = itemView.findViewById(R.id.tv_company);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvStart = itemView.findViewById(R.id.tv_start);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvStipend = itemView.findViewById(R.id.tv_stipend);
            tvEnd_date = itemView.findViewById(R.id.tv_end_date);
            tvDescription = itemView.findViewById(R.id.tv_desp);
            tvSkills = itemView.findViewById(R.id.tv_skills);
            tvWhocanapply = itemView.findViewById(R.id.tv_who_apply);
            tvPerks = itemView.findViewById(R.id.tv_perks);
            apply = itemView.findViewById(R.id.button);
        }
    }
}
