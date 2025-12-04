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

            if(lines.isEmpty()) {
                System.err.println("-----CSV File is Empty-----");
                return;
            }

            for(int i = 1; i < lines.size(); i++) {     //Begins for loop to scan each line after the first line
                String rawLine = lines.get(i).trim();

                if (rawLine.isEmpty()) continue;

                String[] line = rawLine.split(",");

                if (line.length != 4) {
                    System.out.println("-----Line "+ i + ": '" + lines.get(i) + "' is malformed and will be ignored-----");
                    continue;
                }

                String id = line[0].trim();
                String name = line[1].trim();

                int age = 0;        //Initialize age and severity variables
                int severity = 0;

                try{
                    age = Integer.parseInt(line[2].trim());        //Creates age and severity from the read string and turns them into an integer
                    severity = Integer.parseInt(line[3].trim());}
                catch(NumberFormatException e){
                    System.err.println("-----Line " + i + ": Invalid age or severity (must be an integer)-----");
                    continue;
                }
                reg.registerNew(id, name, age, severity);       //Created new patient based on the information given from each line
            }
        }
        catch(IOException e){ System.out.println("-----IO Exception: " + e + "-----");}
    }


    // TODO: exportLog(Path csv, List<TreatedCase> cases)
    //   - Write ISO-8601 times; escape commas in notes if needed
    public static void exportLog(Path csv, List<TreatedCase> cases){
        try(BufferedWriter writer = Files.newBufferedWriter(csv)){      //Creates

            writer.write("id,name,age,initialSeverity,arrivalIso,treatStartIso,treatEndIso,outcome,notes");
            writer.newLine();

            for(TreatedCase tc : cases){
                var treatedPatient = tc.getPatient();

                // Escape commas in notes by wrapping in quotes
                String notes = tc.getNotes().contains(",")
                        ? "\"" + tc.getNotes() + "\""
                        : tc.getNotes();

                //Write the treated patient as a string by utilizing the toString() method
                writer.write(String.join(",",
                        treatedPatient.getId(),
                        treatedPatient.getName(),
                        String.valueOf(treatedPatient.getAge()),
                        String.valueOf(treatedPatient.getSeverity()),
                        treatedPatient.getArrival().toString(),
                        tc.getStart().toString(),
                        tc.getEnd().toString(),
                        tc.getOutcome().name(),
                        notes
                ));
                writer.newLine();
            }
        }
        catch(IOException e){ System.out.println("-----IO Exception: " + e + "-----");}
    }
}
