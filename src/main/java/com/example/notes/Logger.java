package com.example.notes;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {



    public static void writeException(Exception ex){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uu-HH.mm.ss");

        File logDirectory = new File("logs/");

        if (!logDirectory.exists())
            logDirectory.mkdirs();

        File log = new File(logDirectory.getPath() + "/" + "log-" + dtf.format(LocalDateTime.now()) + ".log");


        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(log);

            if (fileWriter != null) {
                fileWriter.write(ex.toString());

                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                String sStackTrace = sw.toString();
                fileWriter.write(sStackTrace);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
