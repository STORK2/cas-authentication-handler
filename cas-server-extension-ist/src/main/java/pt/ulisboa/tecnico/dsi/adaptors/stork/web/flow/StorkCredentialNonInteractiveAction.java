package pt.ulisboa.tecnico.dsi.adaptors.stork.web.flow;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.web.flow.AbstractNonInteractiveCredentialsAction;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.execution.RequestContext;

import pt.ulisboa.tecnico.dsi.adaptors.stork.authentication.principal.StorkCredential;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;



public final class StorkCredentialNonInteractiveAction extends AbstractNonInteractiveCredentialsAction{

	private final static String SAML_RESPONSE_PARAMETER = "SAMLResponse";
	private final static String SAML_ENGINE_INSTANCE = "SP";

	@Override
	protected Credential constructCredentialsFromRequest(RequestContext context) {
		
		final HttpServletRequest request = WebUtils.getHttpServletRequest(context);

		String samlResponse = request.getParameter(SAML_RESPONSE_PARAMETER);
		String remoteHostIP = request.getRemoteAddr();
		
		StorkCredential storkCredential = new StorkCredential();

		storkCredential.setSamlResponse(samlResponse);
		
		STORKAuthnResponse authnResponse = validateSamlResponseAndRetrieveAuthnResponse(samlResponse, remoteHostIP);
		
		storkCredential.setAuthnResponse(authnResponse);
		
		logger.info("Stork Credentials successfully retrieved");
		return storkCredential;

	}

	
	/**
	 * The SAMLEngine validates the SAMLReponse (checking it's signature) and returns the STORKAuthnResponse object.
	 * This structure contains information about the credentials/user attributes and if the authentication at the 
	 * IdP was successful.  
	 * @return STORKAuthnResponse
	 */
	private STORKAuthnResponse validateSamlResponseAndRetrieveAuthnResponse(String samlResponse, String remoteHostIP){
		
		STORKSAMLEngine samlEngine = STORKSAMLEngine.getInstance(SAML_ENGINE_INSTANCE);

		STORKAuthnResponse authnResponse = null;
		byte[] decSamlToken = PEPSUtil.decodeSAMLToken(samlResponse);

		try{
			authnResponse = samlEngine.validateSTORKAuthnResponse(decSamlToken, remoteHostIP);
		}catch (STORKSAMLEngineException e){
			System.out.println("STORKSAMLEngineException Caught: " + e.getMessage());
			logger.error("SamlEngine exception caught: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return authnResponse;
	}

}
