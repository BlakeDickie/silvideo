/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.model.repo;

import net.landora.silvideo.anidb.model.AnimeFile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Blake Dickie
 */
public interface AnimeFileRepository extends MongoRepository<AnimeFile, String> {

    public AnimeFile findByFileId( int fileId );

}
