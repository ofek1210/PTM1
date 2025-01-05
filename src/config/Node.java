package config;

import graph.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {
    private String name;
    private List<Node> edges;
    private Message msg;
    Node(String name) {

        this.name = name;
        this.edges = new ArrayList<>();
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
        edges.add(node);
    }




    public boolean hasCycles() {
        Set<Node> visited = new HashSet<Node>(); //הגדרה של סט צמתים שפגשתי
        Set<Node> stack = new HashSet<Node>();
        return dfs(this,visited,stack);
    }


    private boolean dfs(Node current, Set<Node> visited, Set<Node> stack) {
     visited.add(current);
     stack.add(current);
     for (Node n : current.getEdges()) {
         if (!visited.contains(n)) {
             if (dfs(n,visited,stack)) {
                 return true;
             }
         }else if (stack.contains(n)) {
            return true;
         }

     }
     stack.remove(current);
     return false;
    }
}