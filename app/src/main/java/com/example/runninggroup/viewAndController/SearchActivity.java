package com.example.runninggroup.viewAndController;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.runninggroup.R;
import com.example.runninggroup.util.StatusBarUtils;
import com.example.runninggroup.util.StringUtil;
import com.example.runninggroup.viewAndController.adapter.MyPagerAdapter;
import com.example.runninggroup.viewAndController.adapter.SearchAdapter;
import com.example.runninggroup.viewAndController.fragment.FragmentPersonSearch;
import com.example.runninggroup.viewAndController.fragment.FragmentTeamSearch;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    RadioButton personBtn,teamBtn;
    RadioGroup tabGroup;
    ViewPager mViewPager;
    List<Fragment> frgList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initEvent();
    }

    private void initEvent() {
        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(SearchActivity.this, checkedId + "", Toast.LENGTH_SHORT).show();
                if (group.getCheckedRadioButtonId() == R.id.team_tab) {
                    teamBtn.setBackground(getDrawable(R.drawable.btn_style10));
                    personBtn.setBackground(null);
                    mViewPager.setCurrentItem(1);
                }else {
                    personBtn.setBackground(getDrawable(R.drawable.btn_style9));
                    teamBtn.setBackground(null);
                    mViewPager.setCurrentItem(0);
                }
            }
        });

    }

    private void initView() {
        personBtn = findViewById(R.id.person_tab);
        teamBtn = findViewById(R.id.team_tab);
        tabGroup = findViewById(R.id.tabs_rg);
        mViewPager = findViewById(R.id.viewPager);
        frgList = new ArrayList<>();
        frgList.add(new FragmentPersonSearch());
        frgList.add(new FragmentTeamSearch());
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), (ArrayList<Fragment>) frgList, null));
        StatusBarUtils.setWindowStatusBarColor(this,R.color.top);



    }

    @Override
    public void onClick(View v) {

    }
}
