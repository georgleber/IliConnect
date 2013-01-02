package com.android.iliConnect.dataproviders;

import java.io.IOException;

import com.android.iliConnect.MainActivity;

import android.widget.Toast;

public class AuthException extends IOException {
	
	String error;
	
	public AuthException(String s) {
		super();
		this.error = s;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Login fehlgeschlagen: "+this.error;
	}

}
