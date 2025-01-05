package config;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

import java.util.function.BinaryOperator;

public class BinOpAgent implements Agent {
    private String agentName;
    private String topic1;
    private String topic2;
    private String output;
    private BinaryOperator<Double> _op;
    private Double input1 = null;
    private Double input2 = null;
    private TopicManager tm;


    BinOpAgent(String agentName,String topic1,String topic2,String output,BinaryOperator<Double> op){
        this.topic1 = topic1;
        this.topic2 = topic2;
        this.output = output;
        this._op = op;
        this.agentName = agentName;
        this.tm = TopicManagerSingleton.get();
        tm.getTopic(topic1).subscribe(this);
        tm.getTopic(topic2).subscribe(this);
    }

    public void callback(String topic, Message msg) {
        if (msg.asDouble != Double.NaN) {
            if (topic.equals(this.topic1)) {
                input1 = msg.asDouble;
            } else if (topic.equals(this.topic2)) {
                input2 = msg.asDouble;
            }

            if (input1 != null && input2 != null) {
                double result = _op.apply(input1, input2);
                tm.getTopic(output).publish(new Message(result));
                reset();  // מאפס את הקלטים לאחר הפרסום
            }
        }
    }


    public void reset() {
        input1 = null;
        input2 = null;
    }


    public String getName() {
        return agentName;
    }


    public void close() {
        tm.getTopic(topic1).unsubscribe(this);
        tm.getTopic(topic2).unsubscribe(this);
    }
}
    

