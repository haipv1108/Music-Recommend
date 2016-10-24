package com.example.haileader.musicrecommend;

import com.example.haileader.musicrecommend.musicbrainz.AlbumTripleBuilder;
import com.example.haileader.musicrecommend.musicbrainz.BeanPopulator;
import com.example.haileader.musicrecommend.musicbrainz.MusicBrainz;
import com.example.haileader.musicrecommend.musicbrainz.MusicBrainzImpl;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haileader on 07/10/16.
 */
public class RecordCollectionRDFGenerator {
    private MusicBrainz _server;

    public RecordCollectionRDFGenerator()
    {
        _server = new MusicBrainzImpl();
    }

    public void process(File input, File output) throws IOException
    {
        process(input, new FileOutputStream(output));
    }

    public void process(File input, OutputStream out) throws IOException
    {
        process(new BufferedInputStream( new FileInputStream(input) ), out);
    }

    public void process(InputStream in, OutputStream out) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String albumName = reader.readLine();
        List albums = new ArrayList();
        while (albumName != null)
        {
            Model model = _server.findAlbumByName(albumName, 4, 1);
            List returnedAlbums = BeanPopulator.getAlbums(model);

            if (returnedAlbums.size() > 0)
            {
                //assume first item is correct...
                albums.add(returnedAlbums.get(0));
            }

            albumName = reader.readLine();
        }

        Model model = ModelFactory.createDefaultModel();
        AlbumTripleBuilder tripleBuilder = new AlbumTripleBuilder(model);
        tripleBuilder.addAlbums(albums);
        model.write(out);
    }
}
