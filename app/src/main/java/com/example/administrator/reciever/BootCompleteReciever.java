package com.example.administrator.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.action.App;

public class BootCompleteReciever extends BroadcastReceiver {
    private static final String TAG = BootCompleteReciever.class.getSimpleName();
    public BootCompleteReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        ((App) context.getApplicationContext()).correctSIM();
    }
}
