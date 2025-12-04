package edu.hcu.triage;

import java.util.Random;

/** Deterministic workload generators for performance tests. */
public final class SampleWorkloads {
    // TODO: enqueueNRandomPatients(registry, triage, N, seed, distribution)
    public void enqueueNRandomPatients(PatientRegistry registry,TriageQueue  triage,int N,long seed,boolean distribution){
        Random randomPatient = new Random(seed);

        for (int i = 0; i < N; i++) {

            // Generate a deterministic ID
            String id = "R" + i;

            // Make a simple name
            String name = "RandomPatient" + i;

            // Random age between 1 and 100
            int age = randomPatient.nextInt(100) + 1;

            int severity = 0;
            if (distribution) {
                int r = randomPatient.nextInt(100);
                if (r < 50) severity = 4 + randomPatient.nextInt(2);
                else if (r < 80) severity = 2 + randomPatient.nextInt(2);
            }
            else {severity = randomPatient.nextInt(5) + 1;}

            Patient patient = registry.registerNew(id, name, age, severity);

            triage.enqueue(patient);
        }

        System.out.println("Enqueued " + N + " random patients.");
    }
    // TODO: dequeueK(triage, K) with empty-checks
    public void dequeueK(TriageQueue  triage,int K){

        for(int i = 0; i < K; i++)
            if (triage.isEmpty()) {
                System.out.println("-----Triage Queue is Empty; Stopped Early.-----");
                return;
            } else{
                triage.dequeueNext();
            }
    }
    // TODO: knobs: severity distribution (uniform vs. skewed), ratios enqueue/dequeue
}
