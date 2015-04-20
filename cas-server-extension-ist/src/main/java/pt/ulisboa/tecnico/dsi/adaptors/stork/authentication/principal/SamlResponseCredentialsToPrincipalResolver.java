package pt.ulisboa.tecnico.dsi.adaptors.stork.authentication.principal;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.PrincipalResolver;
import org.jasig.cas.authentication.principal.SimplePrincipal;



public class SamlResponseCredentialsToPrincipalResolver implements PrincipalResolver{

	@Override
	public Principal resolve(Credential credential) {
		
		StorkCredential storkCredential = (StorkCredential) credential;
		
		SimplePrincipal principal = new SimplePrincipal(storkCredential.getId());
		
		return principal;
	}

	@Override
	public boolean supports(Credential credential) {
		return credential instanceof StorkCredential;
	}

}
