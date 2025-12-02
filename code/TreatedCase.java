package edu.hcu.triage;

import java.time.Instant;

//Following libraries imported as stylistic choice to better format the times returned to user
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;


public class TreatedCase {
    public TreatedCase(Patient patient, Instant start, Instant end, Outcome outcome, String notes) {
        if(patient == null){        //If the value for patient is null
            throw new IllegalArgumentException("-----Patient cannot be null-----");     //Return exception explaining that the patient value cannot be null
        }
        this.patient = patient;
        if(start == null || end == null){       //If the start and end times are null
            throw new IllegalArgumentException("-----Start and End cannot be null-----");       //Return exception explaining tha values cannot be null
        }
        this.start = start;

        if (end.isBefore(start)) {      //checks to see if the end time is before the start time
            throw new IllegalArgumentException("-----End time cannot be before start-----");     //if true, return exception explaining why it is invalid
        }
        this.end = end;

        if (outcome == null) {      //Checks to see if output is null
            throw new IllegalArgumentException("-----Outcome cannot be null-----");     //If outcome is null, throw an exception explaining that it cannot be null
        }
        this.outcome = outcome;

        if(notes == null){      //If notes is null
            notes = "";     //The notes will be set to an empty string
        }
        this.notes = notes;
    }

    public enum Outcome { STABLE, OBSERVE, TRANSFER }

    private final Patient patient;
    private final Instant start;
    private final Instant end;
    private final Outcome outcome;
    private final String notes;

    // TODO: constructor with validation; getters; toString()

    public Patient getPatient() {
        return patient;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        // Create a DateTimeFormatter to format date-time values in a custom pattern
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm")   // Format: year-month-day hour:minute
                .withZone(ZoneId.systemDefault()); // System default time zone

        return "Patient " + patient.getName() + " has undergone treatment\n" +
                "Time: " + formatter.format(start) + " - " + formatter.format(end) + "\n" +
                "Outcome: " + outcome + "\n" +
                "Notes: " + notes;
    }
}
