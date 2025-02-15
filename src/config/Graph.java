// Updated Graph.java with robust cycle detection and graph handling
package config;

import graph.Agent;
import graph.Topic;
import graph.TopicManagerSingleton;
import java.util.*;

public class Graph extends ArrayList<Node> {
    private HashMap<String, Node> nodemap;

    public Graph() {
        super();
        nodemap = new HashMap<>();
    }

    public void createFromTopics() {
        this.clear();
        nodemap.clear();

        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        List<Topic> topics = new ArrayList<>(tm.getTopics());

        // Step 1: Create nodes for all topics
        for (Topic topic : topics) {
            String topicNodeName = "T" + topic.name;
            if (!nodemap.containsKey(topicNodeName)) {
                Node topicNode = new Node(topicNodeName);
                nodemap.put(topicNodeName, topicNode);
                this.add(topicNode);
            }
        }

        // Step 2: Create nodes for all agents and avoid duplicates
        for (Topic topic : topics) {
            for (Agent agent : topic.subs) {
                findOrCreateAgentNode(agent);
            }
            for (Agent agent : topic.pubs) {
                findOrCreateAgentNode(agent);
            }
        }

        // Step 3: Add edges from topics to subscribing agents
        for (Topic topic : topics) {
            Node topicNode = nodemap.get("T" + topic.name);
            for (Agent agent : topic.subs) {
                Node agentNode = nodemap.get("A" + agent.getName());
                if (agentNode != null) {
                    topicNode.addEdge(agentNode);
                  
                }
            }
        }

        // Step 4: Add edges from agents to published topics
        for (Topic topic : topics) {
            for (Agent agent : topic.pubs) {
                Node agentNode = nodemap.get("A" + agent.getName());
                Node topicNode = nodemap.get("T" + topic.name);
                if (agentNode != null) {
                    agentNode.addEdge(topicNode);
                
                }
            }
        }

       
   
    }

    private void findOrCreateAgentNode(Agent agent) {
        String agentNodeName = "A" + agent.getName();
        if (!nodemap.containsKey(agentNodeName)) {
            Node node = new Node(agentNodeName);
            nodemap.put(agentNodeName, node);
            this.add(node);
     
        }
    }

    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();
        Set<Node> stack = new HashSet<>();

        for (Node node : this) {
            if (!visited.contains(node)) {
                if (hasCycleHelper(node, visited, stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasCycleHelper(Node current, Set<Node> visited, Set<Node> stack) {
        if (stack.contains(current)) {
            return true;
        }
        if (visited.contains(current)) {
            return false;
        }

        visited.add(current);
        stack.add(current);

        for (Node neighbor : current.getEdges()) {
            if (hasCycleHelper(neighbor, visited, stack)) {
                return true;
            }
        }

        stack.remove(current);
        return false;
    }
}
