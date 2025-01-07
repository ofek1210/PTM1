package graph;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Topic {
    public final String name;
    public List<Agent> subs=new CopyOnWriteArrayList<>();
    public List<Agent> pubs=new CopyOnWriteArrayList<>();
    public Message msg;
    Topic(String name){
        if (name==null || name.isEmpty()){
            throw new IllegalArgumentException("Topic name cannot be null or empty");
        }
        this.name = name;
    }

    public void subscribe(Agent agent) {
        if (!subs.contains(agent)) {
            subs.add(agent);
            System.out.println("Subscribed agent: " + agent.getName() + " to topic: " + name);
        } else {
            System.out.println("Agent already subscribed: " + agent.getName() + " to topic: " + name);
        }
    }

    public void unsubscribe(Agent a) {
        subs.remove(a);
    }

    public void publish(Message m) {
        msg = m;
        for (Agent agent : subs) {
            agent.callback(this.name,msg);
        }
    }

    public void addPublisher(Agent agent) {
        if (!pubs.contains(agent)) {
            pubs.add(agent);
            System.out.println("Added publisher agent: " + agent.getName() + " to topic: " + name);
        } else {
            System.out.println("Agent already a publisher: " + agent.getName() + " to topic: " + name);
        }
    }


    public void removePublisher(Agent a){
        pubs.remove(a);
    }

    public Message getMessage(){
        return msg;
    }


}
