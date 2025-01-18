package graph;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelAgent implements Agent {
    private final Agent agent; // The wrapped agent
    private final ExecutorService executor; // Executor for parallel execution

    public ParallelAgent(Agent agent) {
        this.agent = agent;
        this.executor = Executors.newSingleThreadExecutor(); // Single-threaded executor for the agent
    }

    @Override
    public String getName() {
        return agent.getName(); // Forward to the wrapped agent
    }


    @Override
    public void callback(String topic, Message msg) {
        executor.submit(() -> agent.callback(topic, msg));
    }

    @Override
    public void close() {
        try {
            agent.close(); // Close the wrapped agent
        } finally {
            executor.shutdown(); // Shut down the executor
        }
    }

    @Override
    public void reset() {
        agent.reset(); // Forward to the wrapped agent
    }
}
