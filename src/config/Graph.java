package config;


import graph.Agent;
import graph.Topic;
import graph.TopicManagerSingleton;

import java.util.*;

public class Graph extends ArrayList<Node>{
    private HashMap<String, Node> nodemap;

    public Graph() {
        super();
        nodemap = new HashMap<>();
    }



private boolean hasCycleHelper(Node current,Set<Node> visited,Set<Node> stack) {
        if (stack.contains(current)) {
            return true;
        }
        if (visited.contains(current)) {
            return false;
        }
        visited.add(current);
        stack.add(current);
        for (Node n : current.getEdges()) {
            if (hasCycleHelper(n,visited,stack)) {
                return true;
            }
        }
        stack.remove(current);
        return false;
}




    private void findOrCreateAgentNode(Agent agent) {
        String agentNodeName = "A" + agent.getName();
        Node node = nodemap.get(agentNodeName);
        if (node == null) {
            node = new Node(agentNodeName);
            this.add(node);
            nodemap.put(agentNodeName, node);
        }
    }


    public boolean hasCycles() {
        for (Node node : this) {
            if (hasCycleHelper(node,new HashSet<>(),new HashSet<>())) {
                return true;
            }
        }
        return false;
   }

    public void createFromTopics() {
        // ניקוי הגרף לפני יצירה מחדש
        this.clear();
        nodemap.clear();

        // קבלת TopicManager
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();

        // שלב 1: יצירת קודקודים עבור כל ה-Topics
        for (Topic topic : tm.getTopics()) {
            String topicNodeName = "T" + topic.name;
            nodemap.computeIfAbsent(topicNodeName, name -> new Node(name));
        }

        // שלב 2: יצירת קודקודים עבור כל ה-Agents (ללא כפילויות)
        for (Topic topic : tm.getTopics()) {
            for (Agent agent : topic.subs) {
                findOrCreateAgentNode(agent); // שימוש במתודה שמונעת כפילויות
            }
            for (Agent agent : topic.pubs) {
                findOrCreateAgentNode(agent); // שימוש במתודה שמונעת כפילויות
            }
        }

        // שלב 3: הוספת קשתות מה-Topics ל-Agents (subs)
        for (Topic topic : tm.getTopics()) {
            Node topicNode = nodemap.get("T" + topic.name);
            for (Agent agent : topic.subs) {
                Node agentNode = nodemap.get("A" + agent.getName());
                topicNode.addEdge(agentNode);
            }
        }

        // שלב 4: הוספת קשתות מה-Agents ל-Topics (pubs)
        for (Topic topic : tm.getTopics()) {
            for (Agent agent : topic.pubs) {
                Node agentNode = nodemap.get("A" + agent.getName());
                Node topicNode = nodemap.get("T" + topic.name);
                agentNode.addEdge(topicNode);
            }
        }

        // הוספת כל הקודקודים לגרף
        this.addAll(nodemap.values());
    }



}

