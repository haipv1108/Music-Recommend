package com.example.haileader.musicrecommend.musicbrainz;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Created by haileader on 07/10/16.
 */
public class MQ {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();

    /** <p>The namespace of the vocabalary as a string ({@value})</p> */
    public static final String NS = "http://musicbrainz.org/mm/mq-1.1#";

    /** <p>The namespace of the vocabalary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}

    /** <p>The namespace of the vocabalary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );

    /** <p>The Result class is used to encapsulate all of the server responses.</p> */
    public static final Resource Result = m_model.createResource(NS + "Result");

    public static final Resource SubmitTrack = m_model.createResource(NS + "SubmitTrack");
    public static final Resource LookupMetadata = m_model.createResource(NS + "LookupMetadata");
    public static final Resource FindDisctinctTrmId = m_model.createResource(NS + "FindDisctinctTrmId");
    public static final Resource FindArtist = m_model.createResource(NS + "FindArtist");
    public static final Resource FindAlbum = m_model.createResource(NS + "FindAlbum");
    public static final Resource FindTrack = m_model.createResource(NS + "FindTrack");
    public static final Resource GetCDInfo = m_model.createResource(NS + "GetCDInfo");
    public static final Resource SubmitTRMList = m_model.createResource( NS + "SubmitTRMList");

    /** <p>A query status code from the server. Values for this include OK or fuzzy. Fuzzy is returned when the server made a
     * fuzzy match somewhere while handling the query. E.g. call getTrackInfoFromTRMid with only
     * metadata and no TRM id</p> */
    public static final Property status = m_model.createProperty(NS + "status");

    /** <p>An error message returned from the server. </p> */
    public static final Property error = m_model.createProperty(NS + "error");

    public static final Property artistName = m_model.createProperty(NS + "artistName");

    public static final Property albumName = m_model.createProperty(NS + "albumName");

    public static final Property trackName = m_model.createProperty(NS + "trackName");

    public static final Property relevance = m_model.createProperty(NS + "relevance");

    public static final Property sessionId = m_model.createProperty(NS + "sessionId");

    public static final Property authChallenge = m_model.createProperty(NS + "authChallenge");

    //not seen examples of the following, but are in query.h
    public static final Property artist = m_model.createProperty(NS + "artist");

    public static final Property album = m_model.createProperty(NS + "album");

    public static final Property track = m_model.createProperty(NS + "track");
}
