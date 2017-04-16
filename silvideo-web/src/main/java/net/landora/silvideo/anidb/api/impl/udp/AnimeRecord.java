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
package net.landora.silvideo.anidb.api.impl.udp;

import net.landora.silvideo.anidb.model.RelationType;
import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bdickie
 */
public class AnimeRecord {

    private int aid;
    private String type;
    private List<Relation> relatedAnime = new ArrayList<Relation>();
    private List<Category> category = new ArrayList<Category>();
    private int episodes;
    private int normalEpisodes;
    private int specialEpisodes;
    private LocalDate airDate;
    private LocalDate endDate;
    private String picName;
    private int rating;
    private int ratingCount;
    private int tempRating;
    private int tempRatingCount;
    private long lastUpdated;
    private int specialsCount;
    private int creditsCount;
    private int otherCount;
    private int trailerCount;
    private int parodyCount;
    private boolean adultOnly;

    public AnimeRecord() {
    }

    public int getAid() {
        return aid;
    }

    public void setAid( int aid ) {
        this.aid = aid;
    }

    public LocalDate getAirDate() {
        return airDate;
    }

    public void setAirDate( LocalDate airDate ) {
        this.airDate = airDate;
    }

    public int getCreditsCount() {
        return creditsCount;
    }

    public void setCreditsCount( int creditsCount ) {
        this.creditsCount = creditsCount;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate( LocalDate endDate ) {
        this.endDate = endDate;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes( int episodes ) {
        this.episodes = episodes;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated( long lastUpdated ) {
        this.lastUpdated = lastUpdated;
    }

    public int getNormalEpisodes() {
        return normalEpisodes;
    }

    public void setNormalEpisodes( int normalEpisodes ) {
        this.normalEpisodes = normalEpisodes;
    }

    public int getOtherCount() {
        return otherCount;
    }

    public void setOtherCount( int otherCount ) {
        this.otherCount = otherCount;
    }

    public int getParodyCount() {
        return parodyCount;
    }

    public void setParodyCount( int parodyCount ) {
        this.parodyCount = parodyCount;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName( String picName ) {
        this.picName = picName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating( int rating ) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount( int ratingCount ) {
        this.ratingCount = ratingCount;
    }

    public List<Relation> getRelatedAnime() {
        return relatedAnime;
    }

    public void setRelatedAnime( List<Relation> relatedAnime ) {
        this.relatedAnime = relatedAnime;
    }

    public int getSpecialEpisodes() {
        return specialEpisodes;
    }

    public void setSpecialEpisodes( int specialEpisodes ) {
        this.specialEpisodes = specialEpisodes;
    }

    public int getSpecialsCount() {
        return specialsCount;
    }

    public void setSpecialsCount( int specialsCount ) {
        this.specialsCount = specialsCount;
    }

    public int getTempRating() {
        return tempRating;
    }

    public void setTempRating( int tempRating ) {
        this.tempRating = tempRating;
    }

    public int getTempRatingCount() {
        return tempRatingCount;
    }

    public void setTempRatingCount( int tempRatingCount ) {
        this.tempRatingCount = tempRatingCount;
    }

    public int getTrailerCount() {
        return trailerCount;
    }

    public void setTrailerCount( int trailerCount ) {
        this.trailerCount = trailerCount;
    }

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public boolean isAdultOnly() {
        return adultOnly;
    }

    public void setAdultOnly( boolean adultOnly ) {
        this.adultOnly = adultOnly;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory( List<Category> category ) {
        this.category = category;
    }

    public static class Relation {

        private int aid;
        private RelationType type;

        public Relation( int aid, RelationType type ) {
            this.aid = aid;
            this.type = type;
        }

        public int getAid() {
            return aid;
        }

        public void setAid( int aid ) {
            this.aid = aid;
        }

        public RelationType getType() {
            return type;
        }

        public void setType( RelationType type ) {
            this.type = type;
        }
    }

    public static class Category {

        private int categoryId;
        private int weight;

        public Category( int categoryId, int weight ) {
            this.categoryId = categoryId;
            this.weight = weight;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId( int categoryId ) {
            this.categoryId = categoryId;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight( int weight ) {
            this.weight = weight;
        }

    }
}
