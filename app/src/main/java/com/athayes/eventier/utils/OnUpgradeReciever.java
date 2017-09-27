package com.athayes.eventier.utils;

/**
 * Created by anthonyhayes on 26/09/2017.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.facebook.login.LoginManager;
// Manage any changes to Facebook API - Log the user out upon upgrading
public class OnUpgradeReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            LoginManager.getInstance().logOut();
        }
}
