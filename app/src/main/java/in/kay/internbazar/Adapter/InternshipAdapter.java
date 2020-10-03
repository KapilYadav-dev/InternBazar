package in.kay.internbazar.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

import in.kay.internbazar.Model.InternshipModel;
import in.kay.internbazar.R;

public class InternshipAdapter extends RecyclerView.Adapter<InternshipAdapter.ViewHolder>{
    List<InternshipModel> list=new ArrayList<>();

    public InternshipAdapter(List<InternshipModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvCompany.setText(list.get(position).getCompanyName());
        holder.tvLocation.setText(list.get(position).getLocation().toUpperCase());
        holder.tvStart.setText(list.get(position).getStartDate());
        holder.tvDuration.setText(list.get(position).getInternshipPeriod());
        holder.tvStipend.setText(list.get(position).getStipend());
        holder.tvEnd_date.setText(list.get(position).getApplyBy());
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        FoldingCell foldingCell;
        TextView tvTitle,tvCompany,tvLocation,tvStart,tvDuration,tvStipend,tvEnd_date,tvDescription,tvSkills,tvWhocanapply,tvPerks,Button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foldingCell=itemView.findViewById(R.id.folding_cell);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvCompany=itemView.findViewById(R.id.tv_company);
            tvLocation=itemView.findViewById(R.id.tv_location);
            tvStart=itemView.findViewById(R.id.tv_start);
            tvDuration=itemView.findViewById(R.id.tv_duration);
            tvStipend=itemView.findViewById(R.id.tv_stipend);
            tvEnd_date=itemView.findViewById(R.id.tv_end_date);
            tvDescription=itemView.findViewById(R.id.tv_desp);
            tvSkills=itemView.findViewById(R.id.tv_skills);
            tvWhocanapply=itemView.findViewById(R.id.tv_who_apply);
            tvPerks=itemView.findViewById(R.id.tv_perks);
            Button=itemView.findViewById(R.id.button);
        }
    }
}
