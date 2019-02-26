package com.mario.iot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CustomBroadcastReceiver extends BroadcastReceiver {

    private ConnectionChange connectionChange;

    public CustomBroadcastReceiver(ConnectionChange connectionChange){
        this.connectionChange = connectionChange;
    }

    public CustomBroadcastReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(isOnline(context)){
            connectionChange.connectionChange();
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

}
