package graph;

import config.MainTrain;

import java.util.ArrayList;
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

    public void subscribe(Agent agent){
         if (!subs.contains(agent))
        subs.add(agent);
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

    public void addPublisher(Agent a){
        if (!pubs.contains(a))
            pubs.add(a);
    }

    public void removePublisher(Agent a){
        pubs.remove(a);
    }

    public Message getMessage(){
        return msg;
    }


}
