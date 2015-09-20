package com.example.smsviawifi;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	final SmsManager sms = SmsManager.getDefault();

	@Override
	public void onReceive(Context context, Intent intent) {
		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();
		try {
			if (bundle != null) {
				final Object[] pdusObj = (Object[]) bundle.get("pdus");
				for (int i = 0; i < pdusObj.length; i++) {
					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage
							.getDisplayOriginatingAddress();
					String senderNum = phoneNumber;
					String message = currentMessage.getDisplayMessageBody();
					Log.i("WIFISMS", "senderNum: " + senderNum + "; message: "
							+ message);

					Long timestamp = currentMessage.getTimestampMillis();
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(timestamp);
					Date finaldate = (Date) calendar.getTime();
					DateFormat dateFormat = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss", Locale.US);
					String smsdate = dateFormat.format(finaldate);

					SMS newRecSms = new SMS(smsdate, message, senderNum,
							ContactsRetriever.getContactId(senderNum, context),
							"1", message, ContactsRetriever.getContactName(
									senderNum, context), "0");

					SMS.smsList.add(0, newRecSms);

					OldSmsRetriever.update_message_files();
					
					String newmsg = "{\"newmsg\":\"1\"}";
					
					FileRead.writeFile(context.getFilesDir().getPath() + File.separator
							+ "Data" + File.separator + "messages" + File.separator,
							"newmsg.json", newmsg);

					// Show Alert
					// int duration = Toast.LENGTH_LONG;
					// Toast toast = Toast.makeText(context, "senderNum: "
					// + senderNum + ", message: " + message, duration);
					// toast.show();
				} // end for loop
			} // bundle is null
		} catch (Exception e) {
			Log.e("WIFISMS", "Exception smsReceiver" + e);
		}
	}
}
