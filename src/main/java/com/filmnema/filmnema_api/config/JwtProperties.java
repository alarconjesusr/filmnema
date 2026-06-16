package com.filmnema.filmnema_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

	private String secret = "c3VwZXItc2VjdXJlLWZpbG1uZW1hLWFwaS1qd3Qtc2VjcmV0LWtleS0zMjM0NTY3ODkwMTIzNDU2Nzg5MA==";
	private long expirationMinutes = 60L;
	private String issuer = "filmnema-api";

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getExpirationMinutes() {
		return expirationMinutes;
	}

	public void setExpirationMinutes(long expirationMinutes) {
		this.expirationMinutes = expirationMinutes;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
}