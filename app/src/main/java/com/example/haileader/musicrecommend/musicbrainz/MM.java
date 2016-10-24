package com.example.haileader.musicrecommend.musicbrainz;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Created by haileader on 07/10/16.
 */
public class MM {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();

    /** <p>The namespace of the vocabalary as a string ({@value})</p> */
    public static final String NS = "http://musicbrainz.org/mm/mm-2.1#";

    /** <p>The namespace of the vocabalary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}

    /** <p>The namespace of the vocabalary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );

    public static final Resource Artist = m_model.createResource(NS + "Artist");
    public static final Resource Album = m_model.createResource(NS + "Album");
    public static final Resource Track = m_model.createResource(NS + "Track");
    public static final Resource ReleaseDate = m_model.createResource(NS + "ReleaseDate");

    /** <p>Sort name</p> */
    public static final Property sortname = m_model.createProperty(NS + "sortName");

    /** <p>Release status of an album</p> */
    public static final Property releaseStatus = m_model.createProperty(NS + "releaseStatus");

    /** <p>Release type of an album</p> */
    public static final Property releaseType = m_model.createProperty(NS + "releaseType");

    /** <p>Track number on album</p> */
    public static final Property trackNum = m_model.createProperty( NS + "trackNum");

    /** <p>Duration of a track</p> */
    public static final Property duration = m_model.createProperty( NS + "duration");

    public static final Property trmid = m_model.createProperty( NS + "trmid");

    public static final Property cdindexid = m_model.createProperty( NS + "cdindexid");

    public static final Property artistList = m_model.createProperty( NS + "artistList");

    public static final Property albumList = m_model.createProperty(NS + "albumList");

    public static final Property trackList = m_model.createProperty( NS + "trackList");

    public static final Property cdindexidList = m_model.createProperty( NS + "cdindexidList");

    public static final Property trmidList = m_model.createProperty( NS + "trmidList");

    public static final Property releaseDateList = m_model.createProperty( NS + "releaseDateList" );

    public static final Property country = m_model.createProperty( NS + "country" );

    //release types

    public static final Resource TypeAlbum = m_model.createResource( NS + "TypeAlbum");
    public static final Resource TypeSingle = m_model.createResource( NS + "TypeSingle");
    public static final Resource TypeEP = m_model.createResource( NS + "TypeEP");
    public static final Resource TypeCompilation = m_model.createResource( NS + "TypeCompilation");
    public static final Resource TypeSoundtrack = m_model.createResource( NS + "TypeSoundtrack");
    public static final Resource TypeSpokenword = m_model.createResource( NS + "TypeSpokenword");
    public static final Resource TypeInterview = m_model.createResource( NS + "TypeInterview");
    public static final Resource TypeAudiobook = m_model.createResource( NS + "TypeAudiobook");
    public static final Resource TypeLive = m_model.createResource(NS + "TypeLive");
    public static final Resource TypeRemix = m_model.createResource(NS + "TypeRemix");
    public static final Resource TypeOther = m_model.createResource(NS + "TypeOther");

    public static final Resource StatusOfficial = m_model.createResource( NS + "StatusOfficial");
    public static final Resource StatusBootleg = m_model.createResource( NS + "StatusBootleg");
    public static final Resource StatusPromotion = m_model.createResource( NS + "StatusPromotion");
}
