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
        Set<Node> visited = new HashSet<>(); // סט של צמתים שכבר ביקרנו בהם

        // מעבר על כל הצמתים בגרף
        for (Node node :this.getEdges()) {
            if (!visited.contains(node)) {
                // אם מוצאים מעגל ברכיב קשיר כלשהו - מחזירים true
                if (dfs(node, visited, new HashSet<>())) {
                    return true;
                }
            }
        }
        return false; // אם לא נמצא מעגל בשום רכיב קשיר, מחזירים false
    }

    private boolean dfs(Node current, Set<Node> visited, Set<Node> stack) {
        visited.add(current); // מסמנים את הצומת כצומת שביקרנו בו
        stack.add(current);   // מוסיפים את הצומת לסט הצמתים שבשביל הנוכחי

        for (Node neighbor : current.getEdges()) {
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, visited, stack)) {
                    return true; // נמצא מעגל
                }
            } else if (stack.contains(neighbor)) {
                return true; // נמצא מעגל (חזרה לצומת שכבר נמצא בסט השביל)
            }
        }

        stack.remove(current); // מסירים את הצומת מהשביל (חוזרים אחורה ב-DFS)
        return false;
    }
}