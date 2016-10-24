package com.example.haileader.musicrecommend.musicbrainz;

/**
 * Created by haileader on 07/10/16.
 */
public class Templates {
    private static final String TOP = "<?xml version=\"1.0\"?>" +
            "<rdf:RDF xmlns:rdf =\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" +
            " xmlns:dc  = \"http://purl.org/dc/elements/1.1/\"" +
            " xmlns:mq  = \"http://musicbrainz.org/mm/mq-1.1#\"" +
            " xmlns:mm  = \"http://musicbrainz.org/mm/mm-2.1#\">";

    private static final String TAIL = "</rdf:RDF>";

    public static final String AUTHENTICATE = TOP +
            "<mq:AuthenticateQuery>" +
            "   <mq:username>{0}</mq:username>" +
            "</mq:AuthenticateQuery>" +
            TAIL;

    public static final String GET_CD_INFO = TOP +
            "<mq:GetCDInfo>" +
            "   <mq:depth>{0}</mq:depth>" +
            "   <mm:cdindexid>{1}</mm:cdindexid>" +
            "</mq:GetCDInfo>" +
            TAIL;

    public static final String TRACK_INFO_FROM_TRM_ID = TOP +
            "<mq:TrackInfoFromTRMId>" +
            "   <mm:trmid>{0}</mm:trmid>" +
            "   <mq:artistName>{1}</mq:artistName>" +
            "   <mq:albumName>{2}</mq:albumName>" +
            "   <mq:trackName>{3}</mq:trackName>" +
            "   <mm:trackNum>{4}</mm:trackNum>" +
            "   <mm:duration>{5}</mm:duration>" +
            "</mq:TrackInfoFromTRMId>" +
            TAIL;

    public static final String QUICK_TRACK_INFO_FROM_TRACK_ID = TOP +
            "<mq:QuickTrackInfoFromTrackId>" +
            "   <mm:trackid>{0}</mm:trackid>" +
            "   <mm:albumid>{1}</mm:albumid>" +
            "</mq:QuickTrackInfoFromTrackId>" +
            TAIL;

    public static final String FIND_ARTIST = TOP +
            "<mq:FindArtist>" +
            "   <mq:depth>{0}</mq:depth>" +
            "   <mq:artistName>{1}</mq:artistName>" +
            "   <mq:maxItems>{2}</mq:maxItems>" +
            "</mq:FindArtist>" +
            TAIL;

    public static final String FIND_ALBUM = TOP +
            "<mq:FindAlbum>" +
            "   <mq:depth>{0}</mq:depth>" +
            "   <mq:maxItems>{1}</mq:maxItems>" +
            "   <mq:albumName>{2}</mq:albumName>" +
            "</mq:FindAlbum>" +
            TAIL;

    public static final String FIND_TRACK = TOP +
            "<mq:FindTrack>" +
            "   <mq:depth>{0}</mq:depth>" +
            "   <mq:maxItems>{1}</mq:maxItems>" +
            "   <mq:trackName>{2}</mq:trackName>" +
            "</mq:FindTrack>" +
            TAIL;

    public static final String FIND_DISTINCT_TRM_ID = TOP +
            "<mq:FindDistinctTRMID>" +
            "   <mq:depth>{0}</mq:depth>" +
            "   <mq:artistName>{1}</mq:artistName>" +
            "   <mq:trackName>{2}</mq:trackName>" +
            "</mq:FindDistinctTRMID>" +
            TAIL;

    public static final String GET_ARTIST_BY_ID = "{0}/mm-2.1/artist/{1}/{2}" ;

    public static final String GET_ALBUM_BY_ID = "{0}/mm-2.1/album/{1}/{2}" ;

    public static final String GET_TRACK_BY_ID = "{0}/mm-2.1/track/{1}/{2}" ;

    public static final String GET_TRACK_BY_TRM_ID = "{0}/mm-2.1/trmid/{1}/{2}" ;

    public static final String FILE_INFO_LOOKUP = TOP +
            "<mq:FileInfoLookup>" +
            "   <mm:trmid>{0}</mm:trmid>" +
            "   <mq:artistName>{1}</mq:artistName>" +
            "   <mq:albumName>{2}</mq:albumName>" +
            "   <mq:trackName>{3}</mq:trackName>" +
            "   <mm:trackNum>{4}</mm:trackNum>" +
            "   <mm:duration>{5}</mm:duration>" +
            "   <mq:fileName>{6}</mq:fileName>" +
            "   <mm:artistid>{7}</mm:artistid>" +
            "   <mm:albumid>{8}</mm:albumid>" +
            "   <mm:trackid>{9}</mm:trackid>" +
            "   <mq:maxItems>{10}</mq:maxItems>" +
            "</mq:FileInfoLookup>" +
            TAIL;
}
