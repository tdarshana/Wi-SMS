package com.example.smsviawifi;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	Net n = new Net();

	public static Context context = MyApplication.getAppContext();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView t = (TextView) findViewById(R.id.textview1);
		t.setText("To start connect to wifi hotspot and press the button.");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		findViewById(R.id.toggleButton1).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (((ToggleButton) v).isChecked()) {
							TextView t = (TextView) findViewById(R.id.textview1);

							String ip = wifiIpAddress(context);
							if (ip == null) {
								t.setText("No wifi connection !");
							} else {

								t.setText("Loading Contacts...");
								ContactsRetriever.readContacts();
								t.setText("Loading Previous Messages...");
								OldSmsRetriever.readOldSms();
								n.start();

								t.setText("Goto http://" + ip + ":8080");
							}
						} else {
							Net.go = false;
							try {
								n.join();
							} catch (InterruptedException e) {
								Log.d("WIFISMS", e.toString());
							}
							TextView t = (TextView) findViewById(R.id.textview1);
							t.setText("Stopped");
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onDestroy() {
		// n.terminate();
		try {
			n.join();
		} catch (InterruptedException e) {
			Log.d("WIFISMS", e.toString());
		}
		Log.d("WIFISMS", "Main Terminated");

		super.onDestroy();
	}

	protected String wifiIpAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(WIFI_SERVICE);
		int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

		// Convert little-endian to big-endianif needed
		if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
			ipAddress = Integer.reverseBytes(ipAddress);
		}

		byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

		String ipAddressString;
		try {
			ipAddressString = InetAddress.getByAddress(ipByteArray)
					.getHostAddress();
		} catch (UnknownHostException ex) {
			Log.e("WIFISMS", "Unable to get host address.");
			ipAddressString = null;
		}

		return ipAddressString;
	}

}
