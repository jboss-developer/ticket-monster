package org.jboss.jdf.ticketmonster.android;

import org.apache.cordova.DroidGap;

import android.os.Bundle;
import android.webkit.WebSettings;

public class TicketMonsterActivity extends DroidGap {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }

    @Override
    public void init() {
     super.init();
    
     WebSettings settings = this.appView.getSettings();
     settings.setUserAgentString("TicketMonster Cordova Webview Android");
    }

}
