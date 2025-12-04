package edu.hcu.triage;

import java.nio.file.*;
import java.time.Instant;
import java.util.*;

/** Text-based UI (no GUI). Keep robust and simple. */
public class HospitalApp {

    private final PatientRegistry registry = new PatientRegistry();
    private final TriageQueue triage = new TriageQueue();
    private final TreatmentLog log = new TreatmentLog();
    private final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        new HospitalApp().run(args);
    }

    private void run(String[] args) {
        // TODO: if args.length > 0, attempt to load patients.csv
        // TODO: main loop with menu and input validation

        while(true) {
            Scanner scan = new Scanner(System.in);
            // Required actions:
            //  1) Register patient
            System.out.println("1) Register patient");
            //  2) Update patient
            System.out.println("2) Update patient");
            //  3) Enqueue for triage (by id)
            System.out.println("3) Enqueue for triage (by id)");
            //  4) Peek next
            System.out.println("4) Peek next");
            //  5) Admit/treat next (capture outcome + notes; append to log)
            System.out.println("5) Admit/treat next ");
            //  6) Show triage order (non-destructive)
            System.out.println("6) Show triage order");
            //  7) Find patient by id
            System.out.println("7) Find patient by id");
            //  8) Show treatment log
            System.out.println("8) Show treatment log");
            //  9) Performance demo (use SampleWorkloads)
            System.out.println("9) Performance demo ");
            // 10) Export log to CSV
            System.out.println("10) Export log to CSV");
            //  0) Exit
            System.out.println("0) Exit");
            int option = scan.nextInt();
            scan.nextLine();


            if(option == 1){
                System.out.println("enters patient's ID: ");
                String ID = scan.nextLine();
                System.out.println("enters patient's name:  ");
                String name = scan.nextLine();
                System.out.println("enters patient's age: ");
                int age = scan.nextInt();
                System.out.println("enters patient's severity: ");
                int severity = scan.nextInt();

                registry.registerNew(ID,name,age,severity);


            } else if (option == 2) {

                Optional<String> newName = Optional.empty();
                Optional<Integer> newAge = Optional.empty();
                Optional<Integer> newSeverity = Optional.empty();

                System.out.println("enters patient's ID: ");
                String ID = scan.nextLine();

                System.out.println("would you like to update the name (y/n): ");
                String choice = scan.nextLine();
                if(choice.equals("y")){
                    System.out.println("enters patient's new name:  ");
                    String name = scan.nextLine();
                    newName = Optional.of(name);
                }

                System.out.println("would you like to update the age (y/n): ");
                choice = scan.nextLine();
                if(choice.equals("y")){
                    System.out.println("enters patient's new age:  ");
                    int age = scan.nextInt();
                    scan.nextLine();
                    newAge = Optional.of(age);
                }

                System.out.println("would you like to update the severity (y/n): ");
                choice = scan.nextLine();
                if(choice.equals("y")){
                    System.out.println("enters patient's new severity:  ");
                    int severity = scan.nextInt();
                    scan.nextLine();
                    newSeverity = Optional.of(severity);
                }

                registry.updateExisting(ID,newName,newAge,newSeverity);

            } else if (option == 3) {

                System.out.println("what is the patient's Id: ");
                String patientID = scan.nextLine();
                triage.enqueueById(registry, patientID);

            } else if (option == 4) {

                System.out.println(triage.peekNext());

            } else if (option == 5) {

                Optional<Patient> opt = triage.dequeueNext();

                if (opt.isEmpty()) {
                    System.out.println("No patients in triage queue.");
                    return;
                }

                Patient patient = opt.get();
                System.out.println("Treating patient: " + patient);

                // Read outcome
                System.out.print("Enter outcome (STABLE, OBSERVE, TRANSFER): ");
                String outcomeStr = in.nextLine().trim().toUpperCase();

                TreatedCase.Outcome outcome;
                try {
                    outcome = TreatedCase.Outcome.valueOf(outcomeStr);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid outcome type.");
                    return;
                }

                // Notes
                System.out.print("Enter notes: ");
                String notes = in.nextLine();

                // Times
                Instant start = patient.getArrival();
                Instant end = Instant.now();

                TreatedCase tc = new TreatedCase(
                        patient,
                        start,
                        end,
                        outcome,
                        notes
                );

                log.append(tc);
                System.out.println("Patient treated and logged successfully.");



            } else if (option == 6) {

                System.out.println(triage.snapshotOrder());

            } else if (option == 7) {
                System.out.println("what is the patient's Id: ");
                String patientID = scan.nextLine();
                if(registry.contains(patientID)){
                    System.out.println(registry.get(patientID));
                }else{
                    System.out.println("patient can not be found");
                }

            } else if (option == 8) {

                if(log.size() == 0){
                    System.out.println("log is empty");
                }else {
                    System.out.println(log.asListOldestFirst());
                }

            } else if (option == 9) {

                System.out.print("Workload size: ");
                int workloadSize = in.nextInt();
                System.out.print("Enqueue ratio (0.0-1.0): ");
                double enqueueRatio = in.nextDouble();
                in.nextLine();
                System.out.print("Distribution (uniform/skewed): ");
                String distribution = in.nextLine().trim();
                System.out.print("Random seed: ");
                long seed = in.nextLong();
                in.nextLine();

                long startTime = System.nanoTime();
                SampleWorkloads.WorkloadResult result = SampleWorkloads.runWorkload(
                        registry, triage, workloadSize, enqueueRatio, distribution, seed
                );
                long endTime = System.nanoTime();

                System.out.println("\n--- Results ---");
                System.out.println("Time: " + String.format("%.2f", (endTime - startTime) / 1_000_000.0) + " ms");
                System.out.println("Dequeued: " + result.dequeuesPerformed);
                System.out.println("Remaining: " + triage.size());

            } else if (option == 10) {
                if (log.size() == 0) {
                    System.out.println("Treatment log is empty. Nothing to export.");

                }

                System.out.print("Enter filename for CSV export (e.g., treatment_log.csv): ");
                String filename = in.nextLine().trim();

                // Add .csv extension if not present
                if (!filename.toLowerCase().endsWith(".csv")) {
                    filename += ".csv";
                }

                try {
                    Path outputPath = Paths.get(filename);
                    List<TreatedCase> cases = log.asListOldestFirst();
                    CsvIO.exportLog(outputPath, cases);
                    System.out.println("Successfully exported " + cases.size() + " cases to " + filename);
                } catch (Exception e) {
                    System.err.println("Failed to export log: " + e.getMessage());
                }
            }else if(option ==0){
                break;
            }else {
                System.out.println("choose a number 0-10: ");
            }
        }


    }

    // TODO: helper methods for each menu action
}
