package graph;

import config.MainTrain;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    public final String name;
    public List<Agent> subs;
    public List<Agent> pubs;
    Topic(String name){
        this.subs = new ArrayList<>();
        this.pubs = new ArrayList<>();
        this.name=name;
    }

    public void subscribe(Agent agent){
         if (!subs.contains(agent))
        subs.add(agent);
    }
    public void unsubscribe(Agent a) {
        subs.remove(a);
    }

    public void publish(Message m) {
        for (Agent a : subs) {
            a.callback(this.name, m);  // קריאה למתודה callback של הסוכן
        }
    }

    public void addPublisher(Agent a){
        if (!pubs.contains(a))
            pubs.add(a);
    }

    public void removePublisher(Agent a){
        pubs.remove(a);
    }


}
