package com.example.haileader.musicrecommend.musicbrainz;

import java.io.Serializable;

/**
 * Created by haileader on 07/10/16.
 */
public class ReleaseDate implements Serializable {
    private String _date;
    private String _country;

    public String getCountry()
    {
        return _country;
    }
    public String getDate()
    {
        return _date;
    }
    public void setCountry(String country)
    {
        _country = country;
    }
    public void setDate(String date)
    {
        _date = date;
    }
}
