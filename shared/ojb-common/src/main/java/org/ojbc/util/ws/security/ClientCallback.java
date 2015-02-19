package org.ojbc.util.ws.security;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

import java.io.IOException;

/**
 * Simple password callback handler. This just checks if the password for the private key
 * is being requested, and if so sets that value.
 */
public class ClientCallback implements CallbackHandler
{
	
	private String certificateAlias;
	private String privateKeyPassword;
	
    public void handle(Callback[] callbacks) throws IOException {
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pwcb = (WSPasswordCallback)callbacks[i];
            String id = pwcb.getIdentifier();
            int usage = pwcb.getUsage();
            if (usage == WSPasswordCallback.DECRYPT || usage == WSPasswordCallback.SIGNATURE) {
                
                // used to retrieve password for private key
                if (certificateAlias.equals(id)) {
                    pwcb.setPassword(privateKeyPassword);
                }
                                
            }
        }
    }

	public String getCertificateAlias() {
		return certificateAlias;
	}

	public void setCertificateAlias(String certificateAlias) {
		this.certificateAlias = certificateAlias;
	}

	public String getPrivateKeyPassword() {
		return privateKeyPassword;
	}

	public void setPrivateKeyPassword(String privateKeyPassword) {
		this.privateKeyPassword = privateKeyPassword;
	}
}