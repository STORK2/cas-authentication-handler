package pt.ulisboa.tecnico.dsi.adaptors.stork.web.flow;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.PersonalAttributeList;
import eu.stork.peps.auth.commons.STORKAuthnRequest;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;

public final class StorkSamlRequestAction extends AbstractAction {

	//CONSTANTS
	private final static String STORK_PROPERTIES_FILE = "stork.properties";
	private final static String STORK_ISSUER_PROPERTY = "stork.issuer";
	private final static String STORK_SP_COUNTRY_PROPERTY = "stork.country";
	private final static String STORK_QAA_LEVEL_PROPERTY = "stork.qaalevel";
	private final static String STORK_RETURN_URL_PROPERTY = "stork.return"; 
	private final static String STORK_PEPS_URL_PROPERTY = "stork.pepsurl";
	private final static String STORK_SP_NAME_PROPERTY = "stork.name";
	private final static String STORK_SP_ID_PROPERTY = "stork.id";
	private final static String STORK_SP_SECTOR_PROPERTY = "stork.sector";
	private final static String STORK_SP_APPLICATION_PROPERTY = "stork.application";
	private final static String STORK_SP_INSTITUTION_PROPERTY = "stork.institution";
	private final static String SAML_ENGINE_INSTANCE = "SP";

	//Variables
	private String citizenCountry;
	private String samlRequest;

	@Override
	protected Event doExecute(RequestContext context) throws Exception {

		//Read properties file

		Properties storkProperties = new Properties();
		InputStream in = StorkSamlRequestAction.class.getClassLoader().getResourceAsStream(STORK_PROPERTIES_FILE);
		storkProperties.load(in);
		in.close();

		//Get citizenCountry from request
		citizenCountry = context.getRequestParameters().get("citizenCountry");

		//Create and fill auth request
		STORKAuthnRequest authnRequest = new STORKAuthnRequest();

		authnRequest.setDestination(storkProperties.getProperty(STORK_PEPS_URL_PROPERTY));
		authnRequest.setProviderName(storkProperties.getProperty(STORK_SP_NAME_PROPERTY));	
		authnRequest.setQaa(Integer.parseInt(storkProperties.getProperty(STORK_QAA_LEVEL_PROPERTY)));
		authnRequest.setAssertionConsumerServiceURL(storkProperties.getProperty(STORK_RETURN_URL_PROPERTY));
		authnRequest.setSpSector(storkProperties.getProperty(STORK_SP_SECTOR_PROPERTY));
		authnRequest.setSpInstitution(storkProperties.getProperty(STORK_SP_INSTITUTION_PROPERTY));
		authnRequest.setSpApplication(storkProperties.getProperty(STORK_SP_APPLICATION_PROPERTY));
		authnRequest.setSpCountry(storkProperties.getProperty(STORK_SP_COUNTRY_PROPERTY));
		authnRequest.setSPID(storkProperties.getProperty(STORK_SP_ID_PROPERTY));
		authnRequest.setIssuer(storkProperties.getProperty(STORK_ISSUER_PROPERTY));

		authnRequest.setCitizenCountryCode(citizenCountry);

		//Fill personal attributes to request
		IPersonalAttributeList pAttrList = new PersonalAttributeList();

		PersonalAttribute attr = new PersonalAttribute();
		attr.setName("eIdentifier");
		attr.setIsRequired(true);
		pAttrList.add(attr);

		attr = new PersonalAttribute();
		attr.setName("givenName");
		attr.setIsRequired(true);
		pAttrList.add(attr);

		attr = new PersonalAttribute();
		attr.setName("surname");
		attr.setIsRequired(true);
		pAttrList.add(attr);

		authnRequest.setPersonalAttributeList(pAttrList);

		//SAML Engine
		try{
			STORKSAMLEngine engine = STORKSAMLEngine.getInstance(SAML_ENGINE_INSTANCE);
			authnRequest = engine.generateSTORKAuthnRequest(authnRequest);
		}catch(STORKSAMLEngineException e){
			e.printStackTrace();
			return error();
		}

		byte[] token = authnRequest.getTokenSaml();

		samlRequest = PEPSUtil.encodeSAMLToken(token);

		final HttpServletRequest request = WebUtils.getHttpServletRequest(context);

		request.setAttribute("samlRequest", samlRequest);
		request.setAttribute("pepsURL", storkProperties.getProperty(STORK_PEPS_URL_PROPERTY));
		//request.setAttribute("country", citizenCountry);
		request.setAttribute("country", "LOCAL");

		return success();

	}

}
