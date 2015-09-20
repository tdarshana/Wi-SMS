package com.example.smsviawifi;

import java.util.ArrayList;

public class SMS {
	
	public static ArrayList<SMS> smsList = new ArrayList<SMS>();
	
	String id;
	String body;
	String number;
	String date;
	String type; //sent or received
	String name;
	String snippet;
	String read;

	public SMS(String date, String body, String number, String id, String type, String snippet, String name, String read) {
		this.body = body;
		this.number = number;
		this.date = date;
		this.id = id;
		this.type = type;
		this.name = name;
		this.snippet = snippet.substring(0,(snippet.length() < 20 ? snippet.length() : 20));
		this.read = read;
	}

	public String getid() {
		return id;
	}
	
	public String getname() {
		return name;
	}

	public String getbody() {
		return body;
	}

	public String getnumber() {
		return number;
	}

	public String getdate() {
		return date;
	}
	
	public String getType() {
		return type;
	}
	
	public String getSnippet() {
		return snippet;
	}
	
	public String getRead() {
		return read;
	}

	public String getDetails() {

		return "Type : " + type + " ID : " + id + " Sender: " + number
				+ " Date : " + date + "\nBody: " + body;
	}
}
