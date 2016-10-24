package com.example.haileader.musicrecommend.musicbrainz;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by haileader on 07/10/16.
 */
public class Album implements Comparable, Serializable {
    private String _id;
    private String _name;
    private String _releaseStatus;
    private String _releaseType;
    private Artist _artist;
    private List _tracks;
    private List _cdindexids;
    private String _asin;
    private List _releaseDates;

    private static HashMap _albums = new HashMap();

    public static Album getAlbum(String id)
    {
        Album album = (Album)_albums.get(id);
        if (album == null)
        {
            album = new Album();
            album.setId(id);
            _albums.put(id, album);
        }
        return album;
    }

    /**
     * @return
     */
    public Artist getArtist()
    {
        return _artist;
    }

    /**
     * @return
     */
    public List getCdindexids()
    {
        return _cdindexids;
    }

    /**
     * @return
     */
    public String getId()
    {
        return _id;
    }

    /**
     * @return
     */
    public String getReleaseStatus()
    {
        return _releaseStatus;
    }

    /**
     * @return
     */
    public String getReleaseType()
    {
        return _releaseType;
    }

    /**
     * @return
     */
    public List getTracks()
    {
        return _tracks;
    }

    /**
     * @param artist
     */
    public void setArtist(Artist artist)
    {
        _artist = artist;
    }

    /**
     * @param list
     */
    public void setCdindexids(List list)
    {
        _cdindexids = list;
    }

    /**
     * @param string
     */
    public void setId(String string)
    {
        _id = string;
    }

    /**
     * @param string
     */
    public void setReleaseStatus(String string)
    {
        _releaseStatus = string;
    }

    /**
     * @param string
     */
    public void setReleaseType(String string)
    {
        _releaseType = string;
    }

    /**
     * @param list
     */
    public void setTracks(List list)
    {
        _tracks = list;
    }

    /**
     * @return
     */
    public String getName()
    {
        return _name;
    }

    /**
     * @param string
     */
    public void setName(String string)
    {
        _name = string;
    }

    public String getAmazonId()
    {
        return _asin;
    }

    public void setAmazonId(String asin)
    {
        _asin = asin;
    }


    public List getReleaseDates()
    {
        return _releaseDates;
    }
    public void setReleaseDates(List releaseDates)
    {
        _releaseDates = releaseDates;
    }

    public int hashCode()
    {
        return _id.hashCode();
    }

    public String toString()
    {
        return _id + "[" + _name + "]";
    }

    public boolean equals(Object other)
    {
        Album album = (Album)other;
        if (album._id == null)
        {
            return false;
        }
        return _id.equals(album._id);
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object other)
    {
        Album album = (Album)other;
        return _name.toLowerCase().compareTo( album._name.toLowerCase() );
    }

    public boolean isCompilation()
    {
        return MusicBrainz.VARIOUS_ARTISTS.equals(_artist.getId());
    }

}
