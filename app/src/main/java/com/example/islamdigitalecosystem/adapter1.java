package com.example.islamdigitalecosystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class adapter1 extends PagerAdapter {
    private List<model2> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public adapter1(List<model2> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item2, container, false);
        ImageView imageView;
        TextView tittle;
        imageView = view.findViewById(R.id.image1);
        tittle = view.findViewById(R.id.title1);

        imageView.setImageResource(models.get(position).getImage());
        tittle.setText(models.get(position).getTitle());

        view.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(position == 0){
                    Toast.makeText(context, "Slide 1 Clicked",Toast.LENGTH_SHORT).show();
                } else if(position == 1){
                    Toast.makeText(context, "Slide 2 Clicked",Toast.LENGTH_SHORT).show();
                } else if(position == 2){
                    Toast.makeText(context, "Slide 3 Clicked",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Slide 4 Clicked",Toast.LENGTH_SHORT).show();
                }
            }
        });


        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
