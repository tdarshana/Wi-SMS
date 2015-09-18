package com.example.smsviawifi;

import java.io.File;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

public class ContactsRetriever {

	public static Context context = MyApplication.getAppContext();

	static ContentResolver cr = context.getContentResolver();

	public static void readContacts() {

		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		String allContacts = "{\"contacts\" :[\n";

		if (cur.getCount() > 0) {

			while (cur.moveToNext()) {

				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));

				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

					// Log.i("WIFISMS","name : " + name + ", ID : " + id);
					allContacts = allContacts + "{\n" + "\"ID\":\"" + id
							+ "\",\n" + "\"name\":\"" + name + "\",\n"
							+ "\"numbers\":[";

					// get the phone number
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					
					
					boolean noNum=true;
					while (pCur.moveToNext()) {
						String phone = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						// Log.i("WIFISMS","phone" + phone);
						allContacts = allContacts + "\"" + phone + "\",";
						noNum=false;
					}
					
					pCur.close();
					if(noNum) allContacts = allContacts + "-";
					allContacts = allContacts.substring(0,
							allContacts.length() - 1);

					allContacts = allContacts + "]\n" + "}";
					allContacts = allContacts + ",";

				}
			}

			allContacts = allContacts.substring(0, allContacts.length() - 1)
					+ "]}";
		}

		FileRead.writeFile(context.getFilesDir().getPath() + File.separator
				+ "Data" + File.separator + "contacts" + File.separator,
				"contacts-list.json", allContacts);

	}

	public static String getContactId(String phoneNumber, Context context) {
		ContentResolver mResolver = context.getContentResolver();

		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));

		Cursor cursor = mResolver.query(uri, new String[] {
				PhoneLookup.DISPLAY_NAME, PhoneLookup._ID }, null, null, null);

		String contactId = "";

		if (cursor.moveToFirst()) {
			do {
				contactId = cursor.getString(cursor
						.getColumnIndex(PhoneLookup._ID));
			} while (cursor.moveToNext());
		}

		cursor.close();
		cursor = null;
		return contactId;
	}
	public static String getContactName(String phoneNumber, Context context) {
		ContentResolver mResolver = context.getContentResolver();

		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));

		Cursor cursor = mResolver.query(uri, new String[] {
				PhoneLookup.DISPLAY_NAME, PhoneLookup._ID }, null, null, null);

		String contactId = "";

		if (cursor.moveToFirst()) {
			do {
				contactId = cursor.getString(cursor
						.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			} while (cursor.moveToNext());
		}

		cursor.close();
		cursor = null;
		return contactId;
	}

}
