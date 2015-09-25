package com.gzz.sendsms;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class BootCompletedReceiver  extends BroadcastReceiver {

	 @Override 
	  public void onReceive(Context context, Intent intent) { 
	    if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) 
	    { 
	      Intent newIntent = new Intent(context, MainActivity.class); 
	      newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //注意，必须添加这个标记，否则启动会失败 
	      context.startActivity(newIntent);       
	    }       
	  } 
    
}
