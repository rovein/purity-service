#include <SPI.h>
#include <Ethernet.h>
#include <ArduinoJson.h>
#include "DHT.h"
#include "MQ135.h"
#include <TextFinder.h>

#define MQ_PIN  A4
#define DHT_PIN A5

const byte mac[] = {0x90, 0xA2, 0xDA, 0x00, 0x82, 0x7A};
IPAddress server;
unsigned int port;
unsigned int placementId;

IPAddress arduinoIp(192, 168, 0, 2);
IPAddress myDns(192, 168, 0, 1);
EthernetServer arduinoServer(80);
boolean isConfigured = false;

byte byte1;
byte byte2;
byte byte3;
byte byte4;
String readString = String(30);

EthernetClient client;

DHT dht(DHT_PIN, DHT11);
MQ135 mq = MQ135(MQ_PIN);

const float recHumidity = 50;
const float recTemperature = 23;
const float fullValue = 1;

float humidityDiff;
float temperatureDiff;
float totalDiff;

void setup() {
    Serial.begin(9600);

    while (!Serial) {}

    dht.begin();

    Serial.println("Ініціалізація з'єднання за допомогою DHCP:");

    Ethernet.begin(mac, arduinoIp);
    Serial.print(" встановлений IP ");
    Serial.println(Ethernet.localIP());
    arduinoServer.begin();

    Serial.println("Очікується конфігурація приладу...");
    while (!isConfigured) {
        EthernetClient configuringClient = arduinoServer.available();
        if (configuringClient) {
            TextFinder finder(configuringClient);
            while (configuringClient.connected()) {
                if (configuringClient.available()) {
                    if (finder.find("GET")) {
                        initializeConfiguringValues(finder);
                        server = IPAddress(byte1, byte2, byte3, byte4);
                        isConfigured = true;

                        configuringClient.println("HTTP/1.1 200 OK");
                        configuringClient.println("Content-Type: text/plain");
                        configuringClient.println("Access-Control-Allow-Origin: *");
                        configuringClient.println();
                        configuringClient.println(
                                "Arduino was successfully configured!");
                        configuringClient.stop();
                    }
                }
            }
        }
    }

    Serial.print("під'єднання до ");
    Serial.print(server);
    Serial.println("...");

    if (client.connect(server, port)) {
        Serial.print("під'єднано ");
        Serial.println(client.remoteIP());
    } else {
        Serial.println("зв'язок з сервером не встановлено");
    }
}

void loop() {

    if (client.connect(server, port)) {
        float humidity = dht.readHumidity();
        float temperature = dht.readTemperature();
        float airPollution = mq.getPPM() / 10;
        Serial.println(
                "Вологість: " + String(humidity) + " %\t" +
                "Температура: " + String(temperature) + " *C\t");

        humidityDiff = getPercentDifference(humidity, recHumidity);
        temperatureDiff = getPercentDifference(temperature, recTemperature);
        totalDiff = temperatureDiff + humidityDiff;

        printDifference();

        const size_t capacity = JSON_OBJECT_SIZE(5);
        DynamicJsonBuffer jsonBuffer(capacity);

        float adjustmentFactor = fullValue + totalDiff;
        float airQuality = fullValue - airPollution;

        JsonObject &root = jsonBuffer.createObject();
        root["adjustmentFactor"] = adjustmentFactor;
        root["humidity"] = humidity;
        root["id"] = placementId;
        root["airQuality"] = airQuality;
        root["temperature"] = temperature;

        String data;
        root.printTo(data);

        client.println("POST /device HTTP/1.1");
        client.println("Host: localhost");
        client.println("User-Agent: Arduino/1.0");
        client.println("Connection: close");
        client.println("Content-Type: application/json");
        client.print("Content-Length: ");
        client.println(data.length());
        client.println();
        client.println(data);

        printResponse(client);
    }

    delay(10000);
}

float getPercentDifference(float x, float y) {
    float difference;
    if (x > y) {
        difference = 1 - (y / x);
    } else {
        difference = 1 - (x / y);
    }
    return difference;
}

void printResponse(EthernetClient client) {
    while (client.connected()) {
        if (client.available()) {
            char c = client.read();
            Serial.print(c);
        }
    }
}

void printDifference() {
    Serial.println(
            "Відхилення вологості від норми: " +
            String(humidityDiff * 100) + " %\t");

    Serial.println(
            "Відхилення температури від норми: " +
            String(temperatureDiff * 100) + " %\t");

    Serial.println(
            "Загальне відхилення: " +
            String(totalDiff * 100) + " %\t");
}

void initializeConfiguringValues(TextFinder finder) {
    if (finder.find("byte1=")) {
        byte1 = finder.getValue();
    }
    if (finder.find("byte2=")) {
        byte2 = finder.getValue();
    }
    if (finder.find("byte3=")) {
        byte3 = finder.getValue();
    }
    if (finder.find("byte4=")) {
        byte4 = finder.getValue();
    }
    if (finder.find("port=")) {
        port = finder.getValue();
    }
    if (finder.find("roomId=")) {
        placementId = finder.getValue();
    }
}
