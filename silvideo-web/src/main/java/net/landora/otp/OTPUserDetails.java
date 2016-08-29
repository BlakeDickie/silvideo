/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.otp;

import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Blake Dickie
 */
public interface OTPUserDetails extends UserDetails {

    public byte[] getEncryptedTotpSecretKey();
}
