import org.eclipse.paho.client.mqttv3.*;

public class MqttSubscriber {
    public static void main(String[] args) {
        String broker = "tcp://localhost:1883"; // Replace with your broker's address.
        String topic = "CIM_Analytics";
        String clientId = "JavaClient";
        String username = "test";
        String password = "test";

        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(username);
            options.setPassword(password.toCharArray());

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Message received on topic " + topic + ": " + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used in subscriber.
                }
            });

            client.connect(options);
            System.out.println("Connected to broker: " + broker);
            client.subscribe(topic,1);
            System.out.println("Subscribed to topic: " + topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
