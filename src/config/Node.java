package config;

import graph.Message;
import java.util.*;

public class Node {
    private String name;
    private List<Node> edges;
    private Message msg;

    Node(String name) {
        this.name = name;
        this.edges = new ArrayList<Node>();
       
    }

    public String getName() {
     
        return name;
    }

    public void setName(String name) {
       
        this.name = name;
    }

    public List<Node> getEdges() {
   
        return edges;
    }

    public void setEdges(List<Node> edges) {
       
        this.edges = edges;
    }

    public Message getMsg() {
        
        return msg;
    }

    public void setMsg(Message msg) {
        
        this.msg = msg;
    }

    public void addEdge(Node node) {
        if (!edges.contains(node)) {
            edges.add(node);
     
        } else {
          
        }
    }

    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();
        Set<Node> stack = new HashSet<>();
        return hasCycleHelper(this, visited, stack);
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
