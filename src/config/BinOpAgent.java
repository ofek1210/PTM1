package config;

import graph.Agent;
import graph.Message;
import graph.Topic;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

import java.util.function.BinaryOperator;

public class BinOpAgent implements Agent {
    private String agentName;
    private String topic1;
    private String topic2;
    private String output;
    private BinaryOperator<Double> _op;
    private TopicManager tm;
    private Double input1 = null;
    private Double input2 = null;

    public BinOpAgent(String agentName, String topic1, String topic2, String output, BinaryOperator<Double> op) {
        this.agentName = agentName;
        this.topic1 = topic1;
        this.topic2 = topic2;
        this.output = output;
        this._op = op;
        this.tm = TopicManagerSingleton.get();

        // יצירת נושאי הקלט והפלט אם הם לא קיימים
        tm.createTopicIfAbsent(topic1);
        tm.createTopicIfAbsent(topic2);
        tm.createTopicIfAbsent(output);

        // הרשמה לנושאי הקלט
        tm.getTopic(topic1).subscribe(this);
        tm.getTopic(topic2).subscribe(this);
    }



    @Override
    public void callback(String topic, Message msg) {
        if (topic.equals(topic1)) {
            input1 = msg.asDouble;
        } else if (topic.equals(topic2)) {
            input2 = msg.asDouble;
        }

        // בודקים אם שני הקלטים זמינים
        if (input1 != null && input2 != null) {
            double result = _op.apply(input1, input2); // הפעלת הפעולה הבינארית
            tm.getTopic(output).publish(new Message(result)); // פרסום התוצאה לנושא הפלט
            reset(); // איפוס הקלטים לאחר הפרסום
        }
    }

    @Override
    public String getName() {
        return agentName;
    }

    @Override
    public void reset() {
        input1 = null;
        input2 = null;
    }

    @Override
    public void close() {
        tm.getTopic(topic1).unsubscribe(this);
        tm.getTopic(topic2).unsubscribe(this);
    }
}
