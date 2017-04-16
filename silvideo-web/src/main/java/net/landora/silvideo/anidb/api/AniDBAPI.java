/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.api;

import net.landora.silvideo.anidb.model.Anime;
import net.landora.silvideo.anidb.model.AnimeEpisode;
import net.landora.silvideo.anidb.model.AnimeFile;
import java.util.List;

/**
 *
 * @author bdickie
 */
public interface AniDBAPI {

    public boolean downloadTitles();

    public Anime downloadAnime( int animeId );

    public List<WaitingNotification> getNotifications();

    public void getNotification( int id );

    public void getMessage( int id );

    public boolean hasNotifications();

    AnimeEpisode getAnimeEpisode( int animeId, String episodeNumber );

    AnimeEpisode getAnimeEpisode( int animeId, int episodeNumber );

    AnimeEpisode getAnimeEpisode( int episodeId );

    AnimeFile getAnimeFile( String ed2k, long size );

    AnimeFile getAnimeFile( int fileId );

    boolean queueMyListExport( String templateName );

}
