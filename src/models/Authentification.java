package models;

public class Authentification {
	public boolean autologin;
	public String user_id;
	public String password;
	public String url_src = "http://www.recruitment-specialist.de/xml/RemoteData.xml";

	public Authentification(){
		
	}
	
	public Authentification(boolean autologin, String user_id, String password) {
		super();
		this.autologin = autologin;
		this.user_id = user_id;
		this.password = password;
		
	}

	
}
