package org.jboss.jdf.ticketmonster.android;

import org.apache.cordova.DroidGap;

import android.os.Bundle;

public class TicketMonsterActivity extends DroidGap {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index-android.html");
    }

}
