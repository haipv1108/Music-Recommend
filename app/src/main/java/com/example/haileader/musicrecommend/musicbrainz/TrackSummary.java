package com.example.haileader.musicrecommend.musicbrainz;

import java.io.Serializable;

/**
 * Created by haileader on 07/10/16.
 */
public final class TrackSummary implements Serializable {
    private String _artistName;
    private String _albumName;
    private String _trackName;
    private int _trackNum;
    private int _duration;

    protected TrackSummary() {}

    /**
     * @return
     */
    public String getAlbumName()
    {
        return _albumName;
    }

    /**
     * @return
     */
    public String getArtistName()
    {
        return _artistName;
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
    public String getTrackName()
    {
        return _trackName;
    }

    /**
     * @return
     */
    public int getTrackNum()
    {
        return _trackNum;
    }

    /**
     * @param string
     */
    public void setAlbumName(String string)
    {
        _albumName = string;
    }

    /**
     * @param string
     */
    public void setArtistName(String string)
    {
        _artistName = string;
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
    public void setTrackName(String string)
    {
        _trackName = string;
    }

    /**
     * @param i
     */
    public void setTrackNum(int i)
    {
        _trackNum = i;
    }

    public String toString()
    {
        return _artistName + "/" + _albumName + "/" + "_trackName";
    }

    public int hashCode()
    {
        return toString().hashCode();
    }
}
