package com.example.haileader.musicrecommend;

import android.util.Log;
import android.widget.Toast;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

/**
 * Created by haileader on 05/10/16.
 */
public class Note {

    private static final String TAG = Note.class.getCanonicalName();

    private long startCountingTime;
    private long stopCountingTime;
    private float timeElapsed;

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

    private void showLog(String message){
        Log.d(TAG, message);
    }

    private void showLogAndToast(final String message){
        showLog(message);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
