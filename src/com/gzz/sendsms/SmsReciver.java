package com.gzz.sendsms;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		SmsMessage msg = null;
		if (null != bundle) {
			Object[] smsObj = (Object[]) bundle.get("pdus");
			for (Object object : smsObj) {
				msg = SmsMessage.createFromPdu((byte[]) object);

			}
			Date date = new Date(msg.getTimestampMillis());// 时间
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String receiveTime = format.format(date);
			Log.d("myTag", "number:" + msg.getOriginatingAddress()
					+ "   body:" + msg.getDisplayMessageBody() + "  time:"
					+ msg.getTimestampMillis());

			// 在这里写自己的逻辑
			if (msg.getOriginatingAddress().equals("13720260301")) {
				// TODO
				Log.d("myTag", "111111111111111111111111111");
			}

		}
	}
}
