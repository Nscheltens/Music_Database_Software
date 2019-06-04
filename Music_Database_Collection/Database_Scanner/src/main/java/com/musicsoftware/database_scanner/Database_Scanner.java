package com.musicsoftware.database_scanner;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.musicsoftware.database_control.Database_Control;

/**
 *
 * @author Nick Scheltens
 */
public class Database_Scanner {
    
    // use threads
    public final String DefaultDirectory;
    private final Database_Control database;
    private static final java.io.PrintStream OUT = System.out;
    private final Scanner IN = new Scanner(System.in);
    
    public Database_Scanner(Database_Control d, String dir){
        OUT.println("Starting Database Scanner");
        DefaultDirectory = dir;
        database = d;
    }
    
    public void ScanForSongs(String direct, boolean auto, String Artist, String Album){
        //OUT.println(direct);
        File directory = new File(direct);
        String[] directoryContents = directory.list();
        if(!auto) OUT.println("Listing files in directory");
        String[][] cutList = processSongNames(directoryContents, direct, Artist, Album);
        if(cutList.length == 0){
            if(!auto) OUT.println("No correctly formatted files found.");
        }else{
            if(auto){
                handleAutoFiles(cutList, 0);
            }
            else{
                handleFiles(cutList,"Song",0);
            }
        }
    }
    private String[][] processSongNames(String[] list, String direct, String Artist, String Album){
        int numSongs = list.length;
        int listMover = 0;
        String[][] cuttingList = new String[list.length][5];
        Pattern mp = Pattern.compile(".+\\.mp3");
        Pattern fp = Pattern.compile(".+\\.flac");
        for (String list1 : list) {
            //OUT.println(list1);
            Matcher mm = mp.matcher(list1);
            Matcher fm = fp.matcher(list1);
            if (mm.matches() || fm.matches()) {
                //System.out.println("going to cut number off "+list1);
            
                String[] NumRemoved = list1.split("[0-9]{2}+ ");
                //System.out.println("going to cut extention off "+NumRemoved[1]);
                
                //fix to look for flac and mp3
                String[] SongName = NumRemoved[1].split(".mp3");
                
                //System.out.println("adding "+SongName[0]);
                cuttingList[listMover][0] = SongName[0];
                
                File path = new File(direct, list1);
                //System.out.println("adding "+path.getAbsolutePath());
                cuttingList[listMover][1] = path.getAbsolutePath();
                
                String[] num = list1.split(" ");
                //System.out.println("adding "+num[0]);
                cuttingList[listMover][2] = num[0];
                
                cuttingList[listMover][3] = Album;
                
                cuttingList[listMover][4] = Artist;
                listMover++;
            } else {
                numSongs--;
            }
        }
        //System.out.println(numSongs);
        return Arrays.copyOf(cuttingList, numSongs);
    }
    
    public void ScanForAlbums(String direct, boolean auto){
        //OUT.println(direct);
        File directory = new File(direct);
        String[] directoryContents = directory.list();
        if(!auto) OUT.println("Listing files in directory");
        String[][] cutList = processAlbumNames(directoryContents, direct);
        if(cutList.length == 0){
            if(!auto) OUT.println("No correctly formatted files found.");
        }else{
            if(auto){
                handleAutoFiles(cutList, 1);
            }
            else{
                handleFiles(cutList,"Album",1);
            }
        }
    }
    private String[][] processAlbumNames(String[] list, String direct){
        int listMover = 0;
        int numSongs = list.length;
        String[][] cuttingList = new String[list.length][6];
        Pattern p = Pattern.compile("\\[.++");
        for (String list1 : list) {
            Matcher m = p.matcher(list1);
            if (m.matches()) {
                String[] spaceSplit = list1.split(" ");
                cuttingList[listMover][2] = spaceSplit[0];
                cuttingList[listMover][3] = spaceSplit[1];
                
                String[] nameSplit1 = list1.split("-\\s");
                String[] nameSplit2 = nameSplit1[1].split("\\s\\{");
                cuttingList[listMover][0] = nameSplit2[0];
                
                String[] ripSplit = list1.split("[\\{\\}]");
                cuttingList[listMover][4] = ripSplit[1];
                
                String[] fileSplit = list1.split("[\\(\\)]");
                cuttingList[listMover][5] = fileSplit[1];
                //System.out.println("date = "+cuttingList[listMover][2]+" artist = "+cuttingList[listMover][3]+" name = "+cuttingList[listMover][0]+" Rip = "+cuttingList[listMover][4]+" filetype = "+cuttingList[listMover][5]);
                
                File path = new File(direct, list1);
                cuttingList[listMover][1] = path.getAbsolutePath();
                listMover++;
            } else{
                numSongs--;
            }
        }
        return Arrays.copyOf(cuttingList, numSongs);
    }
    
    public void ScanForArtists(String direct, boolean auto){
        //OUT.println(direct);
        File directory = new File(direct);
        String[] directoryContents = directory.list();
        if(!auto) OUT.println("Listing files in directory");
        String[][] cutList = processArtistNames(directoryContents, direct);
        if(cutList.length == 0){
            if(!auto) OUT.println("No correctly formatted files found.");
        }else{
            if(auto){
                handleAutoFiles(cutList, 2);
            }
            else{
                handleFiles(cutList,"Artist",2);
            }
        }
    }
    private String[][] processArtistNames(String[] list, String direct){
        String[][] cuttingList = new String[list.length][2];
        for (int i = 0;i < list.length; i++) {
            cuttingList[i][0] = list[i];
            
            File path = new File(direct, list[i]);
            cuttingList[i][1] = path.getAbsolutePath();
        }
        return cuttingList;
    }
   
    private void handleFiles(String[][] items, String type, int dive){
        OUT.println("Scanner found these "+type+"s");
        OUT.println("-------------------------------------------------");
        for (String[] item : items) {
            OUT.println(item[0]);
        }
        OUT.println("-------------------------------------------------");
        OUT.println("add these files to the database? (y = yes, l = list individuals)");
        String anw = IN.next();
        switch (anw) {
            case "y":
                OUT.println("Adding files to the database");
                for(String[] item : items){
                    boolean check = checkAdd(item, dive);
                    if(check) OUT.println("Successfully added.");
                    else OUT.println("failed to add.");
                }
                if(dive == 0){
                    return;
                }else{
                    OUT.println("These are "+type+"'s would you like to scan deeper for albums/songs? (y = yes)");
                    String newAnw = IN.next();
                    if(newAnw.equals("y")){
                        OUT.println("Diving deeper for more files");
                        for(String[] item : items){
                            if(dive == 1){
                                ScanForSongs(item[1], false, item[3], item[0]);
                            }
                            else{
                                ScanForAlbums(item[1], false);
                            }
                        }
                    }
                    else{
                        OUT.println("Not diving for more files");
                    }
                }
                break;
            case "l":
                handleFilesList(items,type,dive);
                break;
            default:
                OUT.println("Not adding files to the database");
                // give examples of proper file names
                break;
        }
    }
    private void handleFilesList(String[][] items, String type, int dive){
        OUT.println("listing individuals");
        for(String[] item : items){
            OUT.println("Scanner found this"+type);
            OUT.println("-------------------------------------------------");
            OUT.println(item[0]);
            OUT.println("-------------------------------------------------");
            OUT.println("add this file to the database? (y = yes)");
            String anw = IN.next();
            switch (anw) {
            case "y":
                OUT.println("Adding file to the database");
                boolean check = checkAdd(item, dive);
                if(check) OUT.println("Successfully added.");
                else OUT.println("failed to add.");
                if(dive == 0){
                    return;
                }else{
                    OUT.println("These are "+type+"'s would you like to scan deeper for albums/songs? (y = yes)");
                    String newAnw = IN.next();
                    if(newAnw.equals("y")){
                        OUT.println("Diving deeper for more files");
                            if(dive == 1){
                                ScanForSongs(item[1], false, item[3], item[0]);
                            }
                            else{
                                ScanForAlbums(item[1], false);
                            }
                    }
                    else{
                        OUT.println("Not diving for more files");
                    }
                }
                break;
            default:
                OUT.println("Not adding files to the database");
                // give examples of proper file names
                break;
            }
        }
    }
    private void handleAutoFiles(String[][] items, int level){
        for(String [] item : items){
            AutocheckAdd(item,level);
            if(level == 0){
                
            }
            else{
                if(level == 1){
                    ScanForSongs(item[1], true, item[3], item[0]);
                }
                else{
                    ScanForAlbums(item[1], true);
                }
            }
        }
    }
    
    private boolean checkAdd(String[] item, int level){
        boolean check;
        switch(level){
            case 0: check = database.checkSong(item[0]);
                break;
            case 1: check = database.checkAlbum(item[0]);
                break;
            case 2: check = database.checkArtist(item[0]);
                break;
            default: check = false;
                break;
        }
        if(!check){
            boolean addCheck;
            switch(level){
                case 0: addCheck = database.addSong(item);
                    break;
                case 1: addCheck = database.addAlbum(item);
                    break;
                case 2: addCheck = database.addArtist(item);
                    break;
                default: addCheck = false;
                    OUT.println("Failed to add "+item[0]);
                    break;
            }
            return addCheck;
        }else{
            OUT.println(item[0]+" already exists in the database");
            return false;
        }
    }
    private boolean AutocheckAdd(String[] item, int level){
        boolean check;
        switch(level){
            case 0: check = database.checkSong(item[0]);
                break;
            case 1: check = database.checkAlbum(item[0]);
                break;
            case 2: check = database.checkArtist(item[0]);
                break;
            default: check = false;
                break;
        }
        if(!check){
            boolean addCheck;
            switch(level){
                case 0: addCheck = database.addSong(item);
                    break;
                case 1: addCheck = database.addAlbum(item);
                    break;
                case 2: addCheck = database.addArtist(item);
                    break;
                default: addCheck = false;
                    OUT.println("Failed to add "+item[0]);
                    break;
            }
            return addCheck;
        }else{
            //OUT.println(item[0]+" already exists in the database");
            return false;
        }
    }
}