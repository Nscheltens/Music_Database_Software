/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.music_databases_control;

import java.io.*;
//import java.util.Iterator;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
//import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
//import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
//import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
/**
 * @author Nicolaas Scheltens
 */

public class Database_Control {
    
    private Model model = setModel();
    private final String root = "http://somewhere/Music#";
    private Resource Artist, Album, Song;
    private Resource Released, Sings, Appears_on;
    private Resource Name, Path, Date, Rip, Filetype, TrackNum;
    private final Property ReleasedP, SingsP, Appears_onP;
    private final Property NameP, PathP, DateP, RipP, FiletypeP, TrackNumP;
    
    public Database_Control(){
        
        System.out.println("Hello World!");
        
        Artist = model.getResource(root + "Artist");
        Album = model.getResource(root + "Album");
        Song = model.getResource(root + "Song");
        ReleasedP = model.createProperty(root + "Released");
        SingsP = model.createProperty(root + "Sings");
        Appears_onP = model.createProperty(root + "AppearsOn");
        
        NameP = model.createProperty(root + "Name");
        PathP = model.createProperty(root + "Path");
        DateP = model.createProperty(root + "Date");
        RipP = model.createProperty(root + "Rip");
        FiletypeP = model.createProperty(root + "Filetype");
        TrackNumP = model.createProperty(root + "TrackNum");
        
        
        // Create a new query
        String quaryName = "TimP";
        String queryString = "PREFIX root: <http://somewhere/Music#> "
                + "select ?o where { root:"+quaryName+" root:Name ?o }";
 
        Query query = QueryFactory.create(queryString);
 
        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
 
        // Output query results 
        //System.out.println(checkArtist(quaryName));
        ResultSetFormatter.out(System.out, results, query);
 
        // Important - free up resources used running the query
        qe.close();
    }
    /**
     * 
     * @return 
     */
    public Model setModel(){
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
        System.out.println("model set successful!");
        return model;
    }
    /**
     * 
     * @param model
     * @return 
     */
    public boolean createDatabase(Model model){
        System.out.println("Creating database in turtle format!");
        File file = new File("Database.ttl");
        FileWriter writer = null;
        try {
            file.createNewFile();
            writer = new FileWriter(file);
        } catch (IOException ex) {
            return false;
        }
        String prefixA = "http://somewhere/Music#";
        model.setNsPrefix("root", prefixA);
        model.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        model.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
        //Create classes
        Artist = model.createResource(prefixA + "Artist").addProperty(RDF.type, OWL.Class);
        Album = model.createResource(prefixA + "Album").addProperty(RDF.type, OWL.Class);
        Song = model.createResource(prefixA + "Song").addProperty(RDF.type, OWL.Class);
        //Create object properties
        Released = model.createResource(prefixA + "Released").addProperty(RDF.type, OWL.ObjectProperty)
                .addProperty(RDFS.domain, Artist)
                .addProperty(RDFS.range, Album);
        Sings = model.createResource(prefixA + "Sings").addProperty(RDF.type, OWL.ObjectProperty)
                .addProperty(RDFS.domain, Artist)
                .addProperty(RDFS.range, Song);
        Appears_on = model.createResource(prefixA + "AppearsOn").addProperty(RDF.type, OWL.ObjectProperty)
                .addProperty(RDFS.domain, Song)
                .addProperty(RDFS.range, Album);
        
        //Create data properties
        Name = model.createResource(prefixA + "Name").addProperty(RDF.type, OWL.DatatypeProperty);
        Path = model.createResource(prefixA + "Path").addProperty(RDF.type, OWL.DatatypeProperty);
        Date = model.createResource(prefixA + "Date").addProperty(RDF.type, OWL.DatatypeProperty);
        Rip = model.createResource(prefixA + "Rip").addProperty(RDF.type, OWL.DatatypeProperty);
        Filetype = model.createResource(prefixA + "Filetype").addProperty(RDF.type, OWL.DatatypeProperty);
        TrackNum = model.createResource(prefixA + "TrackNum").addProperty(RDF.type, OWL.DatatypeProperty);

        model.write(writer, "TTL");
        try {
            writer.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    public boolean writeModel(){
        File file = new File("Database.ttl");
        FileWriter writer = null;
        try {
            //file.createNewFile();
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
    
    public int quaryDatabase(String queryString){
        try{
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet results = qe.execSelect();
            ResultSetFormatter.out(System.out, results, query);
            qe.close();
        } catch(Exception e){
            return 1;
        }
        return 0;
    }
    
    /**
     * 
     * @param songName
     * @return 
     */
    public boolean checkSong(String songName){
        String modelQuery = getSong(removeSpaces(songName));
        return songName.equals(modelQuery);
    }
    /**
     * 
     * @param artistName
     * @return 
     */
    public boolean checkArtist(String artistName){
        String modelQuery = getArtist(removeSpaces(artistName));
        return artistName.equals(modelQuery);
    }
    /**
     * 
     * @param albumName
     * @return 
     */
    public boolean checkAlbum(String albumName){
        String modelQuery = getAlbum(removeSpaces(albumName));
        return albumName.equals(modelQuery);
    }
    
    /**
     * Adds an artist of the name artistName to the database. Returns false if
     * any errors occur, true if none occur. Function utilizes checkArtist() and
     * writeModel() functions.
     * @param artistList
     * @return
     */
    public boolean addArtist(String[] artistList){
        if(!checkArtist(removeSpaces(artistList[0]))){
            model.createResource(root + removeSpaces(artistList[0])).addProperty(RDF.type, Artist)
                    .addLiteral(NameP, artistList[0])
                    .addLiteral(PathP, artistList[1]);
            if(writeModel()){
                return true;
            }
        }
        return false;
    }
    /**
     * Adds an album of the name albumName to the database and connects to the 
     * resource artist of the name artistName. Returns false if
     * any errors occur, true if none occur. Function utilizes checkAlbum() and
     * writeModel() functions.
     * @param albumList
     * @return 
     */
    public boolean addAlbum(String[] albumList){
        System.out.print("adding "+albumList[0]);
        if(!checkAlbum(removeSpaces(albumList[0]))){
            Resource album = model.createResource(root + removeSpaces(albumList[0])).addProperty(RDF.type, Album)
                    .addLiteral(NameP, albumList[0])
                    .addLiteral(PathP, albumList[1])
                    .addLiteral(DateP, albumList[2])
                    .addLiteral(RipP, albumList[4])
                    .addLiteral(FiletypeP, albumList[5]);
            model.getResource(root + removeSpaces(albumList[3])).addProperty(ReleasedP, album);
            if(writeModel()){
                return true;
            }
        }
        return false;
    }
    /**
     * Adds a song of the name songName to the database and connects to the 
     * resource artist of the name artistName and to the album of the name albumName.
     * Returns false if any errors occur, true if none occur. Function utilizes 
     * checkSong() and writeModel() functions.
     * @param SongList
     * @return 
     */
    public boolean addSong(String[] SongList){
        String songName = removeSpaces(SongList[0]);
        System.out.print("adding "+SongList[0]);
        if(!checkSong(songName)){
            Resource song = model.createResource(root + songName).addProperty(RDF.type, Song)
                    .addProperty(Appears_onP, root + removeSpaces(SongList[3]))
                    .addLiteral(NameP, SongList[0])
                    .addLiteral(PathP, SongList[1])
                    .addLiteral(TrackNumP, SongList[2]);
            model.getResource(root + removeSpaces(SongList[4])).addProperty(SingsP, song);
            if(writeModel()){
                return true;
            }
        }
        return false;
    }
    private String removeSpaces(String s){
        String[] sr = s.split(" ");
        String ret = sr[0];
        for(int i = 1; i < sr.length; i++){
            ret = ret +"_"+sr[i];
        }
        String[] sp = ret.split("[\\u2019,]");
        ret = sp[0];
        for(int i = 1; i < sp.length; i++){
            System.out.println(ret);
            ret = ret +"_"+sp[i];
        }
        System.out.println("Removed spaces "+ret);
        return ret;
    }
    
    public boolean removeArtist(String artistName){
        
        return false;
    }
    public boolean removeAlbum(String albumName){
        
        return false;
    }
    public boolean removeSong(String songName){
        
        return false;
    }
    
    /**
     * 
     * @param artistName
     * @return 
     */
    public String getArtist(String artistName){
        //add error handeling
        String queryString = "PREFIX root: <http://somewhere/Music#> "
                + "select ?o where { "
                + "root:"+artistName+" a root:Artist . "
                + "root:"+artistName+" root:Name ?o . "
                + "}";
        //System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        String resultString;
        try(QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qe.execSelect();
            try{
                resultString = results.next().getLiteral("o").getString();
            }catch(Exception e){
                resultString = null;
            }
        }
        return resultString;
    }
    /**
     * 
     * @param albumName
     * @return 
     */
    public String getAlbum(String albumName){
        String queryString = "PREFIX root: <http://somewhere/Music#> "
                + "select ?o where { "
                + "root:"+albumName+" a root:Album . "
                + "root:"+albumName+" root:Name ?o . "
                + "}";
        System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        String resultString;
        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qe.execSelect();
            try{
                resultString = results.next().getLiteral("o").getString();
            }catch(Exception e){
                resultString = null;
            }
        }
        return resultString;
    }
    /**
     * 
     * @param songName
     * @return 
     */
    public String getSong(String songName){
        String queryString = "PREFIX root: <http://somewhere/Music#> "
                + "select ?o where { "
                + "root:"+songName+" a root:Album . "
                + "root:"+songName+" root:Name ?o . "
                + "}";
        System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        String resultString;
        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qe.execSelect();
            try{
                resultString = results.next().getLiteral("o").getString();
            }catch(Exception e){
                resultString = null;
            }
        }
        return resultString;
    }
    
    public String[] getAllArtists(){
        
        return null;
    }
    public String[] getAllAlbums(String ArtistName){
        
        return null;
    }
    public String[] getAllSongs(String albumName){
        
        return null;
    }
    
}
