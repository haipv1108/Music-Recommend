package com.example.haileader.musicrecommend.musicbrainz;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

/**
 * Created by haileader on 07/10/16.
 */
public class MusicBrainzImpl implements MusicBrainz {
    public static final String DEFAULT_SERVER = "http://mm.musicbrainz.org";
    public static final String CGI_PATH = "/cgi-bin/mq_2_1.pl";

    private String _server;

    public MusicBrainzImpl()
    {
        _server = DEFAULT_SERVER;
    }

    public MusicBrainzImpl(String server)
    {
        _server = server;
    }

    public Model findAlbumByName(String albumName, int depth, int maxItems)
            throws IOException
    {
        Object[] param = new Object[] {new Integer(depth),
                new Integer(maxItems),
                albumName};
        String query = makeQuery(Templates.FIND_ALBUM, param);
        return doPostQuery(_server, query);
    }

    public Model findArtistByName(String artistName, int depth, int maxItems)
            throws IOException
    {

        Object[] param = new Object[] {new Integer(depth),
                artistName,
                new Integer(maxItems)};
        String query = makeQuery(Templates.FIND_ARTIST, param);
        return doPostQuery(_server, query);
    }

    public Model findDistinctTRMId(
            String artistName,
            String trackName,
            int depth)
            throws IOException
    {
        Object[] param = new Object[] {new Integer(depth),
                artistName,
                trackName};
        String query = makeQuery(Templates.FIND_DISTINCT_TRM_ID, param);
        System.out.println(query);
        return doPostQuery(_server, query);
    }

    public Model findTrackByName(String trackName, int depth, int maxItems)
            throws IOException
    {
        Object[] param = new Object[] {new Integer(depth),
                new Integer(maxItems),
                trackName};
        String query = makeQuery(Templates.FIND_TRACK, param);
        return doPostQuery(_server, query);
    }

    public Model getAlbumById(String albumid, int depth)
            throws IOException
    {
        Object[] param = new Object[] { _server, albumid, new Integer(depth) };
        String query = makeQuery(Templates.GET_ALBUM_BY_ID, param);
        return doGetQuery(query);
    }

    public Model getArtistById(String artistid, int depth)
            throws IOException
    {
        Object[] param = new Object[] { _server, artistid, new Integer(depth) };
        String query = makeQuery(Templates.GET_ARTIST_BY_ID, param);
        return doGetQuery(query);
    }

    public Model getCDInfoFromCDIndexId(String cdindexid, int depth)
            throws IOException
    {
        Object[] param = new Object[] {new Integer(depth),
                cdindexid};
        String query = makeQuery(Templates.GET_CD_INFO, param);
        return doPostQuery(_server, query);
    }

    public Model getQuickTrackInfoFromTrackId(String trackid, String albumid)
            throws IOException
    {
        Object[] param = new Object[] {trackid, albumid};
        String query = makeQuery(Templates.QUICK_TRACK_INFO_FROM_TRACK_ID, param);
        return doPostQuery(_server, query);
    }

    public Model getTrackById(String trackid, int depth)
            throws IOException
    {
        Object[] param = new Object[] { _server, trackid, new Integer(depth) };
        String query = makeQuery(Templates.GET_TRACK_BY_ID, param);
        return doGetQuery(query);
    }

    public Model getTrackByTRMId(String trmid, int depth)
            throws IOException
    {
        Object[] param = new Object[] { _server, trmid, new Integer(depth) };
        String query = makeQuery(Templates.GET_TRACK_BY_TRM_ID, param);
        return doGetQuery(query);
    }

    public Model getTrackInfoFromTRMId(
            String trmid,
            String artistName,
            String albumName,
            String trackName,
            String trackNum,
            String duration)
            throws IOException
    {
        Object[] param = new Object[] {trmid, artistName,
                albumName, trackName,
                trackNum, duration};
        String query = makeQuery(Templates.TRACK_INFO_FROM_TRM_ID, param);
        return doPostQuery(_server, query);
    }

    public void setServer(String server)
    {
        if (server != null)
        {
            _server = server;
        }
    }

    private String makeQuery(String queryTemplate, Object[] params)
    {
        return MessageFormat.format(queryTemplate, params);
    }

    private Model doGetQuery(String url) throws IOException
    {
        //System.out.println("Getting...\n" + url);
        URL serverURL = new URL(url);
        HttpURLConnection con = (HttpURLConnection)serverURL.openConnection();
        con.setRequestMethod("GET");


        MusicBrainzException.throwIfNecessary(con.getResponseCode(), con.getResponseMessage());
        Model model = ModelFactory.createDefaultModel();
        model.read(con.getInputStream(), "");
        con.disconnect();

        //model.write(System.out);
        MusicBrainzException.throwIfNecessary(model);

        return model;
    }

    private Model doPostQuery(String url, String rdf) throws IOException
    {
        URL serverURL = new URL(_server + CGI_PATH);
        HttpURLConnection con = (HttpURLConnection)serverURL.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Length", rdf.length()+"");

        //System.out.println("Sending...\n" + rdf);

        PrintWriter out = new PrintWriter(
                new OutputStreamWriter(con.getOutputStream(), "UTF-8") );
        out.write(rdf);
        out.flush();
        out.close();

        MusicBrainzException.throwIfNecessary(con.getResponseCode(), con.getResponseMessage());
        Model model = ModelFactory.createDefaultModel();
        model.read(con.getInputStream(), "");
        con.disconnect();

        //model.write(System.out);
        MusicBrainzException.throwIfNecessary(model);

        return model;
    }

}
