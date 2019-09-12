package com.soaint.encoder;

import java.util.Base64;

public class Coder {
	
	public static String codificador(String credentials) {
		
		String encodedName = Base64.getEncoder().encodeToString(credentials.getBytes());

		return encodedName;		
	}
}
