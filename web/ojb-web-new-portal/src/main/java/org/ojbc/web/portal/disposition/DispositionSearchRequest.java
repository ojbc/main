package org.ojbc.web.portal.disposition;

import java.time.LocalDate;

public class DispositionSearchRequest {

	private LocalDate dispositionStartDate; 
	private LocalDate dispositionEndDate;
	
	public LocalDate getDispositionStartDate() {
		return dispositionStartDate;
	}
	public void setDispositionStartDate(LocalDate dispositionStartDate) {
		this.dispositionStartDate = dispositionStartDate;
	}
	public LocalDate getDispositionEndDate() {
		return dispositionEndDate;
	}
	public void setDispositionEndDate(LocalDate dispositionEndDate) {
		this.dispositionEndDate = dispositionEndDate;
	} 
}