package edu.utdallas;

import java.lang.instrument.Instrumentation;


public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {

        System.out.println("My Java Agent is now executing");

        // registers the transformer
        inst.addTransformer(new MyClassFileTransform());

    }
}

