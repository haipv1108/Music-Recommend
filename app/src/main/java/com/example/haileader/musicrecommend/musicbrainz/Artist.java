package com.example.haileader.musicrecommend.musicbrainz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by haileader on 07/10/16.
 */
public class Artist implements Comparable, Serializable {
    private String _id;
    private String _name;
    private String _sortName;
    private List _albums;

    private static HashMap _artists = new HashMap();

    public static Artist getArtist(String id)
    {
        Artist artist = (Artist)_artists.get(id);
        if (artist == null)
        {
            artist = new Artist();
            artist.setId(id);
            _artists.put(id, artist);
        }
        return artist;
    }

    public Artist()
    {
        _albums = new ArrayList();
    }

    /**
     * @return a List of albums. List may be empty
     */
    public List getAlbums()
    {
        return _albums;
    }

    /**
     * @return the MusicBrainz Identifier for this artist
     */
    public String getId()
    {
        return _id;
    }

    /**
     * @return the name of the artist
     */
    public String getName()
    {
        return _name;
    }

    /**
     * @return the sort name of the artist
     */
    public String getSortName()
    {
        return _sortName;
    }

    /**
     * @param list sets the list of albums created by this artist
     */
    public void setAlbums(List list)
    {
        _albums = list;
    }

    /**
     * @param string sets the MusicBrainz identifier for this artist
     */
    public void setId(String string)
    {
        _id = string;
    }

    /**
     * @param string sets the name of the artist
     */
    public void setName(String string)
    {
        _name = string;
    }

    /**
     * @param string sets the sort name of the artist
     */
    public void setSortName(String string)
    {
        _sortName = string;
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
        Artist artist = (Artist)other;
        if (artist._id == null)
        {
            return false;
        }
        return _id.equals(artist._id);
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object other)
    {
        Artist artist = (Artist)other;
        return _sortName.toLowerCase().compareTo( artist._sortName.toLowerCase() );
    }
}
