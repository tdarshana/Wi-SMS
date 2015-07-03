package com.example.smsviawifi;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import android.util.Log;

public class Net extends Thread{
	
	private ServerSocket ServSock = null;
	
	public Net(){
		try {
			ServSock = new ServerSocket(8080);
		} catch (IOException e) {
			Log.d("WIFISMS", e.toString());
		}
	}
	
	@Override
	public void run() {
		Socket Sock = null;
		int i = 0;
		String inp = "";
		while(true){
			i++;
			try {
				Sock = ServSock.accept();
				Log.d("WIFISMS", "connected...");
				PrintWriter out = new PrintWriter(Sock.getOutputStream());
				Scanner in = new Scanner(Sock.getInputStream());
				if(in.hasNextLine()) inp = in.nextLine();
				out.println("HTTP/1.1 200 OK");
				out.println("Content-Type: text/html\r\n\r\n");
				out.println("<html><head><title>Test</title></head><body>");
				out.println("<h1>Test on Android "+i+inp+"</h1>");
				out.println("</body></html>");
				out.flush();
				in.close();
				Log.d("WIFISMS", "sending done...");
			} catch (IOException e) {
				Log.d("WIFISMS", e.toString());
			} finally {
				if(Sock!=null){
					try {
						Sock.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.d("WIFISMS", e.toString());
					}
				}
			}
		}
	}
}
