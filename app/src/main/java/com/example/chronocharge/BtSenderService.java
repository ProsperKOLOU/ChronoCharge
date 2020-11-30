package com.example.chronocharge;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.util.Log;

public class BtSenderService extends JobService {

    private String levelMessage = "100";
    private int bLevel ;
    static BTConnection btComm = null; ;
    private MainActivity comInit = new MainActivity();


    @Override
    public boolean onStartJob(JobParameters params) {
        btComm = comInit.btCommCopie();
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getApplicationContext().registerReceiver(levelReceiver,iFilter);
        Log.d("BtSenderService", "OnstartJob");
        new maTask().execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("BtSenderService", "OnstopJob");
        return true;
    }

    public class maTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            if(bLevel<100)
                levelMessage = "0"+bLevel;
            else
                levelMessage ="100";

            btComm.sendBatteryLevel(levelMessage);
            Log.d("LEVEL", ""+bLevel);
            return null;
        }
    }

    private BroadcastReceiver levelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        }
    };


}


