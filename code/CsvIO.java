package edu.hcu.triage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.nio.ByteBuffer;

/** Minimal CSV import/export using standard IO. */
public final class CsvIO {
    // TODO: loadPatients(Path csv, PatientRegistry reg)
    public void loadPatients(Path csv, PatientRegistry reg){
        //   - Expect header: id,name,age,severity
        //   - Trim fields; skip blanks; validate; warn on malformed lines with line numbers
        try{
            List<String> lines = Files.readAllLines(csv);
            for(int i = 1; i < lines.size(); i++) {
                String[] line = lines.get(i).trim().split(",");
                if (line.length == 0) continue;
                else if (line.length != 4) {
                    System.out.println("-----Line: '" + lines.get(i) + "' is malformed and will be ignored-----");
                    continue;
                }

                String id = line[0];
                String name = line[1];
                int age = Integer.parseInt(line[2]);
                int severity = Integer.parseInt(line[3]);
            }
        }
        catch(IOException e){ System.out.println("-----IO Exception: " + e + "-----");}
    }


    // TODO: exportLog(Path csv, List<TreatedCase> cases)
    //   - Write ISO-8601 times; escape commas in notes if needed
    public static void exportLog(Path csv, List<TreatedCase> cases){
        try(BufferedWriter writer = Files.newBufferedWriter(csv)){
            for(TreatedCase tc : cases){
                writer.write(tc.toString());
                writer.newLine();
            }
        }
        catch(IOException e){ System.out.println("-----IO Exception: " + e + "-----");}
    }
}
