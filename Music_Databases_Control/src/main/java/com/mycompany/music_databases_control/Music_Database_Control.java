/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.music_databases_control;

//import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Cole
 */
public class Music_Database_Control{
    private static final java.io.PrintStream OUT = System.out;
    private Database_Control database = new Database_Control();
    private Data_Scanner scan = new Data_Scanner();
    private final Updater UPPER = new Updater();
    
    public Music_Database_Control() {
        //(new Music_Database_Control()).start();
        Control_Update();
        Input();
    }
    
    public static void main(String[] args){
        for(String arg: args){
            OUT.println(arg);
        }
        Music_Database_Control d = new Music_Database_Control();
    }
    private void Control_Update(){
        OUT.println("this Tread will wait and refresh the database");
        UPPER.Database_Updater();
    }
    private void launchScanner(){
    }
    private void luanchTranscoder(){
        
    }
    private void launchControl(){
        
    }
    private int exitProgram(){
        OUT.println("shutting down scheduler");
        UPPER.Updater_Shutdown();
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
        return 0;
    }
    private int findQuary(String[] params){
        OUT.println(params.length);
        if(params.length < 3) return -1;
        String name = params[2];
        if(params.length > 3){
            for(int i = 3; i < params.length; i++){
                name = name +" "+params[i];
            }
        }
        OUT.println("this is the name "+name);
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
    private int scanFile(String[] Params){
        //build Data_Scanner class
        return 0;
    }
    private int updateDatabase(){
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
    
    private void Input(){
        OUT.println("this Tread will wait for input of user actions");
        Scanner in = new Scanner(System.in);
        OUT.print(">");
        while(in.hasNext()){
            String input = in.nextLine();
            //OUT.println(input);
            int des = Parse_Eval(input);
            if(des == 1) return;
            else if(des == -1) OUT.println("Incorrect usage type help to see commands");
            OUT.print(">");
        }
    }
    /**
     * 
     */
    private class Updater{
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        public void Database_Updater(){
            final Runnable updater = new Runnable() {
                public void run() {OUT.println("updating database");}
            };
            // change timing to a more reasonable time to update database
            final ScheduledFuture<?> updateHandle = scheduler.scheduleAtFixedRate(updater, 10, 10, TimeUnit.SECONDS);
            //remove all below after Input method complete
            /*
            scheduler.schedule(new Runnable() {
                public void run() { System.out.println(updateHandle.cancel(true)); }
            }, 30, TimeUnit.SECONDS);
            scheduler.schedule(new Runnable() { public void run() { scheduler.shutdown(); } }, 31, TimeUnit.SECONDS);
            */
        }
        public void Updater_Shutdown(){
            scheduler.shutdown();
        }
    }
}
