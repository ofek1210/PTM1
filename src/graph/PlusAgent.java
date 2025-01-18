package graph;

public class PlusAgent implements Agent {
    private final String[] subs;
    private final String[] pubs;
    private double x = 0, y = 0; // Default values for internal state

    public PlusAgent(String[] subs, String[] pubs) {
        this.subs = subs;
        this.pubs = pubs;

        // Corrected to use TopicManager
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(subs[0]).subscribe(this);
        tm.getTopic(subs[1]).subscribe(this);
        tm.getTopic(pubs[0]).addPublisher(this);
    }

    @Override
    public String getName() {
        return "PlusAgent";
    }


    @Override
    public void callback(String topic, Message msg) {
        if (subs.length < 2 || pubs.length < 1) return;

        if (topic.equals(subs[0])) {
            x = msg.asDouble; // Update x if the topic matches
        } else if (topic.equals(subs[1])) {
            y = msg.asDouble; // Update y if the topic matches
        }

        // Check if both x and y are valid numbers before calculating
        if (!Double.isNaN(x) && !Double.isNaN(y)) {
            double result = x + y;
            Message resultMessage = new Message(result);
            TopicManagerSingleton.get().getTopic(pubs[0]).publish(resultMessage);

        }
    }

    @Override
    public void close() {
        // Corrected to use TopicManager
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(subs[0]).unsubscribe(this);
        tm.getTopic(subs[1]).unsubscribe(this);
        tm.getTopic(pubs[0]).removePublisher(this);
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
    }
}
