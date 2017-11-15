#include <PubSubClient.h>
#include <ESP8266WiFi.h>

// Librairie de gestion RFID
#include "MFRC522.h"

// Broche commandant le relai
#define RELAY 5

#include <EEPROM.h>
#include <ArduinoJson.h>


const char* host = "192.168.43.154";

// Définition de la broche SS (Select) qui va être utilisé pour sélectionner
// Le chip -RC522
#define SS_PIN  15 
//------ Broche pilotant le reset du chip RC522
#define RST_PIN  16 // RST-PIN for RC522 - RFID - SPI - Modul GPIO5

MFRC522 mfrc522(SS_PIN, RST_PIN); // Create MFRC522 instance


void setup()
{
Serial.begin(115200);    // Initialize serial communications
EEPROM.begin(512);

SPI.begin();           // Init SPI bus
mfrc522.PCD_Init();    // Init MFRC522
Serial.println ("Serial init .. PASS");
// Configuration PIN de commande du relai
pinMode(RELAY, OUTPUT);
digitalWrite(RELAY, LOW);


Serial.print("EEprom[0]= ");
Serial.println(EEPROM.read(0));


//Wifi connection
WiFi.begin("Muchas Arachas", "tchikitchiki");

  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  Serial.print("Connected, IP address: ");
  Serial.println(WiFi.localIP());
}

void loop()
{
  String cardUid = "";
  StaticJsonBuffer<300> jsonBuffer;

  // Look for new cards
  //Serial.print(".");
  if ( ! mfrc522.PICC_IsNewCardPresent()) {
    delay(50);
    return;
  }
  
  
  // Select one of the cards
  Serial.println("card found ...");
  if ( ! mfrc522.PICC_ReadCardSerial()) {
    Serial.println("Cannot read card");
    delay(50);
    return;
  }

  cardUid = getCardUid(mfrc522.uid.uidByte, mfrc522.uid.size);
  Serial.println("Card UID : " + cardUid);

  JsonObject& json = jsonBuffer.createObject();
  json["UID"] = cardUid;

  json.printTo(Serial);
  Serial.println("");

  char JSONmessageBuffer[40];
  json.printTo(JSONmessageBuffer, sizeof(JSONmessageBuffer));

  Serial.println(JSONmessageBuffer);

  // Use WiFiClient class to create TCP connections
  WiFiClient client;
  const int port = 3586;
  if (!client.connect(host, port)) {
    Serial.println("connection failed");
    return;
  }

  // This will send the request to the server
  client.println(JSONmessageBuffer);
  
  Serial.println("[Response:]");
  //read back one line from server
  String line = client.readStringUntil('.');
  Serial.println(line);

  Serial.println("closing connection");
  client.stop();
  Serial.println("\n[Disconnected]");

  if(line == "1"){
    Serial.println("OK");
    openDoor();
  }
  else{
    Serial.println("NOPE");
    lockDoor();
  }
  
  delay(1000);
}

String getCardUid(byte *buffer, byte bufferSize)
{
  String uid = "";
  for (byte i = 0; i < bufferSize; i++) {
    if(i>0 && i< bufferSize) uid = uid + ".";
    uid = uid + String(buffer[i]);
  }
  return uid;
}

void openDoor()
{
  digitalWrite(RELAY, HIGH);
  delay(200);
  digitalWrite(RELAY, LOW);
}


void lockDoor()
{
  digitalWrite(RELAY, HIGH);
  delay(200);
  digitalWrite(RELAY, LOW);
  delay(200);
  digitalWrite(RELAY, HIGH);
  delay(200);
  digitalWrite(RELAY, LOW);
  delay(200);
  digitalWrite(RELAY, HIGH);
  delay(200);
  digitalWrite(RELAY, LOW);
}

