package config;


import graph.Agent;
import graph.Topic;
import graph.TopicManagerSingleton;

import java.util.*;

public class Graph extends ArrayList<Node>{


    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();

        // מעבר על כל הצמתים בגרף
        for (Node node : this) {
            if (!visited.contains(node)) {
                if (node.hasCycles()) { // קריאה למתודה hasCycles של Node
                    return true;
                }
            }
        }
        return false;
    }


    public void createFromTopics() {
        // ניקוי הגרף לפני יצירה מחדש
        this.clear();

        // מפה לשמירת הקודקודים שכבר נוצרו לפי שמותיהם
        Map<String, Node> nodesMap = new HashMap<>();

        // קבלת TopicManager
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();

        // שלב 1: יצירת קודקודים עבור כל ה-Topics
        for (Topic topic : tm.getTopics()) {
            String topicNodeName = "T" + topic.name;
            nodesMap.computeIfAbsent(topicNodeName, name -> new Node(name));
        }

        // שלב 2: יצירת קודקודים עבור כל ה-Agents
        for (Topic topic : tm.getTopics()) {
            for (Agent agent : topic.subs) {
                String agentNodeName = "A" + agent.getName();
                nodesMap.computeIfAbsent(agentNodeName, name -> new Node(name));
            }
            for (Agent agent : topic.pubs) {
                String agentNodeName = "A" + agent.getName();
                nodesMap.computeIfAbsent(agentNodeName, name -> new Node(name));
            }
        }

        // שלב 3: הוספת קשתות מה-Topics ל-Agents (subs)
        for (Topic topic : tm.getTopics()) {
            Node topicNode = nodesMap.get("T" + topic.name);
            for (Agent agent : topic.subs) {
                Node agentNode = nodesMap.get("A" + agent.getName());
                topicNode.addEdge(agentNode);
            }
        }

        // שלב 4: הוספת קשתות מה-Agents ל-Topics (pubs)
        for (Topic topic : tm.getTopics()) {
            for (Agent agent : topic.pubs) {
                Node agentNode = nodesMap.get("A" + agent.getName());
                Node topicNode = nodesMap.get("T" + topic.name);
                agentNode.addEdge(topicNode);
            }
        }

        // הוספת כל הקודקודים לגרף
        this.addAll(nodesMap.values());
    }


}

