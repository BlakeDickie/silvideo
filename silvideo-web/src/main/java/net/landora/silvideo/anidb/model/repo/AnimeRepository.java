/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.model.repo;

import net.landora.silvideo.anidb.model.Anime;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Blake Dickie
 */
public interface AnimeRepository extends MongoRepository<Anime, String> {

    public Anime findByAnimeId( int animeId );

    public Anime findByEpisodes_EpisodeId( int episodeId );
}
