package com.apps.avichay.myeasyapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class EnternetConected {

    Context myContaxt;

    public EnternetConected(Context context){
        this.myContaxt = context;
    }
    public boolean isEnternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) myContaxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null||!networkInfo.isAvailable()||!networkInfo.isConnected()) {
            return false;
        } else {
            return true;
        }
    }
}
