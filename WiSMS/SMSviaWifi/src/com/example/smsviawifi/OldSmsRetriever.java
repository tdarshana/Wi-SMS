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

public class OldSmsRetriever extends Thread {

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
	public static final String SNIPPET = "snippet";

	public static Context context = MyApplication.getAppContext();

	public static void readOldSms() {

		Log.d("WIFISMS", "Starting reading old sms.");

		ContentResolver contentResolver = context.getContentResolver();
		Cursor cursor = contentResolver.query(Uri.parse("content://sms"), null,
				null, null, null);

		int indexBody = cursor.getColumnIndex(BODY);
		int indexAddr = cursor.getColumnIndex(ADDRESS);
		int indexDate = cursor.getColumnIndex(DATE);
		int indexType = cursor.getColumnIndex(TYPE);
		int indexRead = cursor.getColumnIndex(READ);

		if (indexAddr < 0 || !cursor.moveToFirst())
			return;

		SMS.smsList.clear();

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
							context), cursor.getString(indexType),
					cursor.getString(indexBody),
					ContactsRetriever.getContactName(
							cursor.getString(indexAddr), context),
					cursor.getString(indexRead));

			SMS.smsList.add(rev_sms);

		} while (cursor.moveToNext());

		Log.d("WIFISMS", "All sms loaded.");

		update_message_files();
		
		String newmsg = "{\"newmsg\":\"0\"}";
		
		FileRead.writeFile(context.getFilesDir().getPath() + File.separator
				+ "Data" + File.separator + "messages" + File.separator,
				"newmsg.json", newmsg);

		cursor.close();
		synchronized (MainActivity.lock) {
			MainActivity.messagesLoaded = true;
		}
	}

	public static void update_message_files() {
		String message = "";
		String message_list = "";
		try {
			message_list = "{\"conversations\":[";
			String conv;
			// int count;
			// ArrayList<SMS> convList = new ArrayList<SMS>();
			ArrayList<String> loadedCovs = new ArrayList<String>();
			boolean doneLoading = false;

			while (!doneLoading) {
				conv = "-1";
				// count = 30;
				String ID = "";
				for (SMS s : SMS.smsList) {
					String tmp = s.getnumber();
					if (!loadedCovs.contains(tmp)) {
						if (conv.equals("-1")) {
							conv = s.getnumber();

							message += "{"
									+ "\"number\":\""
									+ s.getnumber().replaceAll("-", "")
									+ "\","
									+ "\"ID\":\""
									+ (s.getid() == "" ? s.getnumber() : s
											.getid())
									+ "\","
									+ "\"name\":\""
									+ (s.getname() == "" ? s.getnumber() : s
											.getname()) + "\","
									+ "\"messages\":[";

							message_list += "{"
									+ "\"number\":\""
									+ s.getnumber().replaceAll("-", "")
									+ "\","
									+ "\"ID\":\""
									+ (s.getid() == "" ? s.getnumber() : s
											.getid())
									+ "\","
									+ "\"name\":\""
									+ (s.getname() == "" ? s.getnumber() : s
											.getname())
									+ "\","
									+ "\"message\":\""
									+ s.getSnippet().replaceAll("\"", "\\\\\"")
											.replaceAll("\n", "\\\\n") + "\","
									+ "\"read\":\"" + s.getRead() + "\"";

							ID = (s.getid() == "" ? s.getnumber() : s.getid());
						}
						if (s.getnumber() == null) {
							// if (count > 0)
							message += "{\"dir\":\""
									+ s.getType()
									+ "\",\"body\":\""
									+ s.getbody().replaceAll("\"", "\\\\\"")
											.replaceAll("\n", "\\\\n")
									+ "\",\"time\":\"" + s.getdate() + "\"},";
							// convList.add(s);
							// count--;
						} else if (s.getnumber().equals(conv)) {
							// if (count > 0)
							message += "{\"dir\":\""
									+ s.getType()
									+ "\",\"body\":\""
									+ s.getbody().replaceAll("\"", "\\\\\"")
											.replaceAll("\n", "\\\\n")
									+ "\",\"time\":\"" + s.getdate() + "\"},";
							// convList.add(s);
							// count--;
						}
					}

				}

				if (conv.equals("-1")) {
					doneLoading = true;
				} else {
					loadedCovs.add(conv);
					Log.e("WIFISMS", "loaded conv :" + conv);
				}
				
				message = message.substring(0, message.length() - 1);
				message += "]},";
				message_list += "},";

				message = message.substring(0, message.length() - 1);
				FileRead.writeFile(context.getFilesDir().getPath()
						+ File.separator + "Data" + File.separator + "messages"
						+ File.separator, "messages-" + ID + ".json", message);
				message = "";

			}
			
			
		} catch (Exception e) {
			Log.e("WIFISMS", "message string error :" + e.toString());
		}

		message_list = message_list.substring(0, message_list.length() - 1);
		message_list += "]}";
		
		FileRead.writeFile(context.getFilesDir().getPath() + File.separator
				+ "Data" + File.separator + "messages" + File.separator,
				"messages-list.json", message_list);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		readOldSms();
	}

}
