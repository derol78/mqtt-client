import org.eclipse.paho.client.mqttv3.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MqttSubscriber {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String broker = properties.getProperty("broker", "tcp://localhost:1883");
        String topic = properties.getProperty("topic", "CIM_Analytics");
        String clientId = properties.getProperty("clientId", "JavaClient");
        String username = properties.getProperty("username", "test");
        String password = properties.getProperty("password", "test");
        boolean cleanSession = Boolean.parseBoolean(properties.getProperty("cleanSession", "false"));
        boolean debug = Boolean.parseBoolean(properties.getProperty("debug", "false"));

        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(cleanSession);
            options.setUserName(username);
            options.setPassword(password.toCharArray());

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                	if (debug) {
                		System.out.println("Message received on topic " + topic + ": " + new String(message.getPayload()));
                	}
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used in subscriber.
                }
            });

            client.connect(options);
            System.out.println("Connected to broker: " + broker);
            client.subscribe(topic, 1);
            System.out.println("Subscribed to topic: " + topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
