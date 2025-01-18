package graph;

public class IncAgent implements Agent {
    private final String[] subs;
    private final String[] pubs;

    public IncAgent(String[] subs, String[] pubs) {
        this.subs = subs;
        this.pubs = pubs;

        // Corrected to use TopicManager
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(subs[0]).subscribe(this);
        tm.getTopic(pubs[0]).addPublisher(this);
    }

    @Override
    public String getName() {
        return "IncAgent";
    }


    @Override
    public void callback(String topic, Message msg) {
        if (subs.length < 1 || pubs.length < 1) return;

        if (topic.equals(subs[0])) {
            double value = msg.asDouble;

            // Check if value is valid
            if (!Double.isNaN(value)) {
                double result = value + 1;
                Message resultMessage = new Message(result);
                TopicManagerSingleton.get().getTopic(pubs[0]).publish(resultMessage);
            }
        }
    }

    @Override
    public void close() {
        // Corrected to use TopicManager
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(subs[0]).unsubscribe(this);
        tm.getTopic(pubs[0]).removePublisher(this);
    }

    @Override
    public void reset() {
        // No state to reset for IncAgent
    }
}
