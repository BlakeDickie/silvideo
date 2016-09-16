/**
 * Copyright (C) 2012-2014 Blake Dickie
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.landora.model.anidb;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bdickie
 */
public class Anime implements java.io.Serializable {
    private String id;
    private int animeId;
    private DateTime lastLoaded;
    private String nameMain;
    private String nameEnglish;
    private Integer episodeCount;

    private LocalDate startDate;
    private LocalDate endDate;

    private String type;
    private String description;

    private Float ratingPermanent;
    private Integer ratingPermanentVotes;
    private Float ratingTemporary;
    private Integer ratingTemporaryVotes;

    private String pictureFileName;
    private boolean hentai;

    private List<AnimeName> names;

    private List<AnimeEpisode> episodes;
    private List<AnimeCategoryWeight> categories;
//    private List<AnimeRelation> relations;

    public Anime() {
        names = new ArrayList<>();
        episodes = new ArrayList<>();
        categories = new ArrayList<>();
    }

//    public List<AnimeName> getNames() {
//        return names;
//    }
//
//    public void setNames(List<AnimeName> names) {
//        this.names = names;
//    }
    public List<AnimeCategoryWeight> getCategories() {
        return categories;
    }

    public void setCategories( List<AnimeCategoryWeight> categories ) {
        this.categories = categories;
    }
//
//    public List<AnimeRelation> getRelations() {
//        return relations;
//    }
//
//    public void setRelations(List<AnimeRelation> relations) {
//        this.relations = relations;
//    }

    public int getAnimeId() {
        return animeId;
    }

    public void setAnimeId( int animeId ) {
        this.animeId = animeId;
    }

    public DateTime getLastLoaded() {
        return lastLoaded;
    }

    public void setLastLoaded( DateTime lastLoaded ) {
        this.lastLoaded = lastLoaded;
    }

    public String getNameMain() {
        return nameMain;
    }

    public void setNameMain( String nameMain ) {
        this.nameMain = nameMain;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish( String nameEnglish ) {
        this.nameEnglish = nameEnglish;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount( Integer episodeCount ) {
        this.episodeCount = episodeCount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate( LocalDate startDate ) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate( LocalDate endDate ) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Float getRatingPermanent() {
        return ratingPermanent;
    }

    public void setRatingPermanent( Float ratingPermanent ) {
        this.ratingPermanent = ratingPermanent;
    }

    public Integer getRatingPermanentVotes() {
        return ratingPermanentVotes;
    }

    public void setRatingPermanentVotes( Integer ratingPermanentVotes ) {
        this.ratingPermanentVotes = ratingPermanentVotes;
    }

    public Float getRatingTemporary() {
        return ratingTemporary;
    }

    public void setRatingTemporary( Float ratingTemporary ) {
        this.ratingTemporary = ratingTemporary;
    }

    public Integer getRatingTemporaryVotes() {
        return ratingTemporaryVotes;
    }

    public void setRatingTemporaryVotes( Integer ratingTemporaryVotes ) {
        this.ratingTemporaryVotes = ratingTemporaryVotes;
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public void setPictureFileName( String pictureFileName ) {
        this.pictureFileName = pictureFileName;
    }

    public boolean isHentai() {
        return hentai;
    }

    public void setHentai( boolean hentai ) {
        this.hentai = hentai;
    }

    public List<AnimeName> getNames() {
        return names;
    }

    public void setNames( List<AnimeName> names ) {
        this.names = names;
    }

    public List<AnimeEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes( List<AnimeEpisode> episodes ) {
        this.episodes = episodes;
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public AnimeEpisode findEpisodeById( int id ) {
        for ( AnimeEpisode e : getEpisodes() ) {
            if ( e.getEpisodeId() == id ) {
                return e;
            }
        }
        return null;
    }

}
