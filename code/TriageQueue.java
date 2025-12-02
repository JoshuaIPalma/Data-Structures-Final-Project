package edu.hcu.triage;

import java.util.*;

/** Thin wrapper around PriorityQueue to enforce triage policy and utilities. */
public class TriageQueue {
    private final PriorityQueue<Patient> pq = new PriorityQueue<>(new TriageOrder());
    //Creates a Priority Queue which is a record of all patients in Triage Order using Triage Comparator

    // TODO: enqueue(Patient p)
    public void enqueue(Patient p){
        if(p == null) throw new NullPointerException("-----Patient cannot be null-----");
        pq.add(p);      //Adds the given patient to the priority queue
    }

    //commennt
    // TODO: enqueueById(PatientRegistry reg, String id) - lookup then enqueue
    public void enqueueById(PatientRegistry reg, String id){
        Optional<Patient> newPatientId = reg.get(id);

        if (newPatientId.isEmpty()) {
            throw new IllegalArgumentException("-----invalid Id Entered------");    // throw exception
        }
        else {
            Patient newPatient = newPatientId.get(); // safe now
            pq.add(newPatient); // add newPatient to your priority queue
        }
    }

    // TODO: peekNext(): Optional<Patient>
    public Optional<Patient> peekNext(){        //Makes peekNext() give a patient if one exists, or an empty Optional if not
        return Optional.ofNullable(pq.peek());      //If empty, return empty; if not empty, returns the patient
    }
    // TODO: dequeueNext(): Optional<Patient>
    public Optional<Patient> dequeueNext(){     //Makes dequeueNext() give a patient if one exists, or an empty Optional if not
        return Optional.ofNullable(pq.poll());  //If empty, return empty; if not empty, removes the next patient
    }
    // TODO: size()
    public int size(){
        return pq.size();       //returns the size of the queue
    }
    // TODO: snapshotOrder(): List<Patient> in triage order without mutating the queue
    public List<Patient> snapshotOrder(){
        PriorityQueue<Patient> pqCopy = new PriorityQueue<>(new TriageOrder()); //Creates a Priority Queue which is a record of all patients in Triage Order using Triage Comparator
        pqCopy.addAll(pq);      //Adds all patients in original Priority Queue to a new priority queue which is the copy of the original

        List<Patient> sortedPqList = new ArrayList<>();     //Creates an empty list that will contain each element in the Priority Queue "pq" in order of highest severity to lowest and triage order
        while(!pqCopy.isEmpty()){       //while the copy of pq is not empty
            Patient p = pqCopy.poll();      //Removes next value from pqCopy and sets it equal to instance variable p
            sortedPqList.add(p);        //Adds P to the sortedPqList which will be returned to the user
        }
        return sortedPqList;
    }
    //       Hint: clone PQ or copy to an array then sort with same comparator.
    // TODO: clear()
    public void clear(){
        pq.clear();     //Clears the Priority Queue
    }
}
