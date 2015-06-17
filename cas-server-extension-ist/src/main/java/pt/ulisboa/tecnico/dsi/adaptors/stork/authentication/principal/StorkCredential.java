package pt.ulisboa.tecnico.dsi.adaptors.stork.authentication.principal;

import java.io.Serializable;

import org.jasig.cas.authentication.Credential;

import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.STORKAuthnResponse;

public class StorkCredential implements Credential, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final String STORK_IDENTIFIER = "eIdentifier"; 

	private String username = null;

	private String SamlResponse;
	private STORKAuthnResponse authnResponse;
	
	public StorkCredential() {}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getSamlResponse() {
		return SamlResponse;
	}

	public void setSamlResponse(String samlResponse) {
		SamlResponse = samlResponse;
	}

	public STORKAuthnResponse getAuthnResponse() {
		return authnResponse;
	}

	public void setAuthnResponse(STORKAuthnResponse authnResponse) {
		this.authnResponse = authnResponse;
	}

	@Override
	public String getId() {

		return username;

	}

}
