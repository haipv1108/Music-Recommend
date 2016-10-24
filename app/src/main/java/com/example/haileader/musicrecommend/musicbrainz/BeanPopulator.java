package com.example.haileader.musicrecommend.musicbrainz;

import com.example.haileader.musicrecommend.amazon.Amazon;
import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Seq;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haileader on 07/10/16.
 */
public class BeanPopulator {

    /**
     * Call this method to retrieve the artists contained in an mq:Result/mm:artistList.
     * Will walk through the graph populating the artist with a list of albums, and those
     * albums with tracks depending on the level of detail in the Model.
     *
     * @param model
     * @return the list of Artist object, populated from the artistList in the Model
     */
    public static List getArtists(Model model)
    {
        ResIterator iter = model.listSubjectsWithProperty(MM.artistList);

        if (!iter.hasNext())
        {
            return new ArrayList();
        }
        Resource result = iter.nextResource();
        Statement statement = result.getProperty(MM.artistList);
        Bag bag = statement.getBag();

        NodeIterator nIter = bag.iterator();

        List artists = new ArrayList();

        while (nIter.hasNext())
        {
            Resource resource = (Resource)nIter.nextNode();

            artists.add( getArtist(resource, false) );
        }

        return artists;
    }

    public static Artist getArtist(Resource rArtist, boolean shallow)
    {
        Artist artist = Artist.getArtist( getId( rArtist.getURI() ) );
        artist.setName( rArtist.getProperty(DC.title).getString());
        artist.setSortName( rArtist.getProperty(MM.sortname).getString());

        if (!shallow && rArtist.hasProperty(MM.albumList))
        {
            artist.setAlbums( getAlbums(rArtist) );
        }
        return artist;
    }
    /**
     * Call this method to retrieve the albums in an mq:Result/mm:albumList.
     * Will walk through the model retrieving details of the tracks and artists
     * depending on the level of detail in the Model
     * @param model
     * @return the List of Album object, may be empty.
     */
    public static List getAlbums(Model model)
    {
        ResIterator iter = model.listSubjectsWithProperty(MM.albumList);

        if (!iter.hasNext())
        {
            return new ArrayList();
        }

        Resource result = iter.nextResource();
        //make sure we're processing the result, not an mm:Artist for a query with depth > 1
        while( !result.getProperty(RDF.type).getObject().equals(MQ.Result) )
        {
            result = iter.nextResource();
        }

        Statement statement = result.getProperty(MM.albumList);
        Bag bag = statement.getBag();

        NodeIterator nIter = bag.iterator();

        List albums = new ArrayList();

        while (nIter.hasNext())
        {
            Resource rAlbum = (Resource)nIter.nextNode();
            albums.add( getAlbum(rAlbum, true) );
        }
        return albums;
    }

    /**
     * Call this to retrieve the tracks from an mq:Result/mm:trackList. Will
     * walk through the model retrieving details of the tracks, albums, artists, etc
     * depending on the level of detail in the model.
     * @param model
     * @return
     */
    public static List getTracks(Model model)
    {
        ResIterator iter = model.listSubjectsWithProperty(MM.trackList);

        if (!iter.hasNext())
        {
            return new ArrayList();
        }

        Resource result = iter.nextResource();
        //make sure we're processing the result, not an mm:Album for a query with depth > 1
        while( !result.getProperty(RDF.type).getObject().equals(MQ.Result) )
        {
            result = iter.nextResource();
        }

        Statement statement = result.getProperty(MM.trackList);
        Bag bag = statement.getBag();

        NodeIterator nIter = bag.iterator();

        List tracks = new ArrayList();

        while (nIter.hasNext())
        {
            Resource rTrack = (Resource)nIter.nextNode();
            Resource rAlbum = null;
            if (rTrack.hasProperty(MQ.album))
            {
                rAlbum = (Resource)rTrack.getProperty(MQ.album).getObject();
            }
            tracks.add( getTrack(rAlbum, rTrack) );
        }
        return tracks;
    }

    public static Album getAlbum(Resource rAlbum, boolean includeTracks)
    {
        Album album = Album.getAlbum( getId(rAlbum.getURI()) );
        if (rAlbum.hasProperty(DC.title))
        {
            album.setName( rAlbum.getProperty(DC.title).getString());
        }
        if (rAlbum.hasProperty(DC.creator))
        {
            //is the artist in the model?
            Statement statement = rAlbum.getProperty(DC.creator);
            Resource rArtist = (Resource)statement.getObject();
            if (rArtist.hasProperty(DC.title))
            {
                Artist artist = getArtist(rArtist, true);
                album.setArtist(artist);
            }
            else
            {
                //no detail in model, just store id.
                String creatorId = rAlbum.getProperty(DC.creator).getObject().toString();
                album.setArtist( Artist.getArtist( getId(creatorId) ) );
            }
        }
        if (rAlbum.hasProperty(MM.trackList) && includeTracks)
        {
            album.setTracks( getTracks(rAlbum) );
        }
        if (rAlbum.hasProperty(MM.releaseStatus))
        {
            album.setReleaseStatus( rAlbum.getProperty(MM.releaseStatus).getObject().toString() );
        }
        if (rAlbum.hasProperty(MM.releaseType))
        {
            album.setReleaseType( rAlbum.getProperty(MM.releaseType).getObject().toString() );
        }
        if ( rAlbum.hasProperty(MM.cdindexid) )
        {
            List cdindexids = new ArrayList();
            cdindexids.add( rAlbum.getProperty(MM.cdindexid).getString() );
            album.setCdindexids(cdindexids);
        }
        if ( rAlbum.hasProperty(MM.cdindexidList) )
        {
            List cdids = new ArrayList();
            Statement cdindexidList = rAlbum.getProperty(MM.cdindexidList);
            Bag cdindexBag = cdindexidList.getBag();
            NodeIterator nIter = cdindexBag.iterator();
            while (nIter.hasNext())
            {
                Resource cdindexid = (Resource)nIter.nextNode();
                cdids.add( getId(cdindexid.getURI()) );
            }
            album.setCdindexids(cdids);
        }
        if ( rAlbum.hasProperty(Amazon.Asin) )
        {
            album.setAmazonId( rAlbum.getProperty(Amazon.Asin).getString());
        }
        if ( rAlbum.hasProperty( MM.releaseDateList ) )
        {
            album.setReleaseDates( getReleaseDates(rAlbum) );
        }
        return album;
    }

    /**
     * @param resource an mm:Artist with an mm:albumList property.
     * @return a List of Album objects, may be empty.
     */
    public static List getAlbums(Resource resource)
    {
        Statement albumList = (Statement)resource.getProperty(MM.albumList);
        Bag albumBag = albumList.getBag();

        NodeIterator nIter = albumBag.iterator();
        List albums = new ArrayList();
        while (nIter.hasNext())
        {
            Resource rAlbum = (Resource)nIter.nextNode();
            albums.add( getAlbum(rAlbum, true) );
        }
        return albums;
    }

    /**
     * @param resource an mm:Album resource containing an mm:trackList
     * @return a List of Tracks, may be empty.
     */
    public static List getTracks(Resource resource)
    {
        Statement trackList = (Statement)resource.getProperty(MM.trackList);
        Seq trackSeq = trackList.getSeq();

        NodeIterator nIter = trackSeq.iterator();
        List tracks = new ArrayList();

        int count = 1;
        while (nIter.hasNext())
        {
            Resource rTrack = (Resource)nIter.nextNode();
            Track track = getTrack(resource, rTrack);
            track.setNumber(count);
            tracks.add( track );
            count++;
        }
        return tracks;
    }

    public static List getReleaseDates(Resource album)
    {
        Statement dates = (Statement)album.getProperty(MM.releaseDateList);
        Seq dateSeq = dates.getSeq();

        NodeIterator nIter = dateSeq.iterator();
        List releases = new ArrayList();

        int count = 1;
        while (nIter.hasNext())
        {
            Resource resource = (Resource)nIter.nextNode();
            releases.add( getReleaseDate(resource) );
            count++;
        }
        return releases;
    }

    public static ReleaseDate getReleaseDate(Resource resource)
    {
        ReleaseDate release = new ReleaseDate();

        if ( resource.hasProperty(DC.date) )
        {
            release.setDate( resource.getProperty(DC.date).getString() );
        }
        if ( resource.hasProperty(MM.country) )
        {
            release.setCountry( resource.getProperty(MM.country).getString() );
        }
        return release;
    }

    public static Track getTrack(Resource rAlbum, Resource rTrack)
    {
        Track track = new Track();
        track.setId( getId( rTrack.getURI() ) );
        if (rAlbum != null)
        {
            //track.setAlbum( Album.getAlbum( getId( rAlbum.getURI() ) ) );
            track.setAlbum( getAlbum(rAlbum, false) );
        }
        if (rTrack.hasProperty(MM.duration))
        {
            track.setDuration( rTrack.getProperty(MM.duration).getInt() );
        }
        if (rTrack.hasProperty(DC.creator))
        {
            //is the artist in the model?
            Statement statement = rTrack.getProperty(DC.creator);
            Resource rArtist = (Resource)statement.getObject();
            if (rArtist.hasProperty(DC.title))
            {
                Artist artist = getArtist(rArtist, true);
                track.setArtist(artist);
            }
            else
            {
                //no detail in model, just store id.
                track.setArtist( Artist.getArtist( getId( rTrack.getProperty(DC.creator).getObject().toString() ) ) );
            }

        }
        if (rTrack.hasProperty(DC.title))
        {
            track.setName( rTrack.getProperty(DC.title).getString() );
        }
        if (rTrack.hasProperty(MM.trackNum))
        {
            track.setNumber( rTrack.getProperty(MM.trackNum).getInt());
        }
        if ( rTrack.hasProperty(MM.trmid) )
        {
            List trmids = new ArrayList();
            trmids.add( rTrack.getProperty(MM.trmid).getString() );
            track.setTrmIds(trmids);
        }
        if ( rTrack.hasProperty(MM.trmidList) )
        {
            List trmids = new ArrayList();
            Statement trmidList = rTrack.getProperty(MM.trmidList);
            Bag trmBag = trmidList.getBag();
            NodeIterator nIter = trmBag.iterator();
            while (nIter.hasNext())
            {
                Resource trmid = (Resource)nIter.nextNode();
                trmids.add( getId(trmid.getURI()) );
            }
            track.setTrmIds(trmids);
        }
        return track;
    }

    /**
     * Builds a TrackSummary object from the results of a <code>getQuickTrackInfoFromTrackId</code>
     * query.
     * @param model result of the quick track info query.
     * @return The TrackSummary or null
     */
    public static TrackSummary getTrackSummary(Model model)
    {
        TrackSummary summary = new TrackSummary();
        ResIterator iter = model.listSubjectsWithProperty(RDF.type, MQ.Result);

        if (!iter.hasNext())
        {
            return null;
        }
        Resource result = iter.nextResource();

        if (result.hasProperty(MQ.artistName))
        {
            summary.setArtistName( result.getProperty(MQ.artistName).getString() );
        }

        if (result.hasProperty(MQ.albumName))
        {
            summary.setAlbumName( result.getProperty(MQ.albumName).getString() );
        }

        if (result.hasProperty(MQ.trackName))
        {
            summary.setTrackName( result.getProperty(MQ.trackName).getString() );
        }

        if (result.hasProperty(MM.trackNum))
        {
            summary.setTrackNum( result.getProperty(MM.trackNum).getInt() );
        }

        if (result.hasProperty(MM.duration))
        {
            summary.setDuration( result.getProperty(MM.duration).getInt() );
        }

        return summary;
    }

    /**
     * @param url a MB resource identifier.
     * @return the MB id from the resource URL.
     */
    private static String getId(String url)
    {
        return url.substring( url.lastIndexOf("/") + 1);
    }
}
