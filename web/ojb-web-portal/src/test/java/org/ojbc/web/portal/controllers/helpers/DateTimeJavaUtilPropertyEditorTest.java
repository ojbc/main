package org.ojbc.web.portal.controllers.helpers;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DateTimeJavaUtilPropertyEditorTest {

	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	@Test
	public void testDate() {

		DateTimeJavaUtilPropertyEditor dEditor = new DateTimeJavaUtilPropertyEditor();

		dEditor.setAsText("01/04/2014");
		Date d = (Date) dEditor.getValue();
		
		String sDate = sdf.format(d);
		assertEquals("01/04/2014", sDate);
	}
}
