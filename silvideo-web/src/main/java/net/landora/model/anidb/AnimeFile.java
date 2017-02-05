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

import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 *
 * @author bdickie
 */
public class AnimeFile implements java.io.Serializable {

    public static final int SAVE_REVISION = 2;

    private String id;

    private int fileId;
    @DBRef
    private Anime anime;
    private int episodeId;
    @DBRef
    private AnimeGroup group;

    private Boolean crcValid;
    private Integer version;
    private Boolean censored;

    private Long size;
    private String ed2k;

    private String source;
    private String videoCodec;
    private String videoResolution;
    private String fileType;
    private int currentSaveRevision;

    private boolean generic;

    private AnimeListItem listItem;

    public AnimeFile() {
    }

    public Boolean getCensored() {
        return censored;
    }

    public void setCensored( Boolean censored ) {
        this.censored = censored;
    }

    public Boolean getCrcValid() {
        return crcValid;
    }

    public void setCrcValid( Boolean crcValid ) {
        this.crcValid = crcValid;
    }

    public AnimeEpisode getEpisode() {
        return getAnime().findEpisodeById( getEpisodeId() );
    }

    public void setEpisode( AnimeEpisode episode ) {
        setAnime( episode.getAnime() );
        setEpisodeId( episode.getEpisodeId() );
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId( int fileId ) {
        this.fileId = fileId;
    }

    public AnimeGroup getGroup() {
        return group;
    }

    public void setGroup( AnimeGroup group ) {
        this.group = group;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion( Integer version ) {
        this.version = version;
    }

    public String getEd2k() {
        return ed2k;
    }

    public void setEd2k( String ed2k ) {
        this.ed2k = ed2k;
    }

    public Long getSize() {
        return size;
    }

    public void setSize( Long size ) {
        this.size = size;
    }

    public boolean isGeneric() {
        return generic;
    }

    public void setGeneric( boolean generic ) {
        this.generic = generic;
    }

    public void setState( int state ) {
        crcValid = null;
        version = null;
        censored = null;

        if ( ( state & FILE_CRCOK ) != 0 ) {
            crcValid = true;
        } else if ( ( state & FILE_CRCERR ) != 0 ) {
            crcValid = false;
        }

        if ( ( state & FILE_ISV2 ) != 0 ) {
            version = 2;
        } else if ( ( state & FILE_ISV3 ) != 0 ) {
            version = 3;
        } else if ( ( state & FILE_ISV4 ) != 0 ) {
            version = 4;
        } else if ( ( state & FILE_ISV5 ) != 0 ) {
            version = 5;
        }

        if ( ( state & FILE_CEN ) != 0 ) {
            censored = true;
        } else if ( ( state & FILE_UNC ) != 0 ) {
            censored = false;
        }
    }

    private static final int FILE_CRCOK = 1;
    private static final int FILE_CRCERR = 2;
    private static final int FILE_ISV2 = 4;
    private static final int FILE_ISV3 = 8;
    private static final int FILE_ISV4 = 16;
    private static final int FILE_ISV5 = 32;
    private static final int FILE_UNC = 64;
    private static final int FILE_CEN = 128;

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final AnimeFile other = (AnimeFile) obj;
        if ( this.fileId != other.fileId ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.fileId;
        return hash;
    }

    @Override
    public String toString() {
        if ( isGeneric() ) {
            return String.format( "Generic: %s", getEpisode().toString() );
        } else if ( group == null ) {
            return String.format( "No Group: %s", getEpisode().toString() );
        } else {
            return String.format( "%s: %s", group.getShortName(), getEpisode().toString() );
        }
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType( String fileType ) {
        this.fileType = fileType;
    }

    public String getSource() {
        return source;
    }

    public void setSource( String source ) {
        this.source = source;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec( String videoCodec ) {
        this.videoCodec = videoCodec;
    }

    public String getVideoResolution() {
        return videoResolution;
    }

    public void setVideoResolution( String videoResolution ) {
        this.videoResolution = videoResolution;
    }

    public int getCurrentSaveRevision() {
        return currentSaveRevision;
    }

    public void setCurrentSaveRevision( int currentSaveRevision ) {
        this.currentSaveRevision = currentSaveRevision;
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public AnimeListItem getListItem() {
        return listItem;
    }

    public void setListItem( AnimeListItem listItem ) {
        this.listItem = listItem;
    }

    public Anime getAnime() {
        return anime;
    }

    public void setAnime( Anime anime ) {
        this.anime = anime;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId( int episodeId ) {
        this.episodeId = episodeId;
    }

}
