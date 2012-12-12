package com.android.iliConnect.dataproviders;

import java.io.IOException;

public class AuthException extends IOException {
	
	
	public AuthException() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {

		return "Login fehlgeschlagen.";
	}

}
