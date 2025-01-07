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
        System.out.println("Node created: " + name);
    }

    public String getName() {
        System.out.println("getName called: " + name);
        return name;
    }

    public void setName(String name) {
        System.out.println("setName called. Old: " + this.name + ", New: " + name);
        this.name = name;
    }

    public List<Node> getEdges() {
        System.out.println("getEdges called for node: " + name);
        return edges;
    }

    public void setEdges(List<Node> edges) {
        System.out.println("setEdges called for node: " + name + ", Edges size: " + edges.size());
        this.edges = edges;
    }

    public Message getMsg() {
        System.out.println("getMsg called for node: " + name);
        return msg;
    }

    public void setMsg(Message msg) {
        System.out.println("setMsg called for node: " + name + ", Message: " + msg);
        this.msg = msg;
    }

    public void addEdge(Node node) {
        if (!edges.contains(node)) {
            edges.add(node);
            System.out.println("Edge added from " + this.name + " to " + node.getName());
        } else {
            System.out.println("Edge already exists from " + this.name + " to " + node.getName());
        }
    }

    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();
        Set<Node> stack = new HashSet<>();
        return hasCycleHelper(this, visited, stack);
    }

    private boolean hasCycleHelper(Node current, Set<Node> visited, Set<Node> stack) {
        if (stack.contains(current)) {
            System.out.println("Cycle detected at node: " + current.getName());
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
