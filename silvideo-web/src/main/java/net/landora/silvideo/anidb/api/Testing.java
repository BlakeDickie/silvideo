/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.api;

import net.landora.silvideo.anidb.AniDBInfo;
import net.landora.silvideo.anidb.api.impl.udp.AniDBUDPManager;
import net.landora.silvideo.anidb.model.repo.AnimeRepository;
import net.landora.silvideo.anidb.tvdb.MappingListLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author bdickie
 */
@Component
public class Testing implements CommandLineRunner {

    static final String FILE_FMASK = "71C04B0000";
    static final String FILE_AMASK = "000000C1";

    @Autowired
    private AniDBUDPManager udpManager;

    @Autowired
    private AniDBAPI api;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private AniDBInfo info;

    @Autowired
    private MappingListLoader mappingListLoader;

    @Override
    public void run( String... args ) throws Exception {
//        api.queueMyListExport( "xml-plain-new" );
//        System.exit( 1 );
//        info.checkNotifications();
//        Anime anime = api.downloadAnime( 5025 );
//        anime.setCategories( Collections.EMPTY_LIST );
//        anime.setEpisodes( null );
//        animeRepository.save( anime );
//        AniDBCommand command = new AniDBCommand( "FILE" );
////        command.addArgument( "aid", "5025" );
//        command.addArgument( "fid", "299092" );
////        command.addArgument( "generic", "1" );
////        command.addArgument( "epno", "2" );
//        command.addArgument( "fmask", FILE_FMASK );
//        command.addArgument( "amask", FILE_AMASK );
////        command.addArgument( "gid", "0" );
////        command.addArgument( "epno", "1" );
//
//        AniDBReply reply = udpManager.sendData( command );
//        System.out.println( reply );
        mappingListLoader.loadList();
    }

}
