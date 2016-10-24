package com.example.haileader.musicrecommend.musicbrainz;

import com.hp.hpl.jena.rdf.model.Model;

import java.io.IOException;

/**
 * Created by haileader on 07/10/16.
 */
public interface MusicBrainz
{
    /**
     * The MusicBrainz ID used to indicate that an album is a "various artists" album
     * (i.e. a compilation).
     */
    public static final String VARIOUS_ARTISTS = "89ad4ac3-39f7-470e-963a-56509c546377";

    /**
     * Length of a MusicBrainz CD Index Id
     */
    public static final int CD_INDEX_ID_LENGTH = 28;

    /**
     * Length of a MusicBrain Artist/Album/Track Id
     */
    public static final int MB_ID_LENGTH = 36;

    /**
     * Configure the MusicBrainz server to use for the queries
     * @param server MB server to use for queries
     */
    public void setServer(String server);

    /**
     * Use this query to return an albumList for the given CD Index Id
     * @param cdindexid The cdindex id to look up at the remote server.
     * @param depth level of detail in query response
     */
    public Model getCDInfoFromCDIndexId(String cdindexid, int depth)
            throws IOException;

    /**
     * Use this query to return the metadata information (artistname,
     * albumname, trackname, tracknumber) for a given trm id. Optionally,
     * you can also specifiy the basic artist metadata, so that if the server
     * cannot match on the TRM id, it will attempt to match based on the basic
     * metadata.
     * In case of a TRM collision (where one TRM may point to more than one track)
     * this function will return more than on track. The user (or tagging app)
     * must decide which track information is correct.
     * @param trmid The TRM id for the track to be looked up
     * @param artistName The name of the artist
     * @param albumName The name of the album
     * @param trackName The name of the track
     * @param trackNum The number of the track
     */
    public Model getTrackInfoFromTRMId(String trmid,
                                       String artistName,
                                       String albumName,
                                       String trackName,
                                       String trackNum,
                                       String duration)
            throws IOException;

    /**
     * Use this query to return the basic metadata information (artistname,
     * albumname, trackname, tracknumber) for a given track mb id
     * @param trackid The MB track id for the track to be looked up
     * @param albumid The MB album id for the track to be looked up
     */
    public Model getQuickTrackInfoFromTrackId(String trackid, String albumid)
            throws IOException;

    /**
     * Use this query to find artists by name. This function returns an artistList
     * for the given artist name.
     * @param artistName The name of the artist to find.
     * @param depth level of detail in response
     * @param maxItems maximum number of items to return
     */
    public Model findArtistByName(String artistName, int depth, int maxItems)
            throws IOException;

    /**
     * Use this query to find albums by name. This function returns an albumList
     * for the given album name.
     * @param albumName The name of the album to find.
     * @param depth level of detail in response. Set depth to be 4 to get details of artist and all tracks
     * @param maxItems maximum number of items to return
     */
    public Model findAlbumByName(String albumName, int depth, int maxItems)
            throws IOException;

    /**
     * Use this query to find tracks by name. This function returns a trackList
     * for the given track name.
     * @param trackName The name of the track to find.
     * @param depth level of detail in response
     * @param maxItems maximum number of items to return
     */
    public Model findTrackByName(String trackName, int depth, int maxItems)
            throws IOException;

    /**
     * Use this function to find TRM Ids that match a given artistName
     * and trackName, This query returns a trmidList.
     *
     * TODO resolve the expected behaviour of this: even for known tracks I get no useful results.
     * This appears to be a bug in the MusicBrainz server code.
     *
     * @param artistName The name of the artist to find.
     * @param trackName The name of the track to find.
     * @param depth level of detail in response
     */
    public Model findDistinctTRMId(String artistName, String trackName, int depth)
            throws IOException;

    /**
     * Retrieve an artistList from a given Artist id
     * @param artistid MB id of the artist
     * @param depth level of detail in the result
     */
    public Model getArtistById(String artistid, int depth)
            throws IOException;

    /**
     * Retrieve an albumList from a given Album id
     * @param albumid MB id of the album
     * @param depth level of detail in the result
     */
    public Model getAlbumById(String albumid, int depth)
            throws IOException;

    /**
     * Retrieve a trackList from a given Track id
     * @param trackid MB id of the album
     * @param depth level of detail in the result
     */
    public Model getTrackById(String trackid, int depth)
            throws IOException;

    /**
     * Retrieve a trackList from a given TRM id
     * @param trackid TRM id of the album
     * @param depth level of detail in the result
     */
    public Model getTrackByTRMId(String trmid, int depth)
            throws IOException;

}

