package com.example.administrator.newapplication.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.service.AlarmReceiver;
import com.example.administrator.newapplication.ui.ClockActivity;

import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;

public class CFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = CFragment.class.getSimpleName();

    private TimePicker mTimePicker;

    //闹钟
    private Button cc_clock;

    private int mHour = -1;
    private int mMinute = -1;

    public static final long DAY = 1000L * 60 * 60 * 24;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c,null);
        cc_clock = view.findViewById(R.id.cc_clock);
        cc_clock.setOnClickListener(this);
        abc(view);
        return view;
    }

    private void abc(final View view) {
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        if(mHour == -1 && mMinute == -1) {
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
        }

        mTimePicker = view.findViewById(R.id.timePicker);
        mTimePicker.setCurrentHour(mHour);
        mTimePicker.setCurrentMinute(mMinute);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                mHour = hourOfDay;
                mMinute = minute;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cc_clock:
                startActivity(new Intent(getActivity(),ClockActivity.class));
                break;
        }
    }
}
