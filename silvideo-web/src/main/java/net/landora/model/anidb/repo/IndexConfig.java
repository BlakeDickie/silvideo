/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.model.anidb.repo;

import net.landora.model.anidb.Anime;
import net.landora.model.anidb.AnimeCategory;
import net.landora.model.anidb.AnimeFile;
import net.landora.model.anidb.AnimeGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import javax.annotation.PostConstruct;

/**
 *
 * @author Blake Dickie
 */
@Configuration
public class IndexConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void init() {
        mongoTemplate.indexOps( Anime.class )
                .ensureIndex( new Index().on( "animeId", Sort.Direction.ASC ) );
        mongoTemplate.indexOps( AnimeGroup.class )
                .ensureIndex( new Index().on( "groupId", Sort.Direction.ASC ) );
        mongoTemplate.indexOps( AnimeCategory.class )
                .ensureIndex( new Index().on( "categoryId", Sort.Direction.ASC ) );
        mongoTemplate.indexOps( AnimeFile.class )
                .ensureIndex( new Index().on( "fileId", Sort.Direction.ASC ) );
    }

}
