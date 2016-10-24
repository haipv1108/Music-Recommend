package com.example.haileader.musicrecommend.musicbrainz;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

import java.util.Iterator;
import java.util.List;

/**
 * Created by haileader on 07/10/16.
 */
public class AlbumTripleBuilder {
    private Model _model;

    public AlbumTripleBuilder(Model model)
    {
        _model = model;
    }

    public void addAlbums(List albums)
    {
        for (Iterator iter = albums.iterator(); iter.hasNext();)
        {
            Album element = (Album) iter.next();
            addAlbum(element);
        }
    }

    public void addAlbum(Album album)
    {
        Resource a = makeAlbumResource( makeAlbumId( album.getId() ) );

        conditionallyAddProperty(a, DC.title, album.getName(), true);
        conditionallyAddProperty(a, MM.releaseType, album.getReleaseType(), false);
        conditionallyAddProperty(a, MM.releaseType, album.getReleaseType(), false);

        Artist artist = album.getArtist();
        if (artist != null)
        {
            a.addProperty(DC.creator, addArtist(artist));
        }

    }

    private Resource addArtist(Artist artist)
    {
        Resource a = makeArtistResource( makeArtistId( artist.getId() ) );

        conditionallyAddProperty(a, DC.title, artist.getName(), true);
        return a;
    }

    private Resource makeAlbumResource(String id)
    {
        Resource album = _model.createResource(id);
        album.addProperty(RDF.type, MM.Album);
        return album;
    }

    private Resource makeArtistResource(String id)
    {
        Resource artist = _model.createResource(id);
        artist.addProperty(RDF.type, MM.Artist);
        return artist;
    }

    private void conditionallyAddProperty(Resource resource, Property p, String propertyValue, boolean literal)
    {
        if (propertyValue != null)
        {
            RDFNode node = (literal ? (RDFNode)_model.createLiteral(propertyValue) : (RDFNode)_model.createResource(propertyValue) );
            _model.add(resource, p, node);
        }
    }

    private String makeAlbumId(String id)
    {
        return "http://mm.musicbrainz.org/album/" + id;
    }

    private String makeArtistId(String id)
    {
        return "http://mm.musicbrainz.org/artist/" + id;
    }
}
