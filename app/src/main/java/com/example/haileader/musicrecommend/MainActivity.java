package com.example.haileader.musicrecommend;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haileader.musicrecommend.Ontology.UserProfileOntology;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.reasoner.ValidityReport.Report;
import com.hp.hpl.jena.util.FileManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private long startCountingTime;
    private long stopCountingTime;
    private float timeElapsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TextView tView = (TextView) findViewById(R.id.output_message);
//        tView.setMovementMethod(new ScrollingMovementMethod());
//        try {
//            UserProfileOntology profileOntology = new UserProfileOntology(getApplicationContext());
//            String output = profileOntology.loadOntology();
//            tView.setText(output);
//            profileOntology.loadUserInfo();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //loadUserInfo();

        new ArtistMusicBrainz().execute();

    }


    public class ArtistMusicBrainz extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLogAndToast("Excute");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String queryString = "PREFIX mo:\n" +
                    "<http://purl.org/ontology/mo/>\n" +
                    "SELECT ?artist\n" +
                    "WHERE {?artist a\n" +
                    "mo:MusicArtist}";
            Query query = QueryFactory.create(queryString);
            QueryExecution queryExecution = QueryExecutionFactory.createServiceRequest("http://dbtune.org/musicbrainz/sparql", query);
            try {
                ResultSet resultSet = queryExecution.execSelect();
                while (resultSet.hasNext()){
                    QuerySolution solution = resultSet.nextSolution();
                    showLogAndToast(solution.toString());
                }
            } finally {
                queryExecution.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showLogAndToast("Finish!");
        }
    }

    public void loadUserInfo(){
        startCountingTime= System.currentTimeMillis();

        Model model = FileManager.get().loadModel("assets/music_ontology.owl");
        showLogAndToast(model.toString());
        String queryString = "PREFIX ns0: <http://csc750/p2/reference.owl#> " +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "SELECT * WHERE { " +
                "?p rdfs:comment ?comment ." +
                "}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()){
                QuerySolution solution = results.nextSolution();
                showLog(solution.toString());
                Literal literal = solution.getLiteral("comment");
                //showLogAndToast(literal.getString());
            }
        } finally {
            qexec.close();
        }

        stopCountingTime = System.currentTimeMillis()-startCountingTime;
        float timeElapsed2 = stopCountingTime;
        timeElapsed = timeElapsed2/1000;
        showLogAndToast("Load time: " + timeElapsed);
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
