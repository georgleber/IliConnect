package models;

public class Settings {
	public Authentification auth;
	public boolean sync;
	public int interval;
	public boolean sync_wlanonly;
	public int num_notifications;
	public int level_warning;
	public int level_critical;

	public Settings(){
		
	}
	
	public Authentification getAuth() {
		return auth;
	}

	public Settings(Authentification auth, boolean sync, int interval,
			boolean sync_wlanonly, int num_notifications, int level_warning,
			int level_critical) {
		super();
		this.auth = auth;
		this.sync = sync;
		this.interval = interval;
		this.sync_wlanonly = sync_wlanonly;
		this.num_notifications = num_notifications;
		this.level_warning = level_warning;
		this.level_critical = level_critical;
	}

}
