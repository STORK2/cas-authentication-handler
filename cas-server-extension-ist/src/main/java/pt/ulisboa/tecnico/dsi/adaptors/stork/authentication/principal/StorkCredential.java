package pt.ulisboa.tecnico.dsi.adaptors.stork.authentication.principal;

import java.io.Serializable;

import org.jasig.cas.authentication.Credential;

import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.STORKAuthnResponse;

public class StorkCredential implements Credential, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final String STORK_IDENTIFIER = "eIdentifier"; 

	private String SamlResponse;
	private STORKAuthnResponse authnResponse;
	
	public StorkCredential() {}
	
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
		
		PersonalAttribute attr;
		
		attr = authnResponse.getPersonalAttributeList().get(STORK_IDENTIFIER);
		if(attr != null){
			return attr.getValue().get(0);
		}
		
		return null;
	}

}
