package com.example.haileader.musicrecommend.Ontology;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

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
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.util.FileManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by haileader on 16/09/16.
 */
public class UserProfileOntology {
    private static final String TAG = UserProfileOntology.class.getCanonicalName();

    private Context mContext;

    public UserProfileOntology(Context context) throws IOException {
        this.mContext = context;
    }

    public String loadOntology(){
        String output = "";
        Model schema = ModelFactory.createDefaultModel();//FileManager.get().loadModel("file:assets/PO_new.owl");
        Model model = ModelFactory.createDefaultModel();//FileManager.get().loadModel("file:assets/instance.rdf");
        InputStream fin;

        try {
            AssetManager assetManager = mContext.getAssets();
            fin = assetManager.open("PO_new.owl");
            schema.read(fin,null);
            fin.close();

		/*
			1. 2Item_Instance.rdf - Has two distinct "hasItem" properties with each item explicitly stated different to each other.
			2. 2Orders_Instance.rdf - Has two "OrderNumber" properties under PurchaseOrder.
			3. normal_instance.rdf - Working instance of ontology PO_new.owl
			4. PO_new.owl - contains Purchase Order Ontology.
			5. StringOrder_Instance - Has a string instead of Integer as Order number.
		*/

            model = FileManager.get().loadModel("assets/normal_instance.rdf");
//			model = FileManager.get().loadModel("assets/2Orders_Instance.rdf");
//			model = FileManager.get().loadModel("assets/2Item_Instance.rdf");
//			model = FileManager.get().loadModel("assets/StringOrder_Instance.rdf");
        } catch (IOException e){
            e.printStackTrace();
        }

        Reasoner reasoner = ReasonerRegistry.getOWLMiniReasoner();
        reasoner = reasoner.bindSchema(schema);

        InfModel infmodel = ModelFactory.createInfModel(reasoner, model);
        ValidityReport validity = infmodel.validate();
        if (validity.isValid()) {
            output += "Order Confirmed\n";
            output = getInfo(infmodel, model, output);
        } else {
            for (Iterator<ValidityReport.Report> iterator = validity.getReports(); iterator.hasNext();) {
                ValidityReport.Report report = iterator.next();
                output += "ERROR: " + report.getType();
                output += " - " + report.getDescription();
            }
        }

        return output;
    }

    private String getInfo(InfModel infmodel, Model data, String output) {
        StmtIterator iter = data.listStatements();
        float unitPrice = 0;
        int quantity = 0;
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            // Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            String propertyValue = stmt.getObject().toString();
            if(propertyValue.indexOf('^') != -1 )
                propertyValue = propertyValue.substring(0,propertyValue.indexOf('^'));
            if (predicate.toString().contains("OrderNumber"))
                output += "OrderNumber = " + propertyValue + "\n";
            else if (predicate.toString().contains("OrderDate"))
                output += "OrderDate = " + propertyValue + "\n";
            else if (predicate.toString().contains("FirstName"))
                output += "FirstName = " + propertyValue + "\n";
            else if (predicate.toString().contains("LastName"))
                output += "LastName = " + propertyValue + "\n";
            else if (predicate.toString().contains("ItemName"))
                output += "ItemName = " + propertyValue + "\n";
            else if (predicate.toString().contains("Quantity")) {
                output += "Quantity = " + propertyValue + "\n";
                quantity = Integer.valueOf(propertyValue);
            } else if (predicate.toString().contains("shippingAddress"))
                output += "ShippingAddress: " + "\n";
            else if(predicate.toString().contains("Street"))
                output += "\t Street = " + propertyValue + "\n";
            else if(predicate.toString().contains("City"))
                output += "\t City = " + propertyValue + "\n";
            else if(predicate.toString().contains("State"))
                output += "\t State = " + propertyValue + "\n";
            else if(predicate.toString().contains("Zip"))
                output += "\t Zip = " + propertyValue + "\n";
            else if (predicate.toString().contains("unitPrice"))
                unitPrice = Float.valueOf(propertyValue);
        }
        output += "TotalPrice = " + quantity * unitPrice + "\n";
        return output;
    }

    public void loadUserInfo(){
        Model model = FileManager.get().loadModel("assets/normal_instance.rdf");
        showLog(model.toString());
        String queryString = "PREFIX ns0: <http://csc750/p2/reference.owl#> " +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                "SELECT * WHERE { " +
                "?p ns0:OrderDate ?firstname ." +
                "}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()){
                QuerySolution solution = results.nextSolution();
                showLog(solution.toString());
                Literal literal = solution.getLiteral("firstname");
                showLog(literal.getString());
            }
        } finally {
            qexec.close();
        }
//        String queryString =
//                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
//                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
//                        "PREFIX mo: <http://purl.org/ontology/mo/> " +
//                        "SELECT * WHERE { " +
//                        " ?p mo:MusicArtist ?interest ." +
//                        "}";
//        Query query = QueryFactory.create(queryString);
//        QueryExecution qexec = QueryExecutionFactory.create(query, model);
//        try {
//            ResultSet results = qexec.execSelect();
//            while (results.hasNext()){
//                QuerySolution solution = results.nextSolution();
//                showLogAndToast(solution.toString());
//                Resource resource = (Resource) solution.get("interest");
//                showLogAndToast(resource.getURI());
//                //Literal name = solution.getLiteral("interest");
//                //showLogAndToast(name.getString());
//            }
//        } finally {
//            qexec.close();
//        }
    }

    private void showLog(String message){
        Log.d(TAG, message);
    }

}
