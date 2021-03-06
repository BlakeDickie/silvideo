/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.model.repo;

import net.landora.silvideo.anidb.model.AnimeNameLookup;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 *
 * @author Blake Dickie
 */
public interface AnimeNameLookupRepository extends MongoRepository<AnimeNameLookup, String> {

    public List<AnimeNameLookup> findByAnimeId( int animeId );

}
