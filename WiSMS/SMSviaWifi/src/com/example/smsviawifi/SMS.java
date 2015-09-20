package com.example.smsviawifi;

public class SMS {
	String id;
	String body;
	String number;
	String date;
	String type;
	String name;
	String snippet;

	public SMS(String date, String body, String number, String id, String type, String snippet, String name) {
		this.body = body;
		this.number = number;
		this.date = date;
		this.id = id;
		this.type = type;
		this.name = name;
		this.snippet = snippet.substring(0,(snippet.length() < 20 ? snippet.length() : 20));
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

	public String getDetails() {

		return "Type : " + type + " ID : " + id + " Sender: " + number
				+ " Date : " + date + "\nBody: " + body;
	}
}
