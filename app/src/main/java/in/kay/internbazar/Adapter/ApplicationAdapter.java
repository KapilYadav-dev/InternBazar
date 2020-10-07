package in.kay.internbazar.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.kay.internbazar.Model.ApplicationModel;
import in.kay.internbazar.R;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {
    List<ApplicationModel> list=new ArrayList<>();

    public ApplicationAdapter(List<ApplicationModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.application_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String status=list.get(position).getStatus();
        holder.tv_company.setText(list.get(position).getCompanyName());
        holder.tv_title.setText(list.get(position).getInternshipProfile());
        if (status.equalsIgnoreCase("accepted"))
        {
            holder.tv_status.setText("Accepted");
            holder.tv_status.setTextColor(Color.parseColor("#51CF66"));
            holder.relativeLayout.setBackgroundResource(R.drawable.accepted_back);
        }
        else if (status.equalsIgnoreCase("rejected"))
        {
            holder.tv_status.setText("Rejected");
            holder.tv_status.setTextColor(Color.parseColor("#FFFFFF"));
            holder.relativeLayout.setBackgroundResource(R.drawable.rejected_back);
        }
        else
        {
            holder.tv_status.setText("Applied");
            holder.tv_status.setTextColor(Color.parseColor("#945e12"));
            holder.relativeLayout.setBackgroundResource(R.drawable.applied_back);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_company,tv_status;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_company=itemView.findViewById(R.id.tv_company);
            tv_status=itemView.findViewById(R.id.tv_status);
            relativeLayout=itemView.findViewById(R.id.rl);
        }
    }
}
