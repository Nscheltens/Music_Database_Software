/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.database_control;

import java.io.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;
/**
 *
 * @author Nicolaas Scheltens
 */
public class Database_Control {
    public static void main(String[] args){
        
        System.out.println("Hello World!");
        
        Model model = setModel();
        // Create a new query
        String queryString = "select * where { ?s ?p ?o }";
 
        Query query = QueryFactory.create(queryString);
 
        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
 
        // Output query results 
        ResultSetFormatter.out(System.out, results, query);
 
        // Important - free up resources used running the query
        qe.close();   
    }
    /**
     * 
     * @return 
     */
    public static Model setModel(){
        InputStream in = null;
        Model model = null;
        try {
            // Create an empty in-memory model and populate it from the graph
            model = ModelFactory.createDefaultModel();
            in = new FileInputStream(new File("Database.ttl"));
            
            model.read(in, null, "TTL"); // null base URI, since model URIs are absolute
            in.close();
        } catch (FileNotFoundException ex) {
            System.err.println("No valid Database detectied, Creating new one");
            if (createDatabase(model)){
                try {
                    // Create an empty in-memory model and populate it from the graph
                    model = ModelFactory.createDefaultModel();
                    in = new FileInputStream(new File("Database.ttl"));
            
                    model.read(in, null, "TTL"); // null base URI, since model URIs are absolute
                    in.close();
                } catch(Exception e) {
                    System.err.println(e.toString());
                }
            }
        } catch (Exception ex){
            System.err.println(ex.toString());
        }
        return model;
    }
    /**
     * 
     * @param model
     * @return 
     */
    public static boolean createDatabase(Model model){
        System.out.println("Creating database in turtle format!");
        File file = new File("Database.ttl");
        FileWriter writer = null;
        try {
            file.createNewFile();
            writer = new FileWriter(file);
        } catch (IOException ex) {
            return false;
        }
        model.write(writer, "TTL");
        try {
            writer.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    public static boolean checkSong(String artistName, String albumName, String songName){
        String queryString = null;
        if(false){
            
        }
        return false;
    }
    public static boolean checkArtist(String artistName){
        return false;
    }
    
    public static boolean checkAlbum(String artistName){
        return false;
    }
    
    public static boolean addSong(String songName, String albumName){
        
        return false;
    }
    
}
