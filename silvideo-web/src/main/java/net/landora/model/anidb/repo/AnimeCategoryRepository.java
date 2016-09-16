/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.model.anidb.repo;

import net.landora.model.anidb.AnimeCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Blake Dickie
 */
public interface AnimeCategoryRepository extends MongoRepository<AnimeCategory, String> {

    public AnimeCategory findByCategoryId( int categoryId );

}
