/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.security;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author bdickie
 */
public interface UserInfoRepository extends MongoRepository<UserInfo, String> {

    public UserInfo findByGoogleSubject( String googleSubject );

}
