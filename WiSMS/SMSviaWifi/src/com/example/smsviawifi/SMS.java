package com.example.smsviawifi;

public class SMS {
	String id;
	String body;
	String number;
	String date;
	String type;

	public SMS(String date, String body, String number, String id, String type) {
		this.body = body;
		this.number = number;
		this.date = date;
		this.id = id;
		this.type = type;
	}

	public String getid() {
		return id;
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

	public String getDetails() {

		return "Type : " + type + " ID : " + id + " Sender: " + number
				+ " Date : " + date + "\nBody: " + body;
	}
}
