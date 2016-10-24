package com.example.haileader.musicrecommend.musicbrainz;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Created by haileader on 07/10/16.
 */
public class MusicBrainzException extends RuntimeException {
    public MusicBrainzException()
    {
        super();
    }

    public MusicBrainzException(String arg0)
    {
        super(arg0);
    }

    public MusicBrainzException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
    }

    public MusicBrainzException(Throwable arg0)
    {
        super(arg0);
    }

    protected static void throwIfNecessary(int code, String message)
    {
        if (code == 204 || code == 400 || code == 404|| code == 403 || code == 500 || code == 503)
        {
            throw new MusicBrainzException("HTTP " + code + " : " + message);
        }
    }

    protected static void throwIfNecessary(Model model)
    {
        //FIXME check that we've not got an mq:Request/mq:error
    }
}
