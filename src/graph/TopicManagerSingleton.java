package graph;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TopicManagerSingleton {

    public static class TopicManager{

        Map<String,Topic> topics;
        
        private TopicManager(){
            topics=new ConcurrentHashMap<>();
        }
    
        public synchronized Topic getTopic(String name){
            if(!topics.containsKey(name))
                topics.put(name, new Topic(name));
            return topics.get(name);
        }

        private static final TopicManager instance=new TopicManager();

        public Collection<Topic> getTopics() {
            return topics.values();
        }

        public void clear(){
            topics.clear();
        }
    }

    public static TopicManager get(){
        return TopicManager.instance;
    }
    
}