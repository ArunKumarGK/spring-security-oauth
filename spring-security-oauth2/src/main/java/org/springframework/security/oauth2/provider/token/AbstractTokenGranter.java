/*
 * Copyright 2006-2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.security.oauth2.provider.token;

import java.util.Map;
import java.util.Set;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientCredentialsChecker;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenGranter;

/**
 * @author Dave Syer
 * 
 */
public abstract class AbstractTokenGranter implements TokenGranter {

	private final AuthorizationServerTokenServices tokenServices;

	private final ClientCredentialsChecker clientCredentialsChecker;

	private final String grantType;

	protected AbstractTokenGranter(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, String grantType) {
		this.grantType = grantType;
		this.clientCredentialsChecker = new ClientCredentialsChecker(clientDetailsService);
		this.tokenServices = tokenServices;
	}

	public OAuth2AccessToken grant(String grantType, Map<String, String> parameters, String clientId, Set<String> scopes) {

		if (!this.grantType.equals(grantType)) {
			return null;
		}

		AuthorizationRequest clientToken = clientCredentialsChecker.validateCredentials(grantType, clientId, scopes);

		return tokenServices.createAccessToken(getOAuth2Authentication(parameters, clientToken));

	}

	protected abstract OAuth2Authentication getOAuth2Authentication(Map<String, String> parameters,
			AuthorizationRequest clientToken);

}
