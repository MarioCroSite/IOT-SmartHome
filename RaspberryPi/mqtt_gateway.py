import paho.mqtt.client as mqtt
import air_sensor_parser
import threading


class Device:

    def __init__(self):
        self.subscriptions = {}

    def subscribe(self, topic, callback):
        self.subscriptions[topic] = callback
                       
    def start_listeners(self, client):
        for topic in self.subscriptions:
            print("Subscribing on topic: " + topic)
            client.subscribe(topic)

    def subscription_exists(self, topic):
        return topic in self.subscriptions
        
    def get_subscription_callback(self, topic):
        return self.subscriptions[topic]
    
    
class MqttClientThread(threading.Thread):
    
    def __init__(self, host, port, keepalive = 60):
        threading.Thread.__init__(self)  
        self.host = host
        self.port = port
        self.keepalive = keepalive
        self.client = mqtt.Client()
        self.client.on_connect = self.__on_connect
        self.client.on_message = self.__on_message
        self.client.on_publish = self.__on_publish
        self.devices = []
          
    def __on_connect(self, client, userdata, flags, rc):
        for device in self.devices:
            device.start_listeners(client)
        
    def __on_message(self, client, userdata, msg):
        for device in self.devices:
            if device.subscription_exists(msg.topic):                
                device.get_subscription_callback(msg.topic)(msg.payload)

    def __on_publish(self, client, userdata, mid):
        print("(" + self.host + ":" + str(self.port) + ") " + "Message Published...")
            
    def add_device(self, device):
        self.devices.append(device)
                                           
    def publish(self, topic, value):
        print("(" + self.host + ":" + str(self.port) + ") " + "Publishing on topic: " + topic + ", value: "  + value)
        self.client.publish(topic, value)  
        
    def run(self):
        self.client.connect(self.host, self.port, self.keepalive)
        self.client.loop_forever()  
        
                        
class MqttGateway:
    """ Mediator """
    
    # localhost
        #air
    AIR_SENSOR_STATE_SWITCH_LOCAL_BROKER_PUBLICATION_TOPIC = "triggertopic"
    AIR_SENSOR_DATA_LOCAL_BROKER_SUBSCRIPTION_TOPIC = "sensortopic"

        #plug
    PLUG_STATE_SWITCH_LOCAL_BROKER_PUBLICATION_TOPIC = "smarthome/node/83F91607004B1200/sensor/power/1027/switch/status"
    PLUG_STATE_REPORT_LOCAL_BROKER_SUBSCRIPTION_TOPIC = "smarthome/node/83F91607004B1200/sensor/power/1027/switch/status"
    PLUG_DATA_LOCAL_BROKER_SUBSCRIPTION_TOPIC = "smarthome/node/83F91607004B1200/sensor/power/1027/value/power"

    # remote VM
    AIR_STATE_SWITCH_REMOTE_BROKER_SUBSCRIPTION_TOPIC = "air_state"
    AIR_DATA_REMOTE_BROKER_PUBLICATION_TOPIC = "air"

    PLUG_STATE_SWITCH_REMOTE_BROKER_SUBSCRIPTION_TOPIC = "plug_state_switch"
    PLUG_STATE_REPORT_REMOTE_BROKER_PUBLICATION_TOPIC = "plug_state_report"
    PLUG_DATA_REMOTE_BROKER_PUBLICATION_TOPIC = "plug"

    def __init__(self, local_client_thread, remote_client_thread):    
        self.local_client_thread = local_client_thread
        self.remote_client_thread = remote_client_thread
        self.lock = threading.Lock()
        self.__setup()

    def __setup(self):                      
        air_sensor = Device()
        air_sensor.subscribe(MqttGateway.AIR_SENSOR_DATA_LOCAL_BROKER_SUBSCRIPTION_TOPIC, self.__on_air_data_received)   
        self.local_client_thread.add_device(air_sensor)
        
        plug = Device()
        plug.subscribe(MqttGateway.PLUG_DATA_LOCAL_BROKER_SUBSCRIPTION_TOPIC, self.__on_plug_data_received)
        plug.subscribe(MqttGateway.PLUG_STATE_REPORT_LOCAL_BROKER_SUBSCRIPTION_TOPIC, self.__on_plug_state_report_received)
        self.local_client_thread.add_device(plug)
        
        vm = Device()
        vm.subscribe(MqttGateway.AIR_STATE_SWITCH_REMOTE_BROKER_SUBSCRIPTION_TOPIC, self.__on_air_state_switch_received)
        vm.subscribe(MqttGateway.PLUG_STATE_SWITCH_REMOTE_BROKER_SUBSCRIPTION_TOPIC, self.__on_plug_state_switch_received)
        self.remote_client_thread.add_device(vm)

    def __on_plug_data_received(self, message_bytes):
        message_decoded = message_bytes.decode()
        print("Plug data received: " + message_decoded)
        with self.lock:
            self.remote_client_thread.publish(MqttGateway.PLUG_DATA_REMOTE_BROKER_PUBLICATION_TOPIC, message_decoded)

    def __on_plug_state_report_received(self, message_bytes):
        message_decoded = message_bytes.decode()
        print("Plug state received: " + message_decoded)
        with self.lock:
            self.remote_client_thread.publish(MqttGateway.PLUG_STATE_REPORT_REMOTE_BROKER_PUBLICATION_TOPIC, message_decoded)

    def __on_plug_state_switch_received(self, message_bytes):
        message_decoded = message_bytes.decode()
        print("Plug state received: " + message_decoded)
        with self.lock:
            self.local_client_thread.publish(MqttGateway.PLUG_STATE_SWITCH_LOCAL_BROKER_PUBLICATION_TOPIC, message_decoded)

    def __on_air_state_switch_received(self, message_bytes):
        message_decoded = message_bytes.decode()
        print("Air state received: " + message_decoded)
        with self.lock:
            self.local_client_thread.publish(MqttGateway.AIR_SENSOR_STATE_SWITCH_LOCAL_BROKER_PUBLICATION_TOPIC, message_decoded)

    def __on_air_data_received(self, message_bytes):
        json = air_sensor_parser.parse_bytes_into_json(message_bytes)
        print("Air data received: " + json)
        with self.lock:
            self.remote_client_thread.publish(MqttGateway.AIR_DATA_REMOTE_BROKER_PUBLICATION_TOPIC, json)

    def start(self):
        self.remote_client_thread.start()
        self.local_client_thread.start()        
        self.local_client_thread.join()
        
            

MqttGateway(
        MqttClientThread("localhost", 1883), 
        MqttClientThread("193.198.208.164", 15443)
        ).start()



