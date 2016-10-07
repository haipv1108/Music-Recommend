package com.example.haileader.musicrecommend;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.haileader.musicrecommend.adapter.ArtistsAdapter;
import com.example.haileader.musicrecommend.adapter.TracksAdapter;
import com.example.haileader.musicrecommend.model.Artist;
import com.example.haileader.musicrecommend.model.Track;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final int ARTIST = 1;
    private static final int TRACK = 2;

    private EditText mSearchEdt;
    private RecyclerView mRecyclerResult;

    private List<Track> mTracks;
    private TracksAdapter mTracksAdapter;
    private List<Artist> mArtists;
    private ArtistsAdapter mArtistsAdapter;

    private int mTypeSearch = ARTIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initRecyclerView();
        fakeData();
    }

    private void initUI(){
        mSearchEdt = (EditText)findViewById(R.id.search_edt);
        Button mSearchBtn = (Button) findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(this);
        mRecyclerResult = (RecyclerView)findViewById(R.id.search_recycler);
    }

    private void initRecyclerView(){
        mArtists = new ArrayList<>();
        mArtistsAdapter = new ArtistsAdapter(mArtists);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerResult.setLayoutManager(mLayoutManager);
        mRecyclerResult.setItemAnimator(new DefaultItemAnimator());
//        if(mTypeSearch == ARTIST){
            mRecyclerResult.setAdapter(mArtistsAdapter);
//        }else{
//            mTracks = new ArrayList<>();
//            mTracksAdapter = new TracksAdapter(mTracks);
//            mRecyclerResult.setAdapter(mTracksAdapter);
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_btn:
                if(mSearchEdt.getText().toString().trim().equals("")){
                    showLogAndToast("Please input keyword to search!");
                }else{
                    searchData();
                }
                break;
        }
    }

    public void onSearchTypeRadioClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.radio_artist:
                mTypeSearch = checked? ARTIST: TRACK;
                break;
            case R.id.radio_track:
                mTypeSearch = checked? TRACK: ARTIST;
                break;
        }
//        initRecyclerView();
    }

    private void searchData(){
        String keyword = mSearchEdt.getText().toString().trim();
        if(mTypeSearch == ARTIST){
            new ArtistMusicBrainz(this).execute(keyword);
        }else{
            new TrackMusicBrainz(this).execute();
        }
    }

    private class ArtistMusicBrainz extends AsyncTask<String, Void, Void>{
        private ProgressDialog progressDialog;
        private Context context;
        public ArtistMusicBrainz(Context context){
            this.context = context;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String keyword = strings[0];
            String queryString =
                    "PREFIX mo:<http://purl.org/ontology/mo/>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                    "SELECT ?artist ?name\n" +
                    "WHERE {\n" +
                    "?artist a mo:MusicArtist .\n" +
                    "?artist foaf:name ?name ." +
                    "FILTER regex(?name, \"" + keyword + "\", \"i\") .\n" +
                    "}" +
                    "LIMIT 10";
            Query query = QueryFactory.create(queryString);
            QueryExecution queryExecution = QueryExecutionFactory.createServiceRequest(Utils.MUSICBRAINZ_ENDPOINT, query);
            try {
                ResultSet resultSet = queryExecution.execSelect();
                while (resultSet.hasNext()){
                    QuerySolution solution = resultSet.nextSolution();
                    Literal artistName = solution.getLiteral("name");
                    Resource artistUri = (Resource) solution.get("artist");
                    showLogAndToast(artistName.toString() + artistUri.toString());
                    Artist artist = new Artist(artistName.toString(), artistUri.toString());
                    mArtists.add(artist);
                    mArtistsAdapter.notifyDataSetChanged();
                    //updateArtistsView(artist);
                }
            } finally {
                queryExecution.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    private void updateArtistsView(Artist artist){
        mArtists.add(artist);
        mArtistsAdapter.notifyDataSetChanged();
    }

    private void fakeData(){
        Artist artist = new Artist("Hai", "http://ssss");
        mArtists.add(artist);
         artist = new Artist("Hai", "http://ssss");
        mArtists.add(artist);
         artist = new Artist("Hai", "http://ssss");
        mArtists.add(artist);
         artist = new Artist("Hai", "http://ssss");
        mArtists.add(artist);
         artist = new Artist("Hai", "http://ssss");
        mArtists.add(artist);
        mArtistsAdapter.notifyDataSetChanged();
    }

    private class TrackMusicBrainz extends AsyncTask<Void, Void, Void>{
        private Context context;
        private ProgressDialog progressDialog;

        public TrackMusicBrainz(Context context){
            this.context = context;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String queryString =
                    "PREFIX mo:<http://purl.org/ontology/mo/>\n" +
                            "SELECT ?name\n" +
                            "WHERE {" +
                            "<http://dbtune.org/musicbrainz/resource/track/ec742dc0-ce5a-44c3-b4c4-eb614486db6e> foaf:name ?name .\n" +
                            "}";
            Query query = QueryFactory.create(queryString);
            QueryExecution queryExecution = QueryExecutionFactory.createServiceRequest(Utils.MUSICBRAINZ_ENDPOINT, query);
            try {
                ResultSet resultSet = queryExecution.execSelect();
                while (resultSet.hasNext()){
                    QuerySolution solution = resultSet.nextSolution();
                    Literal name = solution.getLiteral("name");
                    showLogAndToast(name.getString());
                }
            } finally {
                queryExecution.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLog(String message){
        Log.d(TAG, message);
    }

    private void showLogAndToast(final String message){
        showLog(message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
