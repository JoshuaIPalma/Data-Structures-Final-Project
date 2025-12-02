package edu.hcu.triage;

import java.time.Instant;
import java.util.*;

/** Append-only treatment log using LinkedList. */
public class TreatmentLog {
    private final LinkedList<TreatedCase> log = new LinkedList<>();

    // TODO: append(TreatedCase tc)
    public void append(TreatedCase tc) {
        log.add(tc);        //Appends the treated patient ot the end of the list
        System.out.println(tc.getPatient().getName() + " Has been added to the treated log.");
    }

    // TODO: size()
    public int size(){
        return log.size();
    }

    // TODO: asListOldestFirst()
    public List<TreatedCase> asListOldestFirst() {
        if(log.isEmpty()) throw new IllegalStateException("------Log is empty------");      //Checks to see if the log is empty If true, throw exception telling the user that the log is empty

        else{
            //Creates new lift containing the objects in log in the same order, because log is already in the oldest first order
            ArrayList<TreatedCase> asListOldestFirst = new ArrayList<>(log);
            return asListOldestFirst;       //Returns the list to the user
        }
    }


    // TODO: asListNewestFirst()
    public List<TreatedCase> asListNewestFirst() {
        if(log.isEmpty()) throw new IllegalStateException("------Log is empty------");      //Checks to see if the log is empty If true, throw exception telling the user that the log is empty

        else{
            //Creates new list containing the objects in the log
            ArrayList<TreatedCase> asListNewestFirst = new ArrayList<>();       //Creates new list that will contain the log in reverse order
            for(int i = log.size() - 1; i >= 0; i--){   //adds the objects in log in reversed order into asListNewestFirst
                asListNewestFirst.add(log.get(i));
            }
            return asListNewestFirst;       //Returns the list to the user
        }
    }
}
