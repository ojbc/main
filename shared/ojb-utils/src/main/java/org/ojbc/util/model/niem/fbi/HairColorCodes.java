package org.ojbc.util.model.niem.fbi;

public enum HairColorCodes {
	BLK,BLN,BLU,BRO,GRN,GRY,ONG,PLE,PNK,RED,SDY,WHI,XXX;
	
	public static boolean contains(String test) {

	    for (HairColorCodes code : HairColorCodes.values()) {
	        if (code.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}

	
}
