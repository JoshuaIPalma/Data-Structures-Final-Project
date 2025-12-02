//1) Patient Registration
//Create or update patients with: id, name, age, severity (define scale), and arrival timestamp.
//Store in HashMap. Duplicate IDs update the existing record (do not create duplicates).
//Updates to severity do not change queue position unless explicitly re-triaged (state your policy clearly).

package edu.hcu.triage;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable identity (id) + mutable clinical state (severity).
 * Arrival order must be trackable for FIFO tie-breaking.
 */
public class Patient {
    private final String id;        // e.g., "P001"
    private String name;
    private int age;
    private int severity;           // define scale: higher = more urgent
    private final Instant arrival;  // registration time
    private final long arrivalSeq;  // monotonic sequence for FIFO ties

    // TODO: constructor(s) with validation (null/empty id, bounds for age/severity)
    public Patient(String id, String name, int age, int severity, Instant arrival, long arrivalSeq) {
        if (id == null) throw new NullPointerException("------Id cannot be null-----");
        this.id = id;

        if  (name == null) throw new NullPointerException("-----Name cannot be null-----");
        this.name = name;

        if (age < 1 || age > 100) throw new IllegalArgumentException("-----Age must be between 1 and 100-----");
        this.age = age;

        if (severity < 1 || severity > 10){     //Checks to see if severity is between reasonable bounds
            throw new IllegalArgumentException("-----Severity must be between 1 and 10----");       //If not, throw exception
        }
        this.severity = severity;

        if (arrival == null) throw new NullPointerException("------Arrival cannot be null-----");
        this.arrival = arrival;

        if (arrivalSeq < 0) throw new IllegalArgumentException("------Arrival seq cannot be negative-----");
        this.arrivalSeq = arrivalSeq;
    }


    // TODO: getters for all fields
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getSeverity() {
        return severity;
    }

    public Instant getArrival() {
        return arrival;
    }

    public long getArrivalSeq() {
        return arrivalSeq;
    }

    // TODO: setters only where allowed (e.g., name, age, severity)
    public void setName(String name) {
        if (name == null) throw new NullPointerException("----Name cannot be null-----");       //Checks to see if name is null, if it is throw exception
        this.name = name;
    }

    public void setAge(int age) {
        if (age < 1 || age > 100) {     //Checks to see if age is between reasonable bounds
            throw new IllegalArgumentException("-----Age must be between 1 and 100-----");       //If not, throw exception
        }
        this.age = age;     //If age within bounds, set age to new value
    }

    public void setSeverity(int severity) {
        if (severity < 1 || severity > 10){     //Checks to see if severity is between reasonable bounds
            throw new IllegalArgumentException("-----Severity must be between 1 and 10----");       //If not, throw exception
        }
        this.severity = severity;
    }

    // TODO: equals/hashCode based on id only (document this in README.pdf)
    @Override
    public boolean equals(Object other){
        if (this == other) return true;     //Checks to see if they reference the same patient
        if (other == null || getClass() != other.getClass()) return false;      //Checks to see if the object we are comparing is null, or not found in

        Patient otherPatient = (Patient) other;     //Wraps otherPatient as patient object so we can compare the two id attributes
        return Objects.equals(this.id, otherPatient.id);        //compares the string id of the two patients
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);        //Ensures that "Patient" works reliably inside all hash data structures
    }

    // TODO: toString() concise
    @Override
    public String toString() {
        return name + " - id: " + id + ", age: " + age + ", severity: " + severity;
    }
}
