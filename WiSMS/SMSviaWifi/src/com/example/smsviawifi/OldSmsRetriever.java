package com.example.smsviawifi;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class OldSmsRetriever {

	public static final String SMS_EXTRA_NAME = "pdus";
	public static final String SMS_URI = "content://sms";

	public static final String ADDRESS = "address";
	public static final String PERSON = "person";
	public static final String DATE = "date";
	public static final String READ = "read";
	public static final String STATUS = "status";
	public static final String TYPE = "type";
	public static final String BODY = "body";
	public static final String SEEN = "seen";

	public static Context context = MyApplication.getAppContext();

	public static void readOldSms() {

		ArrayList<SMS> smsList = new ArrayList<SMS>();

		ContentResolver contentResolver = context.getContentResolver();
		Cursor cursor = contentResolver.query(Uri.parse("content://sms"), null,
				null, null, null);

		int indexBody = cursor.getColumnIndex(BODY);
		int indexAddr = cursor.getColumnIndex(ADDRESS);
		int indexDate = cursor.getColumnIndex(DATE);
		int indexType = cursor.getColumnIndex(TYPE);

		if (indexAddr < 0 || !cursor.moveToFirst())
			return;

		smsList.clear();

		do {
			String smsDate = null;
			try {
				String date = cursor.getString(indexDate);
				Long timestamp = Long.parseLong(date);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(timestamp);
				Date finaldate = (Date) calendar.getTime();
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss", Locale.US);
				smsDate = dateFormat.format(finaldate);
			} catch (Exception e) {
				Log.e("WIFISMS", e.toString());
			}

			SMS rev_sms = new SMS(smsDate, cursor.getString(indexBody),
					cursor.getString(indexAddr),
					ContactsRetriever.getContactId(cursor.getString(indexAddr),
							context), cursor.getString(indexType));

			smsList.add(rev_sms);

		} while (cursor.moveToNext());

		String message = "";
		try {
			message = "{\"conversations\":[";
			String conv;
			ArrayList<SMS> convList = new ArrayList<SMS>();
			while (!smsList.isEmpty()) {
				conv = "-1";
				for(SMS s : smsList){
					if(conv.equals("-1")){
						conv = s.getnumber();
						message += "{"
							+"\"number\":\""+s.getnumber()+"\","
							+"\"ID\":\""+s.getid()+"\","
							+"\"messages\":[";
					}
					if(s.getnumber().equals(conv)){
						message += "{\"dir\":\""+s.getType()+"\",\"body\":\""+s.getbody()+"\",\"time\":\""+s.getdate()+"\"},";
						convList.add(s);
					}
				}
				message = message.substring(0,message.length() - 1);
				message += "]},";
				for(SMS s : convList){
					smsList.remove(s);
				}
			}
			message = message.substring(0,message.length() - 1);
			message += "]}";
		} catch (Exception e) {
			Log.e("WIFISMS","message string error :"+e.toString());
		}

		
		FileRead.writeFile(context.getFilesDir().getPath() + File.separator
				+ "Data" + File.separator + "messages" + File.separator,
				"messages.json", message);
	}
}
