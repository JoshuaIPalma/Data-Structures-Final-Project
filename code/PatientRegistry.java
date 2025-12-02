package edu.hcu.triage;

import java.time.Instant;
import java.util.*;

/** O(1)-ish lookup of patients by id. */
public class PatientRegistry {
    private final Map<String, Patient> byId = new HashMap<>();
    private long nextArrivalSeq = 0L;

    // TODO: registerNew(id, name, age, severity): create Patient with arrivalSeq = nextArrivalSeq++
    public Patient registerNew(String id, String name, int  age, int severity){
        Patient patient = new Patient(id, name, age, severity, Instant.now(), nextArrivalSeq);      //Creates new patient
        nextArrivalSeq++;       //Increments patients seq by 1
        byId.put(id, patient);      //Adds the patient and their ID into the hashmap "byId"

        return patient;     //Returns the patient along with all the information corresponding to it
    }
    // TODO: updateExisting(id, name?, age?, severity?): partial, validated updates
    public Patient updateExisting(String id, Optional<String> name, Optional<Integer> age, Optional<Integer> severity){     //Creates an object updatedPatient, which takes the updated values and stores them
        Patient updatedPatient = byId.get(id);      //Updates the patient eith the corresponding Id passed

        if (updatedPatient == null) throw new IllegalArgumentException("-----Patient with id " + id + " does not exist-----");      //If the ID points to a null value, throw exception explaining that the user doesn't exist
        else {      //If the updatedPatient id corresponds to an existing patient
            name.ifPresent(updatedPatient::setName);    //Update the name if a new name was passed to argument
            age.ifPresent(updatedPatient::setAge);      //Update the age if a new name was passed to argument
            severity.ifPresent(updatedPatient::setSeverity);    //Update the age if a new name was passed to argument
        }
        return updatedPatient;      //Returns the updated patient
    }

    // TODO: get(id): Optional<Patient>
    public Optional<Patient> get(String id){
        return Optional.ofNullable(byId.get(id));   //retrieves the id, if the id is null, then throw a NullPointerException
    }
    // TODO: contains(id)
    public boolean contains(String id){
        return byId.containsKey(id);        //If the byId hashmap contains the passed key, return true
    }
    // TODO: size()
    public int size(){
        return byId.size();     //Returns the size of the hashmap byId
    }
}
