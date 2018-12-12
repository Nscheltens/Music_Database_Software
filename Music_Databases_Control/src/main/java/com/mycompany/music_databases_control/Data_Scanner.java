/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.music_databases_control;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nick Scheltens
 */
public class Data_Scanner {
    
    // use threads
    public final String DefaultDirectory;
    private final Database_Control database;
    
    public Data_Scanner(Database_Control d, String derect){
        DefaultDirectory = derect;
        database = d;
    }
    
    public void ScanForSongs(String direct){
        System.out.println(direct);
        File directory = new File(direct);
        String[] directoryContents = directory.list();
        System.out.println("Listing files in directory");
        String[][] cutList = processSongNames(directoryContents, direct);
        if(cutList.length == 0){
            System.out.println("No correctly formated files found.");
        }else{
            handelfiles(cutList,"Song");
        }
    }
    private String[][] processSongNames(String[] list, String direct){
        int numSongs = list.length;
        int listMover = 0;
        String[][] cuttingList = new String[list.length][3];
        Pattern p = Pattern.compile(".+[{.mp3}{.flac}]");
        for (String list1 : list) {
            Matcher m = p.matcher(list1);
            if (m.matches()) {
                //System.out.println("going to cut number off "+list1);
                String[] NumRemoved = list1.split("[0-9]{2}+ ");
                //System.out.println("going to cut extention off "+NumRemoved[1]);
                String[] SongName = NumRemoved[1].split(".mp3");
                //System.out.println("adding "+SongName[0]);
                cuttingList[listMover][0] = SongName[0];
                File path = new File(direct, list1);
                //System.out.println("adding "+path.getAbsolutePath());
                cuttingList[listMover][1] = path.getAbsolutePath();
                String[] num = list1.split(" ");
                //System.out.println("adding "+num[0]);
                cuttingList[listMover][2] = num[0];
                listMover++;
            } else {
                numSongs--;
            }
        }
        //System.out.println(numSongs);
        return Arrays.copyOf(cuttingList, numSongs);
    }
    
    public void ScanForAlbums(String direct){
        System.out.println(direct);
        File directory = new File(direct);
        String[] directoryContents = directory.list();
        System.out.println("Listing files in directory");
        for(String fileName: directoryContents){
            File temp = new File(directory, fileName);
            
            System.out.println(fileName);
        }
    }
    public void ScanForArtists(String direct){
        System.out.println(direct);
        File directory = new File(direct);
        String[] directoryContents = directory.list();
        System.out.println("Listing files in directory");
        for(String fileName: directoryContents){
            File temp = new File(directory, fileName);
            
            System.out.println(fileName);
        }
    }
    
    private void AlbumNameHnadeler(String SongName){
        
    }
    private void ArtistNameHnadeler(String SongName){
        
    }
    private void handelfiles(String[][] items, String type){
        Scanner in = new Scanner(System.in);
        System.out.println("Scanner found these "+type+"s");
        System.out.println("-------------------------------------------------");
        for (String[] item : items) {
            System.out.println(item[0]);
        }
        System.out.println("-------------------------------------------------");
        System.out.println("add these files to the database? (y = yes)");
        String anw = in.next();
        if(anw.equals("y")){
            System.out.println("Adding files to the database");
        }else{
            System.out.println("Not adding files to the database");
            // give examples of proper file names
        }
    }
    
}
