##STORK2.0 Jasig CAS 4.0.0 Authentication Handler

###Introduction

This is an example of a STORK 2.0 authentication handler for the [CAS 4.0.0 server](https://www.apereo.org/cas/download).

You can import the extension by adding it to the main pom.xml of the CAS server.

###cas-server-extension-ist

The pom.xml of the extension already has the necessary dependencies for the STORK
SAMLEngine.

The SamlResponseAuthenticationHandler class authenticates a StorkCredential using
the eIdentifier as a principal. No validation against any other database is done,
meaning that the SAMLResponse acts as a capacity. The SAMLResponse is validated,
and the isFail() field of the retrieved AuthnResponse is verified to check if the
authentication of the user at his IdP was successful.

If you wish to use another retrieved attribute as the principal, modify the StorkCredential
class to return it in the overridden getId() method.

To validate some attribute to some local DB (e.g.: an LDAP server), you can do 
it programmatically in the SamlResponseAuthenticationHandler class, but no example
is provided.

###Cas-server-webapp

Only the necessary modified configuration files are presented as an example.
These configurations are not suited for a production environment for CAS and
are to be used as reference only.

The webapp resources folder contains the necessary configuration files for the
SAMLEngine. The stork.properties file contains your country specific configurations
to be set in a SAML Request initiated with the SSO Profile to your national PEPS.

The StorkSamlRequestAction uses this configuration to construct the SAML Request.
A country selection page example is not provided, but if you want one please report an
issue and I'll push one to the repository.



