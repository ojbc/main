/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.web.portal.totp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.web.model.otp.OTPFormCommand;
import org.ojbc.web.portal.AppProperties;
import org.ojbc.web.portal.services.OTPService;
import org.ojbc.web.portal.services.SamlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Element;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

@Controller
@RequestMapping("/code")
@ConditionalOnProperty(name = "otpServiceBean", havingValue = "totpServiceMemoryImpl")
public class CodeController {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource
	SamlService samlService;

	@Autowired
    private GoogleAuthenticator gAuth;
	
	@Resource
    AppProperties appProperties;
	
	@Resource (name="${otpServiceBean:totpServiceMemoryImpl}")
	OTPService otpService;

    @GetMapping(value="/generate/{username}/{millisecond}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] generate(@PathVariable String username) 
    		throws WriterException, IOException {
        final GoogleAuthenticatorKey key = gAuth.createCredentials(username);

        //I've decided to generate QRCode on backend site
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(appProperties.getOtpAuthTotpURL(), username, key);

        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);
        
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
	    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    @GetMapping(value="/qrCode/{username}", produces = MediaType.IMAGE_PNG_VALUE)
    public String showQrCode(@PathVariable String username, Model model) 
    		throws WriterException, IOException {
    	model.addAttribute("userName", username);
    	return "otp/qrCode";
    }
    
	@GetMapping(value = "/inputForm")
	public String searchForm(HttpServletRequest request,
	        Map<String, Object> model) throws Exception {

		//We clear the authentication to force the portal details source method
		//to be invoked.  Otherwise it thinks pre-auth is complete.
		SecurityContextHolder.getContext().setAuthentication(null);
		
		OTPFormCommand otpFormCommand = new OTPFormCommand();
		model.put("otpFormCommand", otpFormCommand);
		
		model.put("otpInfoMessage", "Please enter the one-time password on your secondary token.");		
		
		return "otp/totpInputForm";
	}
	
    @PostMapping("/validate/key")
    public String validateKey(HttpServletRequest request, 
    		@ModelAttribute("otpFormCommand") OTPFormCommand otpFormCommand, 
    		Map<String, Object> model) throws Exception{
		String oneTimePassword = otpFormCommand.getOtpRequest().getOneTimePassword();

    	Element samlAssertion = samlService.getSamlAssertion(request);;
		String userEmail = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.EmailAddressText); 
		if (gAuth.authorizeUser(userEmail, Integer.valueOf(oneTimePassword)))
		{
			otpService.confirmOTP(userEmail, oneTimePassword);
			log.info("OTP confirmed for: " + userEmail);
			return "redirect:/";
		}	
		else
		{
			log.warn("OTP Denied for: " + userEmail);
			model.put("otpInfoMessageError", "Your One-Time Password was invalid.  Please try again or wait for the new One-Time Password.");
			return "otp/totpInputForm";	
		}

    }    
}