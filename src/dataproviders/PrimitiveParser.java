package dataproviders;
public class PrimitiveParser {

	/**
	 * Klasse zum einfachen Parsen (inkl. Fehlerbehandlung) von primitiven Typen.
	 * 
	 *  boolean
		byte
		char
		double
		float
		int
		long
		short
	 */
	
	public boolean parseBoolean(String value) {
		boolean result = false;
		try {
			result = Boolean.parseBoolean(value);
		}catch(Exception ex) {}
		return result;
	}
	
	public byte parseByte(String value) {
		byte result = 0;
		try {
			result = Byte.parseByte(value);
		}catch(Exception ex) {}
		return result;
	}
	
	public char parseChar(String value) {
		char result = ' ';
		try {
			if(value.length() != 1)
				throw new Exception("Ungültiger Char-Wert: " + value);
			
			result = value.charAt(0);
		}catch(Exception ex) {}
		return result;
	}
	
	public double parseDouble(String value) {
		double result = 0.0;
		try {
			result = Double.parseDouble(value.replace(",","."));
		}catch(Exception ex) {}
		return result;
	}
	
	public float parseFloat(String value) {
		float result = 0.0f;
		try {
			result = Float.parseFloat(value);
		}catch(Exception ex) {}
		return result;
	}
	
	public int parseInt(String value) {
		int result = 0;
		try {
			result = Integer.parseInt(value);
		}catch(Exception ex) {}
		return result;
	}
	
	public long parseLong(String value) {
		long result = 0;
		try {
			result = Long.parseLong(value);
		}catch(Exception ex) {}
		return result;
	}
	
	public short parseShort(String value) {
		short result = 0;
		try {
			result = Short.parseShort(value);
		}catch(Exception ex) {}
		return result;
	}
}
