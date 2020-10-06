package in.kay.internbazar.UI.HomeUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import in.kay.internbazar.Adapter.SliderAdapter;
import in.kay.internbazar.Model.SliderItem;
import in.kay.internbazar.R;

public class Home extends Fragment implements View.OnClickListener {
    Context mcontext;
    View view;
    SliderView sliderView;
    private SliderAdapter adapter;
    List<SliderItem> list=new ArrayList<>();
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
        Slider();
    }

    private void Slider() {
        sliderView = view.findViewById(R.id.imageSlider);
        list.add(new SliderItem("Welcome to InterBazaar","https://1.bp.blogspot.com/-jyXTHlkvooI/X3v8yXQ9ZmI/AAAAAAAAABY/GCsdQczEdiwAzzbxhz6k1yMqg2sspbZqACNcBGAsYHQ/s1632/r1920.png"));
        list.add(new SliderItem("Get all latest course notifications..","https://cdn.dribbble.com/users/4214751/screenshots/10503785/media/8ddd53e41258703dc23cc8bd3ca68afd.png"));
        list.add(new SliderItem("Apply many internships at a time","https://1.bp.blogspot.com/-mkJnqu3-Eos/X3v8iXRaEII/AAAAAAAAABQ/HYf_N5ybAnIyUcWpA8MjALGcsxcYCbkMQCNcBGAsYHQ/s827/new.png"));
        adapter = new SliderAdapter(mcontext,list);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
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