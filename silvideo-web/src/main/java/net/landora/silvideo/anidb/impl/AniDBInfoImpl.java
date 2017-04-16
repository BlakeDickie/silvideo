/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.impl;

import net.landora.silvideo.anidb.AniDBInfo;
import net.landora.silvideo.anidb.api.AniDBAPI;
import net.landora.silvideo.anidb.api.WaitingNotification;
import net.landora.silvideo.anidb.model.Anime;
import net.landora.silvideo.anidb.model.AnimeEpisode;
import net.landora.silvideo.anidb.model.AnimeFile;
import net.landora.silvideo.anidb.model.repo.AnimeFileRepository;
import net.landora.silvideo.anidb.model.repo.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 *
 * @author bdickie
 */
@Service
public class AniDBInfoImpl implements AniDBInfo {

    @Autowired
    private AniDBAPI api;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private AnimeFileRepository fileRepository;

    @Override
    public Anime getAnime( int animeId ) {
        Anime anime = animeRepository.findByAnimeId( animeId );
        if ( anime != null ) {
            return anime;
        }
        anime = api.downloadAnime( animeId );
        return animeRepository.save( anime );
    }

    @Override
    public Anime updateAnime( int animeId ) {
        Anime anime = api.downloadAnime( animeId );
        return animeRepository.save( anime );
    }

    @Override
    public AnimeEpisode getEpisode( int episodeId ) {
        Anime anime = animeRepository.findByEpisodes_EpisodeId( episodeId );
        if ( anime == null ) {
            AnimeEpisode episode = api.getAnimeEpisode( episodeId );

            anime = episode.getAnime();
            anime.getEpisodes().add( episode );
            anime = animeRepository.save( anime );
        }

        for ( AnimeEpisode episode : anime.getEpisodes() ) {
            if ( episode.getEpisodeId() == episodeId ) {
                return episode;
            }
        }

        return null;
    }

    @Override
    public AnimeFile getFile( int fileId ) {
        AnimeFile file = fileRepository.findByFileId( fileId );
        if ( file == null ) {
            file = api.getAnimeFile( fileId );
            file = fileRepository.save( file );
        }
        return file;
    }

    @Override
    public boolean checkNotifications() {
        if ( !api.hasNotifications() ) {
            return false;
        }

        List<WaitingNotification> notifications = api.getNotifications();
        for ( WaitingNotification notification : notifications ) {
            switch ( notification.getType() ) {
                case Message:
                    api.getMessage( notification.getId() );
                    break;
                case Notication:
                    api.getNotification( notification.getId() );
                    break;
            }
        }

        return true;
    }

}
