package com.example.smsviawifi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;

public class FileRead {

	public static Context context = MyApplication.getAppContext();

	public static void writeToFile(String data, String file) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					context.openFileOutput(file, Context.MODE_PRIVATE));
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		} catch (IOException e) {
			Log.e("WIFISMS", "File write failed: " + e.toString());
		}
	}

	public static String readFromFile(String file) {
		Log.i("WIFISMS", "readFromFile: " + file);
		String ret = "";

		try {
			InputStream inputStream = context.getAssets().open(file);
			Log.i("WIFISMS", "readFromFile: step check");
			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString).append("\n");
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e("WIFISMS", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("WIFISMS", "Can not read file: " + e.toString());
		}

		return ret;
	}

	public static InputStream getInputStreamForFile(String file) {
		InputStream inputStream = null;
		try {
			inputStream = context.getAssets().open(file);
		} catch (FileNotFoundException e) {
			Log.e("WIFISMS", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("WIFISMS", "IOError: " + e.toString());
		}
		return inputStream;
	}

}
