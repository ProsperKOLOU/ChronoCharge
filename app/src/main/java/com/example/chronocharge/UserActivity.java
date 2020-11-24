package com.example.chronocharge;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static UserActivity inst;
    private TextView alarmTextView;

    private static final int uneHeure = 3600000;


    private Button Gobuttton;
    private Button CancelButton;


    public static UserActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Gobuttton = (Button) findViewById(R.id.Gobutton);
        Gobuttton.setOnClickListener(this);
        CancelButton = (Button) findViewById(R.id.Cancelbutton);
        CancelButton.setOnClickListener(this);

        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTimePicker.setIs24HourView(true);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        alarmTextView.setText("Alame");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);





    }


    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }


    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Click to open");
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Gobutton:
                startService();
                alarmTimePicker.setEnabled(false);
                Log.d("MyActivity", "Alarm On");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
                Intent myIntent = new Intent(this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, myIntent, 0);
                alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                Gobuttton.setEnabled(false);
                CancelButton.setEnabled(false);

                Intent batteryActivity = new Intent(this,BatteryActivity.class);
                startActivity(batteryActivity);

                break;
            case R.id.Cancelbutton:
                alarmManager.cancel(pendingIntent);
                setAlarmText("");
                Log.d("MyActivity", "Alarm Off");
                break;
        }

    }
}