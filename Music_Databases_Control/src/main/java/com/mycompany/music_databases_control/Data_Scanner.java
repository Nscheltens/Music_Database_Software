/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.music_databases_control;

import java.io.File;
import java.util.Scanner;

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
        for(String fileName: directoryContents){
            //File temp = new File(directory, fileName);
            System.out.println(fileName);
        }
    }
    public void ScanForAlbums(String direct){
        
    }
    public void ScanForArtists(String direct){
        
    }
    private void SongNameHnadeler(String SongName){
        
    }
    private void AlbumNameHnadeler(String SongName){
        
    }
    private void ArtistNameHnadeler(String SongName){
        
    }
    private void handlefiles(String[] items){
        Scanner in = new Scanner(System.in);
        System.out.println("These Where the files that where found");
        do{
            
        }while(true);
        
    }
    
}
