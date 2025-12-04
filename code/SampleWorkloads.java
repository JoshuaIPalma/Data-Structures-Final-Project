package edu.hcu.triage;

import java.util.Arrays;
import java.util.Random;

// Deterministic workload generators for performance tests
public final class SampleWorkloads {

    private SampleWorkloads() {
    }

    // Result of running a workload, including counts before/after dequeues and dequeues performed
    public static class WorkloadResult {
        public final int[] beforeDequeues; // snapshot after enqueue
        public final int[] afterDequeues; // snapshot after dequeue
        public final int dequeuesPerformed;     //Num of dequeues performed

        public WorkloadResult(int[] beforeDequeues, int[] afterDequeues, int dequeuesPerformed) {
            this.beforeDequeues = beforeDequeues;
            this.afterDequeues = afterDequeues;
            this.dequeuesPerformed = dequeuesPerformed;
        }
    }

    // Enqueue N random patients into the registry and triage queue
    public static int[] enqueueNRandomPatients(PatientRegistry registry, TriageQueue triage, int N, long seed, String distribution) {
        Random rand = new Random(seed);
        int[] severityCounts = new int[5]; // initialize an array to track each severity count

        for (int i = 0; i < N; i++) {       //for i in range of N random enqueued patients
            String id = "P" + i;        //patient id is equal to the patient and which random patient they are
            String name = "Patient number " + i;        //[atient name is equal to "patient" + whatever number patient they are
            int age = rand.nextInt(100) + 1; // creates a random integer age between 1 and 100;

            // Determine severity based on chosen distribution
            int severity;       //initializes severity to zero
            if ("skewed".equalsIgnoreCase(distribution)) {      //If the distribution is equal to "skewed" meaning that there are more patients with a lesser severity
                // 60% severity 4-5
                // 30% severity 2-3
                // 10% severity 1
                double r = rand.nextDouble();
                if (r < 0.6) {      // 60% severity 4-5
                    severity = rand.nextInt(2) + 4; // 4-5
                } else if (r < 0.9) {       // 30% severity 2-3
                    severity = rand.nextInt(2) + 2; // 2-3
                } else {        // 10% severity 1
                    severity = 1; // 1
                }
            } else {
                // Uniform distribution: all severities have the same amount of patients
                severity = rand.nextInt(5) + 1; // uniform 1-5
            }

            severityCounts[severity - 1]++; // Track each severity count
            registry.registerNew(id, name, age, severity);      //Register patient to registry
            triage.enqueueById(registry, id);       //enqueue patient to triage queue
        }

        return severityCounts;      //return array containing the number of patients for each severity
    }

    // Dequeues K patients with check for empty queues and updates severity counts
    public static int dequeueKPatients(TriageQueue triage, int K, int[] severityCounts) {
        int numOfDequeuedPatients = 0;      //Initializes variable containing the number of dequeued patients
        for (int i = 0; i < K; i++) {       //For i in range K, the number of patients the user wants to dequeue
            var nextPatient = triage.peekNext();        //Creates variable nextOpt which contains the nect patient in the queue
            if (nextPatient.isEmpty()) break; // stop dequeue if queue is empty

            int sev = nextPatient.get().getSeverity();      //Creates new patient that gets the next patients severity
            if (sev >= 1 && sev <= 5) severityCounts[sev - 1]--; // if severity falls between 1 and 5 inclusive, decrement remaining count
            triage.dequeueNext();       //dequeue the nextPatient
            numOfDequeuedPatients++;        //Increment the number of dequeued patients
        }
        return numOfDequeuedPatients;       //return the number of patents dequeued
    }

    // Runs a deterministic workload and returns before/after severity counts and dequeues performed
    public static WorkloadResult runWorkload(PatientRegistry registry, TriageQueue triage,
                                             int workloadSize, double enqueueRatio,
                                             String distribution, long seed) {

        if (workloadSize <= 0) throw new IllegalArgumentException("------Workload Size Cannot Be Negative-----");       //Checks to see if the severity is positive
        if (enqueueRatio < 0.0 || enqueueRatio > 1.0)       //Checks to see if the ratio is between 0 and 1
            throw new IllegalArgumentException("-----Enqueue Ratio Must Be Between 0 and 1-----");      //If not throw exception
        if (!"uniform".equalsIgnoreCase(distribution) && !"skewed".equalsIgnoreCase(distribution))      //If distribution is not "uniform" or "skewed"
            throw new IllegalArgumentException("-----Distribution must be 'uniform' or 'skewed'-----");     //Throw exception

        int enqueueOps = (int) (workloadSize * enqueueRatio);       //Creates a variable that represents the number of patients to be enqueued
        int dequeueOps = workloadSize - enqueueOps;     //Creates a number that shows the number of patients dequeued based on the number of patients enqueued

        // Enqueue patients
        int[] severityCounts = enqueueNRandomPatients(registry, triage, enqueueOps, seed, distribution);

        // Take snapshots so results are not mutated
        int[] beforeDequeues = Arrays.copyOf(severityCounts, severityCounts.length);
        int[] afterDequeues = Arrays.copyOf(severityCounts, severityCounts.length);

        // Dequeue remaining patients and update afterDequeues
        int dequeuesPerformed = dequeueKPatients(triage, dequeueOps, afterDequeues);

        return new WorkloadResult(beforeDequeues, afterDequeues, dequeuesPerformed);
    }
}
