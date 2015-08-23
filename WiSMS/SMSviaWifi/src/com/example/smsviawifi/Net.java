package com.example.smsviawifi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import android.util.Log;

public class Net extends Thread {

	private ServerSocket ServSock = null;

	public Net() {
		try {
			ServSock = new ServerSocket(8080);
		} catch (IOException e) {
			Log.d("WIFISMS", e.toString());
		}
	}

	@Override
	public void run() {
		Socket Sock = null;
		String inp = "";
		while (true) {
			// i++;
			try {
				Sock = ServSock.accept();
				Log.d("WIFISMS", "connected...");

				Scanner in = new Scanner(Sock.getInputStream());
				if (in.hasNextLine())
					inp = in.nextLine();
				String[] inps = inp.split(" ");

				String path = inps[1].substring(1);

				Log.i("WIFISMS", "sending -> " + path);

				if (path.equals(""))
					path = "index.html";

				if (!path.contains("png") && !path.contains("jpg")
						&& !path.contains("ico")) {
					Log.i("WIFISMS", " Sending TEXT");
					PrintWriter out = new PrintWriter(Sock.getOutputStream());
					out.println("HTTP/1.1 200 OK");
					out.println("Content-Type: text/html; charset=UTF-8\n\r\n\r");
					String ss = FileRead.readFromFile(path);
					out.print(ss);
					out.flush();
				} else {
					Log.i("WIFISMS", " Sending DATA");
					OutputStream outStream = Sock.getOutputStream();
					InputStream inStream = FileRead.getInputStreamForFile(path);
					MyApplication.copyFile(inStream, outStream);
					inStream.close();
					outStream.close();
				}

				in.close();
				Log.d("WIFISMS", "sending done...");
			} catch (IOException e) {
				Log.d("WIFISMS", e.toString());
			} finally {
				if (Sock != null) {
					try {
						Sock.close();
					} catch (IOException e) {
						Log.d("WIFISMS", e.toString());
					}
				}
			}
		}
	}
}
