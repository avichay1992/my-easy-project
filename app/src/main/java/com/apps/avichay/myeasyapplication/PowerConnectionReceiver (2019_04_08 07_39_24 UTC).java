package com.apps.avichay.myeasyapplication;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.os.BatteryManager;
        import android.util.Log;
        import android.widget.Toast;

public class PowerConnectionReceiver extends BroadcastReceiver {
    private static boolean countCharge = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive","start");
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING) || (status == BatteryManager.BATTERY_STATUS_FULL);

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = (chargePlug== BatteryManager.BATTERY_PLUGGED_USB);
        boolean acCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_AC);

        Log.e("onReceive","isCharging="+isCharging);
        Log.e("onReceive","usbCharge="+usbCharge);
        Log.e("onReceive","acCharge="+acCharge);

        if (isCharging && !countCharge) {
            Toast.makeText(context, "Charging", Toast.LENGTH_SHORT).show();
            countCharge = true;
        } else if (!isCharging && countCharge) {
            Toast.makeText(context, "Not Charging", Toast.LENGTH_SHORT).show();
            countCharge = false;
        }
    }
}
