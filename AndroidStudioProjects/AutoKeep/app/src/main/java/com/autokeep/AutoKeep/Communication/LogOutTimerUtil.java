package com.autokeep.AutoKeep.Communication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.autokeep.AutoKeep.UserActivityPack.LoginActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class LogOutTimerUtil {
    static final int LOGOUT_TIME = 60000; // delay in milliseconds i.e. 5 min = 300000 ms or use timeout argument
    static Timer longTimer;

    public static synchronized void startLogoutTimer(final Context context, final LogOutListener logOutListener) {
        if (longTimer != null) {
            longTimer.cancel();
            longTimer = null;
        }
        if (longTimer == null) {

            longTimer = new Timer();

            longTimer.schedule(new TimerTask() {

                public void run() {

                    cancel();

                    longTimer = null;

                    try {
                        boolean foreGround = new ForegroundCheckTask().execute(context).get();

                        try {
                            clientSocket.getInstance().close();
                        } catch (NullPointerException e) {
                            //finish();
                        }
                        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                        context.startActivity(intent);
                        //if (foreGround) {
                        // logOutListener.doLogout();
                        //  }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }, LOGOUT_TIME);
        }
    }

    public static synchronized void stopLogoutTimer() {
        if (longTimer != null) {
            longTimer.cancel();
            longTimer = null;
        }
    }

    public interface LogOutListener {
        void doLogout();
    }

    static class ForegroundCheckTask extends AsyncTask <Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List <ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

}