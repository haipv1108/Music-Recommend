package com.example.haileader.musicrecommend.musicbrainz;

import java.io.Serializable;
import java.util.List;

/**
 * Created by haileader on 07/10/16.
 */
public class Track implements Comparable, Serializable {

    private String _id;
    private String _name;
    private int _number;
    private int _duration; //ms
    private Artist _artist;
    private List _trmIds;
    private Album _album;

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
    public int getDuration()
    {
        return _duration;
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
    public String getName()
    {
        return _name;
    }

    /**
     * @return
     */
    public int getNumber()
    {
        return _number;
    }

    /**
     * @return
     */
    public List getTrmIds()
    {
        return _trmIds;
    }

    /**
     * @param artist
     */
    public void setArtist(Artist artist)
    {
        _artist = artist;
    }

    /**
     * @param i
     */
    public void setDuration(int i)
    {
        _duration = i;
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
    public void setName(String string)
    {
        _name = string;
    }

    /**
     * @param i
     */
    public void setNumber(int i)
    {
        _number = i;
    }

    /**
     * @param list
     */
    public void setTrmIds(List list)
    {
        _trmIds = list;
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
        Track track = (Track)other;
        if (track._id == null)
        {
            return false;
        }
        return _id.equals(track._id);
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object other)
    {
        Track track = (Track)other;

        if (!_album.equals(track._album))
        {
            return -1;
        }

        return _number - track._number;
    }
    /**
     * @return
     */
    public Album getAlbum()
    {
        return _album;
    }

    /**
     * @param album
     */
    public void setAlbum(Album album)
    {
        _album = album;
    }

}
