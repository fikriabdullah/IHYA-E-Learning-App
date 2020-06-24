package com.example.islamdigitalecosystem;

import android.animation.ArgbEvaluator;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class alquranfitur extends AppCompatActivity {
    ViewPager viewPager;
    adapter1 adapter;
    List<model2>models;
    Integer[] colors = null ;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alquranfitur);

        models = new ArrayList<>();
        models.add(new model2(R.drawable.surah1,"Al-Fatihah"));
        models.add(new model2(R.drawable.surah2,"Al-Baqarah"));
        models.add(new model2(R.drawable.surah3,"Al-Imran"));
        models.add(new model2(R.drawable.surah4,"An-Nisa"));
        models.add(new model2(R.drawable.surah5,"Al-Maaidah"));
        models.add(new model2(R.drawable.surah6,"Al-An'aam"));

        adapter = new adapter1(models,this);
        viewPager = findViewById(R.id.viewPager1);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130,0 , 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.colors1),
                getResources().getColor(R.color.colors2),
                getResources().getColor(R.color.colors3),
                getResources().getColor(R.color.colors4),
                getResources().getColor(R.color.colors5),
                getResources().getColor(R.color.colors6),
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() -1) && position < (colors.length -1)){
                    viewPager.setBackgroundColor(
                            (Integer)argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position +1])
                    );
                }else{
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }
}