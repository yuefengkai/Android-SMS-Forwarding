package com.gzz.sendsms;

import android.app.Application;

public class SmsApplication extends Application
{    
    private boolean ISRUN=false;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
    }
    
    public void setIsRun(boolean value)
    {
        this.ISRUN = value;
    }
    
    public boolean getIsRun()
    {
        return ISRUN;
    }
}
