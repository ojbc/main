package org.ojb.web.portal.velocity;

/**
 * Custom view resolver is being used to allow Velocity Tools functionality to
 * be used, to provide access to the Velocity ImportTool - which is being used
 * in the portal to display external urls.
 * @deprecated It's not used currently.
 */
public class VelocityViewResolver extends
		org.springframework.web.servlet.view.velocity.VelocityViewResolver {
	
	public VelocityViewResolver() {
		
		setViewClass(OjbVelocityToolboxView.class);
		setSuffix(".vm");
	}
}
