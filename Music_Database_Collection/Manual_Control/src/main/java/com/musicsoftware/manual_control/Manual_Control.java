package com.musicsoftware.manual_control;

//import java.util.Calendar;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.musicsoftware.database_control.Database_Control;
import com.musicsoftware.database_scanner.Database_Scanner;
import java.io.FileWriter;
import java.io.StringWriter;

/**
 *
 * @author Nick Scheltens
 */
public class Manual_Control{
    
    // get from JSON
    public static final String OS = "Linux";
    public String DefaultDirectory;
    public String DatabaseDirectory;
    
    private static final java.io.PrintStream OUT = System.out;
    private static Database_Control database;
    private static Database_Scanner SCAN;
    //private final Updater UPPER = new Updater();
    
    public Manual_Control(String[] args) {
        //(new Music_Database_Control()).start();
        try{
            Object obj = new JSONParser().parse(new FileReader("Info.JSON"));
            JSONObject jo = (JSONObject) obj;
            
            DefaultDirectory = (String) jo.get("Default_Directory");
            DatabaseDirectory = (String) jo.get("Database_Directory");
            
        }catch(FileNotFoundException e){
            OUT.println("No JSON found, createing new one.");
            if(createJSON()){
                OUT.println("JSON successfully created");
                try{
                Object obj = new JSONParser().parse(new FileReader("Info.JSON"));
                JSONObject jo = (JSONObject) obj;
            
                DefaultDirectory = (String) jo.get("Default_Directory");
                DatabaseDirectory = (String) jo.get("Database_Directory");
                }catch(Exception ex){
                    
                }
                
            }else{
                OUT.println("Unable to create JSON,exiting");
                return;
            }
        }catch(IOException e){
            
        }catch(ParseException e){
            
        }
        database = new Database_Control(DatabaseDirectory);
        SCAN = new Database_Scanner(database, DefaultDirectory);
        Input(args);
    } 
    public static void main(String[] args){
        OUT.println("Starting Database Control");
        //OUT.println(System.getProperty("os.name"));
        Manual_Control d = new Manual_Control(args);
    }
    private boolean createJSON(){
        JSONObject obj = new JSONObject();

        obj.put("Default_Directory",requestInfo("Music"));
        obj.put("Database_Directory",requestInfo("Database"));

        try(FileWriter file = new FileWriter("Info.JSON")){
            file.write(obj.toJSONString());
        }catch(Exception ex){
            OUT.println("File creation failed exiting");
            System.err.println(ex.getStackTrace());
            return false;
        }
        return true;
    }
    private String requestInfo(String info){
        Scanner in = new Scanner(System.in);
        String ans = null;
        OUT.println("Set Default "+info+" Directory");
        while(in.hasNext()){
            ans = in.nextLine();
        }
        return ans;
    }
    
    private void Input(String[] args){
        //OUT.println("this Tread will wait for input of user actions");
        Scanner in = new Scanner(System.in);
        OUT.print(">>");
        while(in.hasNext()){
            String input = in.nextLine();
            //OUT.println(input);
            int des = Parse_Eval(input);
            if(des == 1){
                in.close();
                return;
            }
            else if(des == -1) OUT.println("Incorrect usage type help to see commands");
            OUT.print(">>");
        }
    }
    /**
     * 
     * @return 
     */
    private int exitProgram(){
        OUT.println("shuting down scheduler");
        //UPPER.Updater_Shutdown();
        return 1;
    }
    private int displayHelp(){
        //OUT.println();
        OUT.println("/***************************************************************************************************/");
        OUT.println("Usage for Music Database");
        OUT.println("   scan [Artist/Album/Song] [FileLocation]        Scan file/folder to add to music database.");
        OUT.println();
        OUT.println("   [SPAQL Quary]                                  Executes SPARQ Quary on database and displays result.");
        OUT.println();
        OUT.println("   find [Artist/Album/Song] [Name]                Searches database and returns true or false if Name\n"
                  + "                                                  is in the database.");
        OUT.println();
        OUT.println("   refresh                                        preforms automatic update on database from default\n"
                  + "                                                  directory.");
        OUT.println();
        OUT.println("   exit                                           stops database.");
        OUT.println("/***************************************************************************************************/");
        return 0;
    }
    private int runQuary(String Quary){
        //Handel non zero int
        int check = database.quaryDatabase(Quary);
        return check;
    }
    /**
     * executes the find command from the controller
     * @param params
     * @return 
     */
    private int findQuary(String[] params){
        if(params.length < 3) return -1;
        String name = params[2];
        if(params.length > 3){
            for(int i = 3; i < params.length; i++){
                name = name +"_"+params[i];
            }
        }
        boolean stat;
        switch(params[1]){
            default: return -1;
            case "Artist": stat = database.checkArtist(name);
                OUT.println(stat);
                return 0;
            case "Album": stat = database.checkAlbum(name);
                OUT.println(stat);
                return 0;
            case "Song": stat = database.checkSong(name);
                OUT.println(stat);
                return 0;
        }
    }
    private int scanFile(String[] params){
        //build Data_Scanner class
        String path = params[2];
        for(int i = 3;i < params.length; i++){
            path = path +" "+ params[i];
        } 
        switch(params[1]){
            default: return -1;
            case "Artist": SCAN.ScanForArtists(path, false);
                return 0;
            case "Album": SCAN.ScanForAlbums(path, false);
                return 0;
            case "Song": SCAN.ScanForSongs(path, false, "Unasigned", "Unasigned");
                return 0;
        }
    }
    private int updateDatabase(){
        SCAN.ScanForArtists(DefaultDirectory, true);
        OUT.println("Database updated!");
        return 0;
    }
    
    private int Parse_Eval(String statment){
        int ReturnInt;
        String[] tokens = statment.split(" ");
        switch(tokens[0]){
            default: ReturnInt = -1;
                return ReturnInt;
            case "help": ReturnInt = displayHelp();
                return ReturnInt;
            case "scan": ReturnInt = scanFile(tokens);
                return ReturnInt;
            case "select": ReturnInt = runQuary(statment);
                return ReturnInt;
            case "find": ReturnInt = findQuary(tokens);
                return ReturnInt;
            case "refresh": ReturnInt = updateDatabase();
                return ReturnInt;
            case "exit": ReturnInt = exitProgram();
                return ReturnInt;
        }
    }
}