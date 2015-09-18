package com.example.smsviawifi;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSSender {
	public void sendSmsByManager(Context context, String pNumber,
			String message) {
		try {
			// Get the default instance of the SmsManager
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(pNumber, null,
					message, null, null);
			
		} catch (Exception ex) {
			Log.e("WIFISMS",ex.toString());
		}
	}
}
