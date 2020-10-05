package in.kay.internbazar.UI.HomeUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import in.kay.internbazar.R;

public class Home extends Fragment implements View.OnClickListener {
    Context mcontext;
    View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        SetAllClickable();
    }

    private void SetAllClickable() {
        view.findViewById(R.id.rl_banglore).setClickable(true);
        view.findViewById(R.id.rl_chennai).setClickable(true);
        view.findViewById(R.id.rl_delhi).setClickable(true);
        view.findViewById(R.id.rl_hyderabad).setClickable(true);
        view.findViewById(R.id.rl_international).setClickable(true);
        view.findViewById(R.id.rl_kolkata).setClickable(true);
        view.findViewById(R.id.rl_mumbai).setClickable(true);
        view.findViewById(R.id.rl_wfh).setClickable(true);
        view.findViewById(R.id.tv_view_all).setClickable(true);
        view.findViewById(R.id.rl_ngo).setClickable(true);
        view.findViewById(R.id.rl_mba).setClickable(true);
        view.findViewById(R.id.rl_media).setClickable(true);
        view.findViewById(R.id.rl_engineering).setClickable(true);
        view.findViewById(R.id.rl_part_time).setClickable(true);
        view.findViewById(R.id.rl_design).setClickable(true);
        view.findViewById(R.id.rl_science).setClickable(true);
        view.findViewById(R.id.rl_humanities).setClickable(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        Initz();
    }

    private void Initz() {
        view.findViewById(R.id.rl_banglore).setOnClickListener(this);
        view.findViewById(R.id.rl_chennai).setOnClickListener(this);
        view.findViewById(R.id.rl_delhi).setOnClickListener(this);
        view.findViewById(R.id.rl_hyderabad).setOnClickListener(this);
        view.findViewById(R.id.rl_international).setOnClickListener(this);
        view.findViewById(R.id.rl_kolkata).setOnClickListener(this);
        view.findViewById(R.id.rl_mumbai).setOnClickListener(this);
        view.findViewById(R.id.rl_wfh).setOnClickListener(this);
        view.findViewById(R.id.tv_view_all).setOnClickListener(this);
        view.findViewById(R.id.rl_ngo).setOnClickListener(this);
        view.findViewById(R.id.rl_mba).setOnClickListener(this);
        view.findViewById(R.id.rl_media).setOnClickListener(this);
        view.findViewById(R.id.rl_engineering).setOnClickListener(this);
        view.findViewById(R.id.rl_part_time).setOnClickListener(this);
        view.findViewById(R.id.rl_design).setOnClickListener(this);
        view.findViewById(R.id.rl_science).setOnClickListener(this);
        view.findViewById(R.id.rl_humanities).setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_view_all:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("type", "");
                startActivity(intent);
                view.findViewById(R.id.tv_view_all).setClickable(false);
                break;
            case R.id.rl_banglore:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "bangalore");
                intent.putExtra("type", "city");
                view.findViewById(R.id.rl_banglore).setClickable(false);
                startActivity(intent);
                break;
            case R.id.rl_chennai:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "chennai");
                intent.putExtra("type", "city");
                view.findViewById(R.id.rl_chennai).setClickable(false);
                startActivity(intent);
                break;
            case R.id.rl_kolkata:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "kolkata");
                intent.putExtra("type", "city");
                view.findViewById(R.id.rl_kolkata).setClickable(false);
                startActivity(intent);
                break;
            case R.id.rl_delhi:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "delhi");
                intent.putExtra("type", "city");
                view.findViewById(R.id.rl_delhi).setClickable(false);
                startActivity(intent);
                break;
            case R.id.rl_hyderabad:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "hyderabad");
                intent.putExtra("type", "city");
                view.findViewById(R.id.rl_hyderabad).setClickable(false);
                startActivity(intent);
                break;
            case R.id.rl_international:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "international");
                intent.putExtra("type", "city");
                view.findViewById(R.id.rl_international).setClickable(false);
                startActivity(intent);
                break;
            case R.id.rl_mumbai:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "mumbai");
                intent.putExtra("type", "city");
                view.findViewById(R.id.rl_mumbai).setClickable(false);
                startActivity(intent);
                break;
            case R.id.rl_design:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "design");
                intent.putExtra("type", "Category");
                view.findViewById(R.id.rl_design).setClickable(false);
                startActivity(intent);
                break;
            case R.id.rl_humanities:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "humanities");
                intent.putExtra("type", "Category");
                view.findViewById(R.id.rl_humanities).setClickable(false);
                startActivity(intent);
                break;
            case R.id.rl_media:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "media");
                intent.putExtra("type", "Category");
                view.findViewById(R.id.rl_media).setClickable(false);
                startActivity(intent);
                break;
            case R.id.rl_part_time:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "part_time");
                intent.putExtra("type", "Category");
                view.findViewById(R.id.rl_part_time).setClickable(false);
                startActivity(intent);
                break;

            case R.id.rl_engineering:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "engineering");
                intent.putExtra("type", "Category");
                view.findViewById(R.id.rl_engineering).setClickable(false);
                startActivity(intent);
                break;

            case R.id.rl_ngo:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "ngo");
                intent.putExtra("type", "Category");
                view.findViewById(R.id.rl_ngo).setClickable(false);
                startActivity(intent);
                break;

            case R.id.rl_mba:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "mba");
                intent.putExtra("type", "Category");
                view.findViewById(R.id.rl_mba).setClickable(false);
                startActivity(intent);
                break;

            case R.id.rl_science:
                intent = new Intent(mcontext, Detail.class);
                intent.putExtra("query", "science");
                intent.putExtra("type", "Category");
                view.findViewById(R.id.rl_science).setClickable(false);
                startActivity(intent);
                break;
        }
    }
}