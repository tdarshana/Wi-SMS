package com.example.smsviawifi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import android.util.Base64OutputStream;
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

				Log.i("WIFISMS", "sending -> " + inps[1].substring(1));

				if (!inps[1].substring(1).contains("png")
						&& !inps[1].substring(1).contains("jpg")) {
					Log.i("WIFISMS", " Sending TEXT");
					PrintWriter out = new PrintWriter(Sock.getOutputStream());
					out.println("HTTP/1.1 200 OK");
					out.println("Content-Type: text/html; charset=UTF-8\n\r\n\r");
					String ss = FileRead.readFromFile(inps[1].substring(1));
					out.print(ss);
					out.flush();
				} else {
					Log.i("WIFISMS", " Sending DATA");
					OutputStream outStream = Sock.getOutputStream();
					InputStream inStream = FileRead
							.getInputStreamForFile(inps[1].substring(1));
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

	public static void encode(InputStream is, OutputStream base64OutputStream) {
		OutputStream out = new Base64OutputStream(base64OutputStream, 0);
		try {
			MyApplication.copyFile(is, out);
			is.close();
			out.close();
		} catch (Exception e) {
			Log.d("WIFISMS", e.toString());
		}
	}
}
