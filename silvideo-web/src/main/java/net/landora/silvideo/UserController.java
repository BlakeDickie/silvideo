/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

/**
 *
 * @author bdickie
 */
@RestController
public class UserController {

    @RequestMapping( "/user" )
    public Principal user( Principal principal ) {
        return principal;
    }

}
