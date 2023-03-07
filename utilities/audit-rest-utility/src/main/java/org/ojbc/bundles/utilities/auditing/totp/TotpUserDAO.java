package org.ojbc.bundles.utilities.auditing.totp;

import java.util.List;

import org.ojbc.util.model.TotpUser;

public interface TotpUserDAO {

	public Integer saveTotpUser(TotpUser totpUser);
	
	public Integer deleteTotpUserByUserName(String userName);
	
	public TotpUser getTotpUserByUserName(String userName);
	
	public List<TotpUser> retrieveAllTotpUsers();
	
}
