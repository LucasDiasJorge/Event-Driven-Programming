package message.consumer.entities;

public class Form {

    private String message;
    private String topic;

    // No-argument constructor is required for deserialization
    public Form() {
    }

    public Form(String message, String topic) {
        this.message = message;
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
