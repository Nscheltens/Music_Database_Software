package com.mycompany.music_databases_control;

import java.io.*;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import org.apache.jena.query.*;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
/**
 * @author Nicolaas Scheltens
 */

public class Database_Control {
    
    private static final java.io.PrintStream OUT = System.out;
    private final  Model model = setModel();
    private final String root = "http://somewhere/Music#";
    private Resource Artist, Album, Song;
    private Resource Released, Sings, Appears_on;
    private Resource Name, Path, Date, Rip, Filetype, TrackNum;
    private final Property ReleasedP, SingsP, Appears_onP;
    private final Property NameP, PathP, DateP, RipP, FiletypeP, TrackNumP;
    
    public Database_Control(){
       
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
    }
    /**
     * 
     * @return 
     */
    public Model setModel(){
        OUT.println("Starting Database\nSetting model");
        InputStream in = null;
        Model model = null;
        try {
            model = ModelFactory.createDefaultModel();
            CharsetDecoder decoder = Charset.forName("windows-1252").newDecoder();
            in = new FileInputStream(new File("Database.ttl"));
            InputStreamReader inR = new InputStreamReader(in, decoder);
            model.read(inR, null, "TTL");
            in.close();
        } catch (FileNotFoundException ex) {
            System.err.println("No valid Database detectied, Creating new one");
            if (createDatabase(model)){
                try {
                    model = ModelFactory.createDefaultModel();
                    in = new FileInputStream(new File("Database.ttl"));
                    model.read(in, null, "TTL");
                    in.close();
                } catch(Exception e) {
                    System.err.println("error in setting model1");
                    System.err.println(e.toString());
                }
            }
        } catch (Exception ex){
            System.err.println("error in setting model2");
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
                System.out.println("Successfully added [Artist]"+artistList[0]);
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
        if(!checkAlbum(removeSpaces(albumList[0]))){
            Resource album = model.createResource(root + removeSpaces(albumList[0])).addProperty(RDF.type, Album)
                    .addLiteral(NameP, albumList[0])
                    .addLiteral(PathP, albumList[1])
                    .addLiteral(DateP, albumList[2])
                    .addLiteral(RipP, albumList[4])
                    .addLiteral(FiletypeP, albumList[5]);
            model.getResource(root + removeSpaces(albumList[3])).addProperty(ReleasedP, album);
            if(writeModel()){
                System.out.println("Successfully added [Album]"+albumList[0]);
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
        if(!checkSong(songName)){
            Resource song = model.createResource(root + songName).addProperty(RDF.type, Song)
                    .addProperty(Appears_onP, root + removeSpaces(SongList[3]))
                    .addLiteral(NameP, SongList[0])
                    .addLiteral(PathP, SongList[1])
                    .addLiteral(TrackNumP, SongList[2]);
            model.getResource(root + removeSpaces(SongList[4])).addProperty(SingsP, song);
            if(writeModel()){
                System.out.println("Successfully added [Song]"+SongList[0]);
                return true;
            }
        }
        return false;
    }
    private String removeSpaces(String s){
        String[] sp = s.split("[\\u2019,\\u0026]");
        String ret = sp[0];
        for(int i = 1; i < sp.length; i++){
            //System.out.println(ret);
            ret = ret +""+sp[i];
        }
        String[] sr = ret.split(" ");
        ret = sr[0];
        for(int i = 1; i < sr.length; i++){
            ret = ret +"_"+sr[i];
        }
        
        //System.out.println("Removed spaces "+ret);
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
     * // Create a new query
        String quaryName = "TimP";
        String queryString = "PREFIX root: <http://somewhere/Music#> "
                + "select ?x ?p ?o where { ?x ?p ?o }";
        Query query = QueryFactory.create(queryString);
        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        ResultSetFormatter.out(System.out, results, query);
        // Important - free up resources used running the query
        qe.close();
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
        String resultString;
        try{
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet results = qe.execSelect();
            resultString = results.next().getLiteral("o").getString();
        }catch(Exception e){
            resultString = null;
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
        String resultString;
        try{
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet results = qe.execSelect();
            resultString = results.next().getLiteral("o").getString();
        }catch(Exception e){
            resultString = null;
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
                + "root:"+songName+" a root:Song . "
                + "root:"+songName+" root:Name ?o . "
                + "}";
        String resultString;
        try{
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet results = qe.execSelect();
            resultString = results.next().getLiteral("o").getString();
        }catch(Exception e){
            resultString = null;
        }
        return resultString;
    }
}

