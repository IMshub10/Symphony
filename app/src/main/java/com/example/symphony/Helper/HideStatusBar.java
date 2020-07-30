package com.example.symphony.Helper;

import android.view.View;

public class HideStatusBar {

    public  void hide(View view){
        view.setSystemUiVisibility(hide_flags());
    }
    public int hide_flags(){
        return  View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }
}
