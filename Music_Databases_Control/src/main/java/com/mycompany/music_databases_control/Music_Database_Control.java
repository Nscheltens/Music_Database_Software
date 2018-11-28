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
        return 0;
    }
    private int runQuary(String Quary){
        return 0;
    }
    private int scanFile(String[] fileLocation){
        return 0;
    }
    private int Parse_Eval(String statment){
        int ReturnInt;
        String[] tokens = statment.split(" \t");
        switch(tokens[0]){
            default: ReturnInt = -1;
                return ReturnInt;
            case "exit": ReturnInt = exitProgram();
                return ReturnInt;
            case "help": ReturnInt = displayHelp();
                return ReturnInt;
            case "select": ReturnInt = runQuary(statment);
                return ReturnInt;
            case "scan": ReturnInt = scanFile(tokens);
                return ReturnInt;
        }
    }
    
    private void Input(){
        OUT.println("this Tread will wait for input of user actions");
        Scanner in = new Scanner(System.in);
        OUT.print(">");
        while(in.hasNext()){
            String input = in.nextLine();
            OUT.println(input);
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
