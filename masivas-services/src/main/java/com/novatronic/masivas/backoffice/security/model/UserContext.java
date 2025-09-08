package com.novatronic.masivas.backoffice.security.model;

import io.micrometer.common.util.StringUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Obi Consulting
 */
public class UserContext implements Serializable, UserDetails {

    private final String username;
    private final List<String> authorities;
    private final Map<String, Serializable> attributes;
    private final int expirationMinute;
    private final String scaToken;
    private final String responseCode;
    private final String scaProfile;
    private final String ip;
    private String token;

    private UserContext(String username,
            List<String> authorities,
            Map<String, Serializable> attributes,
            int expirationMinute,
            String scaToken,
            String responseCode,
            String scaProfile,
            String ip,
            String token
    ) {
        this.username = username;
        this.authorities = authorities;
        this.attributes = attributes;
        this.expirationMinute = expirationMinute;
        this.scaToken = scaToken;
        this.responseCode = responseCode;
        this.scaProfile = scaProfile;
        this.ip = ip;
        this.token = token;
    }

    /**
     *
     * @param username
     * @param authorities
     * @param attributes
     * @param expirationMinute
     * @param scaToken
     * @param responseCode
     * @param scaProfile
     * @param ip
     * @param token
     * @return
     */
    public static UserContext create(String username, List<String> authorities, Map<String, Serializable> attributes, int expirationMinute, String scaToken, String responseCode, String scaProfile, String ip, String token) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username is blank: " + username);
        }
        return new UserContext(username, authorities, attributes, expirationMinute, scaToken, responseCode, scaProfile, ip, token);
    }

    /**
     *
     * @return
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     *
     * @return
     */
    public String getScaToken() {
        return scaToken;
    }

    /**
     *
     * @return
     */
    public int getExpirationMinute() {
        return expirationMinute;
    }

    /**
     *
     * @return
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public List<String> getAuthoritiesList() {
        return authorities;
    }

    /**
     *
     * @return
     */
    public Map<String, Serializable> getAttributes() {
        return attributes;
    }

    /**
     *
     * @return
     */
    public String getScaProfile() {
        return scaProfile;
    }

    /**
     *
     * @return
     */
    public String getIp() {
        return ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
