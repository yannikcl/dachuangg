package com.example.administrator.newapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.newapplication.R;


public class FollowFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.follow_test,container,false);
        TextView  textView=(TextView) view.findViewById(R.id.content);
        textView.setText("关注");
        return view;
    }
}

