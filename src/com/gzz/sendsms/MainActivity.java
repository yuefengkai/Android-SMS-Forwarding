package com.gzz.sendsms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.R.bool;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button button;
	TextView textView1;
	int count=0;
	private Timer mTimer;

	SmsApplication myApplication;

	// 声明一个Handler对象
	private static Handler handler=new Handler();

	 private void setTimerTask() {
	        mTimer.schedule(new TimerTask() {
	            public void run() {
	                Message message = new Message();
	                message.what = 1;
	                doActionHandler.sendMessage(message);
	            }
	        }, 2000, 2000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
	    };
	 /**
	     * do some action
	     */
	    private Handler doActionHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	            int msgId = msg.what;
	            switch (msgId) {
	                case 1:
	                    // do some action
	                	getSmsInPhone();
	                    break;

	                case 2:
	                    // do some action
	                	mTimer.cancel();
	                	button.setText(R.string.btn_start);
	                    break;
	                default:
	                    break;
	            }
	        }
	    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myApplication=(SmsApplication)getApplication();

		findView();

		if (myApplication.getIsRun()) {
			setState();
		}else {
			mTimer = new Timer();
	        // start timer task
	        setTimerTask();
	        setState();
		}

	}

	public void findView() {
		button = (Button) findViewById(R.id.button1);
		textView1 = (TextView) findViewById(R.id.textView1);

		button.setOnClickListener(new Button.OnClickListener() {// 创建监听
			public void onClick(View v) {
				Log.d("myTag", "线程启动");
				// TODO Auto-generated method stub

				handler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (!myApplication.getIsRun()) {
							// init timer
					        mTimer = new Timer();
					        // start timer task
					        setTimerTask();
					        setState();
						}else {
							Toast.makeText(getApplicationContext(),"线程已启动,请不要重复启动", Toast.LENGTH_SHORT).show();
						}

					}
				});

			}

		});
	}

	public void setState() {
		 Toast.makeText(getApplicationContext(),"线程已启动", Toast.LENGTH_SHORT).show();
			button.setText("线程已启动...");
			textView1.setText("线程已启动,正在监听短信....");
			button.setEnabled(false);
			Log.e("myTag", "线程启动");
	}

	public String getSmsInPhone() {
		myApplication.setIsRun(true);

		final String SMS_URI_ALL = "content://sms/"; // 所有短信
		final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
		final String SMS_URI_SEND = "content://sms/sent"; // 已发送
		final String SMS_URI_DRAFT = "content://sms/draft"; // 草稿
		final String SMS_URI_OUTBOX = "content://sms/outbox"; // 发件箱
		final String SMS_URI_FAILED = "content://sms/failed"; // 发送失败
		final String SMS_URI_QUEUED = "content://sms/queued"; // 待发送列表

		final StringBuilder smsBuilder = new StringBuilder();

		try {
			Uri uri = Uri.parse(SMS_URI_ALL);
			String[] projection = new String[] { "_id", "address", "person",
					"body", "date", "type", };
			// Cursor cur = getContentResolver().query(uri, projection, null,
			// null, "date desc"); // 获取手机内部短信
			// 获取短信中最新的未读短信
			Cursor cur = getContentResolver().query(uri, projection,
					"read = ?", new String[] { "0" }, "date desc");


			try {
				if (cur.moveToFirst()) {
					Log.e("myTag", "已监听到新短信.................");

					int index_id = cur.getColumnIndex("_id");
					int index_Address = cur.getColumnIndex("address");
					int index_Person = cur.getColumnIndex("person");
					int index_Body = cur.getColumnIndex("body");
					int index_Date = cur.getColumnIndex("date");
					int index_Type = cur.getColumnIndex("type");

					do {
						String strAddress = cur.getString(index_Address);
						if (strAddress.contains("1065800711")) {
							continue;
						}
						int intPerson = cur.getInt(index_Person);
						String strbody = cur.getString(index_Body);
						long longDate = cur.getLong(index_Date);
						int intType = cur.getInt(index_Type);

						SimpleDateFormat dateFormat = new SimpleDateFormat("MMdd HH:mm");
						Date d = new Date(longDate);
						String strDate = dateFormat.format(d);

						String strType = "";
						if (intType == 1) {
							strType = "接收";
						} else if (intType == 2) {
							strType = "发送";
						} else if (intType == 3) {
							strType = "草稿";
						} else if (intType == 4) {
							strType = "发件箱";
						} else if (intType == 5) {
							strType = "发送失败";
						} else if (intType == 6) {
							strType = "待发送列表";
						} else if (intType == 0) {
							strType = "所以短信";
						} else {
							strType = "null";
						}

						smsBuilder.append("");
						smsBuilder.append(strAddress + "-");
						// smsBuilder.append(intPerson + ",");
						smsBuilder.append("" + strbody + "- ");
						smsBuilder.append("" + strDate + "");
						// smsBuilder.append(strType);
						smsBuilder.append("");

						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								count++;
								// 发送到手机
								sendSMS("", smsBuilder.toString());
								Log.d("myTag", "短信发送到" + "成功");
								button.setText("已转发"+count+"次！");
							}
						});

						ContentValues values = new ContentValues();
						values.put("read", "1"); // 修改短信为已读模式　　
						int result = getContentResolver().update(uri, values," _id=?", new String[] { "" + cur.getInt(0) });
						 if(result>0){
							 Log.d("myTag", "短信状态已修改!");
						 }

					} while (cur.moveToNext());

					if (null!=cur) {
						cur.close();
						cur = null;
					}
				} else {
					smsBuilder.append("no result!");
				}
			} catch (Exception e) {
				// TODO: handle exception
				if (null!=cur) {
					cur.close();
					cur = null;
				}
			}finally
	        {
				if (null!=cur) {
					cur.close();
					cur = null;
				}
	        }

		} catch (SQLiteException ex) {
			Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
		}

		return smsBuilder.toString();
	}

	/**
	 * 直接调用短信接口发短信，不含发送报告和接受报告
	 *
	 * @param phoneNumber
	 * @param message
	 */
	public void sendSMS(String phoneNumber, String message) {
		// 获取短信管理器
		android.telephony.SmsManager smsManager = android.telephony.SmsManager
				.getDefault();
		// 拆分短信内容（手机短信长度限制）
		List<String> divideContents = smsManager.divideMessage(message);
		for (String text : divideContents) {
			Log.e("myTag", phoneNumber+":"+text);
			smsManager.sendTextMessage(phoneNumber, null, text, null, null);//只发送第一条
			break;
		}
	}


}
