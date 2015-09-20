package com.example.smsviawifi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSSender {
	public static void sendSmsByManager(Context context, String pNumber,
			String message, String ID) {
		try {
			// Get the default instance of the SmsManager
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(pNumber, null,
					message, null, null);
			
		} catch (Exception ex) {
			Log.e("WIFISMS",ex.toString());
		}
		
		Log.e("WIFISMS","send message ID = " + ID);
		
		
		Long timestamp = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		Date finaldate = (Date) calendar.getTime();
		DateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss", Locale.US);
		String smsdate = dateFormat.format(finaldate);
		
		SMS newSentSms = new SMS(smsdate, message, pNumber, ID, "2", message, ContactsRetriever.getContactName(pNumber, context), "1");
		
		SMS.smsList.add(0, newSentSms);
		
		OldSmsRetriever.update_message_files();
		
		/*
		String newmsg = ":[{\"dir\":\"2\",\"body\":\""+message+"\",\"time\":\""+smsdate+"\"},{";
		
		String ss = FileRead.readFromFile(("Data/messages/messages-"+ID+".json").replace("%20", " "));
		
		ss = ss.replace(":[{", newmsg);
		
		FileRead.writeFile(context.getFilesDir().getPath() + File.separator
				+ "Data" + File.separator + "messages" + File.separator,
				"messages-"+ID+".json", ss);
		*/
	}
}
