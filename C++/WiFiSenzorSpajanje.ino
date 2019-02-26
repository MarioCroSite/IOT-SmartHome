#include <ESP8266WiFi.h>
#include <PubSubClient.h>

const char* ssid = "Algebra-HotSpot";
const char* password = "";
const String sensorDataTopic = "sensortopic";
const String triggerTopic = "triggertopic";
bool trigger=true;

WiFiServer server(23);
WiFiClient client;

PubSubClient mqttClient("172.20.9.105", 1883, client);

unsigned long readTime;

void setup() {
  // put your setup code here, to run once:

  Serial.begin(19200);
  Serial.println("Connecting..");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
  server.begin();
  Serial.println(WiFi.localIP());
  server.setNoDelay(true);
 
  if (mqttClient.connect("myClientID")) {
    mqttClient.subscribe(triggerTopic.c_str());
    mqttClient.setCallback(callback);
  } else {
    // connection failed
    // mqttClient.state() will provide more information
    // on why it failed.
  }
  readTime = millis();
}

void loop() {
  handleSerialData();
  
  mqttClient.loop();
  
  if(millis() > readTime + 5000 && trigger){
    readTime = millis();    
    Serial.write("sensor read\r");
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  char t=(char)payload[0];
  trigger = t=='1';
}

void handleSerialData() {
  if (Serial.available()) {
    publish_sensor_info(Serial.readString());
  }
}

void publish_sensor_info(String message){
  mqttClient.publish(sensorDataTopic.c_str(),message.c_str());
}
