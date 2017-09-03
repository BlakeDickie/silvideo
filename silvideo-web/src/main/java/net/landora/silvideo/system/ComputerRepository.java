/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.system;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author bdickie
 */
public interface ComputerRepository extends MongoRepository<Computer, String> {

}
