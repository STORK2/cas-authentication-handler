package pt.ulisboa.tecnico.dsi.adaptors.stork.authentication.handler;

import java.security.GeneralSecurityException;
import java.util.Map;

import javax.security.auth.login.FailedLoginException;

import org.jasig.cas.authentication.AbstractAuthenticationHandler;
import org.jasig.cas.authentication.BasicCredentialMetaData;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.CredentialMetaData;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.principal.SimplePrincipal;

import pt.ulisboa.tecnico.dsi.adaptors.stork.authentication.principal.StorkCredential;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.PersonalAttributeList;
import eu.stork.peps.auth.commons.STORKAuthnResponse;

/**
 * 
 * @author Simon Esposito
 *
 */
public class SamlResponseAuthenticationHandler extends AbstractAuthenticationHandler{
	
	private final String COURSE_NAME = "STORK101"; 

	@Override
	public HandlerResult authenticate(Credential credential)
			throws GeneralSecurityException, PreventedException {

		StorkCredential storkCredential = (StorkCredential) credential;
		STORKAuthnResponse authnResponse = storkCredential.getAuthnResponse();
		String username = null;

		if(authnResponse.isFail()){
			throw new FailedLoginException("Invalid STORK credentials. The authentication at the IdM failed");
		}

		//Get the principle of the authentication
		username = storkCredential.getId();
		
		PersonalAttributeList personalAttrList = (PersonalAttributeList) authnResponse.getPersonalAttributeList();

		PersonalAttribute personalAttrIsStudent = personalAttrList.get("isStudent");
		
		if(personalAttrIsStudent == null){
			throw new FailedLoginException("isStudent attribute is not available");
		}
		
		Map<String, String> isStudentAttr = personalAttrIsStudent.getComplexValue();
		String course = isStudentAttr.get("course");
			
		if(!course.equals(COURSE_NAME)){
			System.out.println("isStudent Course Value: " + course);
			throw new FailedLoginException("isStudent attribute value is: "+course+", expected is: " + COURSE_NAME);
		}
			
		SimplePrincipal principal = new SimplePrincipal(username);

		storkCredential.setUsername(username);
		CredentialMetaData credentialMetaData = new BasicCredentialMetaData(storkCredential);
		HandlerResult handlerResult = new HandlerResult(this, credentialMetaData, principal);

		return handlerResult;
	}
	

	@Override
	public boolean supports(Credential credential) {
		return credential instanceof StorkCredential;
	}

}
