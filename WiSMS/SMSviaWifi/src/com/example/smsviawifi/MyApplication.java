package com.example.smsviawifi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class MyApplication extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		MyApplication.context = getApplicationContext();
		if (!PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getBoolean("installed", false)) {
			PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext())
					.edit().putBoolean("installed", true).commit();

			copyAssetFolder(context.getAssets(), "", context.getFilesDir()
					.getPath());
		}
		
	}

	private static boolean copyAssetFolder(AssetManager assetManager,
			String fromAssetPath, String toPath) {
		Log.i("WIFISMS", "FOLDER " + fromAssetPath + " -> " + toPath);
		try {
			// if(!fromAssetPath.equals("")) fromAssetPath =
			// fromAssetPath+File.separator;
			String[] files = assetManager.list(fromAssetPath);
			//Log.e("WIFISMS", "fromAssetPath --> " + fromAssetPath);
			Log.e("WIFISMS", "first file --> " + files[0]);
			new File(toPath).mkdirs();
			boolean res = true;
			for (String file : files) {
				// Log.d("WIFISMS", "--->> "+fromAssetPath+"/"+file);
				if (file.contains("."))
					res &= copyAsset(assetManager, fromAssetPath + "/" + file,
							toPath + "/" + file);
				else
					res &= copyAssetFolder(assetManager, file, toPath + "/"
							+ file);
			}
			return res;
		} catch (Exception e) {
			Log.d("WIFISMS", "copyAssetFolder " + e.toString());
			e.printStackTrace();
			return false;
		}
	}

	private static boolean copyAsset(AssetManager assetManager,
			String fromAssetPath, String toPath) {
		Log.i("WIFISMS", "FILE " + fromAssetPath + " -> " + toPath);
		InputStream in = null;
		OutputStream out = null;
		try {
			if (fromAssetPath.charAt(0) == '/')
				fromAssetPath = fromAssetPath.substring(1);
			in = assetManager.open(fromAssetPath);
			new File(toPath).createNewFile();
			out = new FileOutputStream(toPath);
			copyFile(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
			return true;
		} catch (Exception e) {
			Log.d("WIFISMS", "copyAsset --> " + e.toString());
			e.printStackTrace();
			return false;
		}
	}

	public static void copyFile(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	public static Context getAppContext() {
		return MyApplication.context;
	}

}