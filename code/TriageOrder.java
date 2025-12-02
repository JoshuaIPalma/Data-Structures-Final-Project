package edu.hcu.triage;

import java.util.Comparator;

/** Comparator: higher severity first; break ties by smaller arrivalSeq (FIFO). */
public final class TriageOrder implements Comparator<Patient> {
    // TODO: implement compare(...) carefully; do not reverse tie order
    @Override
    public int compare(Patient patient1, Patient patient2){     //Overrides the compare function used in the comparator interface
        if (patient1.getSeverity() > patient2.getSeverity()){       //Checks to see if a patients severity is greater than another
            return -1;      //If so, return -1 to give patient one priority in the queue
        }
        else if (patient1.getSeverity() < patient2.getSeverity()){      //If the patients severity is less than another
            return 1;       //return 1 so that the other patient has higher severity
        }
        else{       //If the severity is the same (tie)
            if(patient1.getArrivalSeq() <  patient2.getArrivalSeq()){       //Perform tie-breaker by checking the arrivalSeq of each user
                return -1;      //If the arrivalSeq of one patient is less than the other, return "-1" so they get priority in the queue
            }
            else if(patient1.getArrivalSeq() > patient2.getArrivalSeq()){
                return 1;       //If the arrivalSeq of one patient is greater than another, then return "1" so the other patient gets priority in the queue
            }
            else return 0;      //If the same, they both have the same priority
        }
    }

    // TODO: document how this yields stable behavior for equal severity
}
