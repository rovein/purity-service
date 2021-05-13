#include <SPI.h>
#include <Ethernet.h>
#include <ArduinoJson.h>
#include "DHT.h"
#include "MQ135.h"

#define MQ_PIN  A4
#define DHT_PIN A5

byte mac[] = {0x90, 0xA2, 0xDA, 0x00, 0x82, 0x7A};

IPAddress server(192, 168, 0, 103);

int    HTTP_PORT = 8080;
String HTTP_METHOD = "POST";
String HOST_NAME = "localhost";
String PATH_NAME = "/device";
int    id = 5;

IPAddress ip(192, 168, 0, 104);
IPAddress myDns(192, 168, 0, 1);

EthernetClient client;

DHT dht(DHT_PIN, DHT11);
MQ135 mq = MQ135(MQ_PIN); 

float recHumidity = 50;
float recTemperature = 23;
float fullValue = 1;

float humidityDiff;
float temperatureDiff;
float totalDiff;

void setup() {
    Serial.begin(9600);

    while (!Serial) {}

    dht.begin();

    Serial.println("Ініціалізація з'єднання за допомогою DHCP:");

    if (Ethernet.begin(mac) == 0) {
        Serial.println("Не вдалося налаштувати Інтернет використовуючи DHCP");
        if (Ethernet.hardwareStatus() == EthernetNoHardware) {
            Serial.println("Інтернет модуль не знайдено");
        }
        if (Ethernet.linkStatus() == LinkOFF) {
            Serial.println("Інтернет кабель не під'єднано");
        }
        Ethernet.begin(mac, ip, myDns);
    } else {
        Serial.print("  DHCP встановлений IP ");
        Serial.println(Ethernet.localIP());
    }
    delay(1000);
    Serial.print("під'єднання до ");
    Serial.print(server);
    Serial.println("...");

    if (client.connect(server, HTTP_PORT)) {
        Serial.print("під'єднано ");
        Serial.println(client.remoteIP());
    } else {
        Serial.println("зв'язок з сервером не встановлено");
    }
}

void loop() {

    if (client.connect(server, HTTP_PORT)) {
        
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
        root["id"] = id;
        root["airQuality"] = airQuality;
        root["temperature"] = temperature;
       
        String data;
        root.printTo(data);

        client.println("POST " + PATH_NAME + " HTTP/1.1");
        client.println("Host: " + HOST_NAME);
        client.println("User-Agent: Arduino/1.0");
        client.println("Connection: close");
        client.println("Content-Type: application/json");
        client.print("Content-Length: ");
        client.println(data.length());
        client.println();
        client.println(data);

        printResponse(client);

    }

    delay(100000);

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
