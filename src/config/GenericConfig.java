package config;

import graph.Agent;
import graph.ParallelAgent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class GenericConfig implements Config {
    private final List<ParallelAgent> agents = new ArrayList<>();
    private String configFile;

    @Override
    public void create() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }

            for (int i = 0; i < lines.size(); i += 3) {
                String className = lines.get(i);
                String[] subs = lines.get(i + 1).split(",");
                String[] pubs = lines.get(i + 2).split(",");

                Class<?> agentClass = Class.forName(className);
                Agent agent = (Agent) agentClass.getConstructor(String[].class, String[].class)
                        .newInstance((Object) subs, (Object) pubs);

                ParallelAgent parallelAgent = new ParallelAgent(agent);
                agents.add(parallelAgent);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to create agents.", e);
        }
    }

    public void close() {
        for (ParallelAgent agent : agents) {
            agent.close();
        }
        agents.clear();
    }

    @Override
    public String getName() {
        return "GenericConfig";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    public void setConfFile(String configFile) {
        this.configFile = configFile;
    }



}

