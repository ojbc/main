package org.ojbc.util.model.niem.fbi;

public enum EyeColorCodes {

	BLK, BLU,BRO,GRN,GRY,HAZ,MAR,MUL,PNK,XXX;
	
	public static boolean contains(String test) {

	    for (EyeColorCodes code : EyeColorCodes.values()) {
	        if (code.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}

}
