package com.example.smsviawifi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.util.Log;

public class FileRead {

	public static Context context = MyApplication.getAppContext();

	public static String readFromFile(String file) {
		Log.i("WIFISMS", "readFromFile: " + file);

		String result = "", line;
		try {
			File f = new File(context.getFilesDir(), file);
			BufferedReader br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null) {
				result += line + "\n";
			}
			br.close();
		} catch (Exception e) {
			Log.e("WIFISMS", "File read error: " + e.toString());
		}
		return result;

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

	public static boolean writeFile(String path, String file, String data) {
		Log.i("WIFISMS", "FILE --> " + path+file);
		OutputStream out = null;
		try {
			File f = new File(path);
			f.mkdirs();
			f.createNewFile();
			out = new FileOutputStream(path+file);
			out.write(data.getBytes());
			out.flush();
			out.close();
			out = null;
			return true;
		} catch (Exception e) {
			Log.d("WIFISMS", "writeFile --> " + e.toString());
			e.printStackTrace();
			return false;
		}
	}

}
