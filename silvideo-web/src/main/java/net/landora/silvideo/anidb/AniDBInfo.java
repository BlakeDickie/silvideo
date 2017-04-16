/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb;

import net.landora.silvideo.anidb.model.Anime;
import net.landora.silvideo.anidb.model.AnimeEpisode;
import net.landora.silvideo.anidb.model.AnimeFile;

/**
 *
 * @author bdickie
 */
public interface AniDBInfo {

    public Anime getAnime( int animeId );

    public Anime updateAnime( int animeId );

    public boolean checkNotifications();

    AnimeEpisode getEpisode( int episodeId );

    AnimeFile getFile( int fileId );

}
