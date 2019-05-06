package com.example.helloworld;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private int[] layouts;


    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject( View view,  Object o) {
        return view ==o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layouts = new int[]{
                R.layout.screen1,
                R.layout.screen2,
                R.layout.screen3
        };

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view    = layoutInflater.inflate(layouts[position], container, false);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        View v = (View) object;
        container.removeView(v);
    }
}
