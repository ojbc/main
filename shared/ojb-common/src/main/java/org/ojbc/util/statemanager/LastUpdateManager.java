package org.ojbc.util.statemanager;

public interface LastUpdateManager {

	public String getLastUpdateTime();
	public void saveLastUpdateTime(String lastUpdateArgument);
}
