package com.InternalMeetingApp.InternalMeetingApp.Repository;

import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.Scanner;

@Repository
public class FileRepository {

    public void writeToFile(String json){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("meetings.json").getFile());
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(json);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFromFile(){
        String json = "";

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("meetings.json").getFile());

        try {
            Scanner reader = new Scanner(file);
            json = reader.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(json.isEmpty()){
            return null;
        }

        return json;
    }
}
