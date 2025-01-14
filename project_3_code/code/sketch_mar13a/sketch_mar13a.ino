#include <ESP32Servo.h>

#include "WiFi.h"
#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include "FirebaseESP32.h"

#include <SPI.h>
#include <MFRC522.h>

// Định nghĩa chân RFID
#define RST_PIN 22              // Chân Reset cho module RFID
#define SS_PIN 21               // Chân Slave Select (CS) cho module RFID
MFRC522 rfid(SS_PIN, RST_PIN);  // Tạo một đối tượng RFID

#define DHTPIN 4
#define GAS_SENSOR_PIN 34
DHT dht(DHTPIN, DHT11);
const int den1 = 5;
const int den2 = 17;
const int den3 = 16;

const int servoPin = 25;
Servo myServo;

const char *ssid = "lenovoAVC";
const char *password = "123456788";
#define FIREBASE_AUTH "xFYPzX4WpHt9ERfkSveAyI25gdoXTUi3IRn564on"
#define FIREBASE_HOST "https://tesst-7220a-default-rtdb.firebaseio.com/"
FirebaseData firebaseData;
FirebaseData fbdo;
String path = "/";

// Hàm khởi tạo RFID
void initRFID() {
  SPI.begin();      // Khởi tạo giao tiếp SPI
  rfid.PCD_Init();  // Khởi tạo module MFRC522
  Serial.println("RFID Initialized.");
}

// Hàm đọc RFID và gửi UID lên Firebase
void readRFID() {
  // Kiểm tra xem có thẻ mới không
  if (!rfid.PICC_IsNewCardPresent() || !rfid.PICC_ReadCardSerial()) {
    return;  // Nếu không có thẻ mới hoặc không đọc được thẻ, thoát
  }

  // Đọc UID từ thẻ
  String uid = "";  // Chuỗi lưu UID của thẻ
  for (byte i = 0; i < rfid.uid.size; i++) {
    uid += String(rfid.uid.uidByte[i], HEX);  // Thêm từng byte của UID vào chuỗi
  }
  uid.toUpperCase();  // Chuyển UID thành chữ in hoa
  Serial.print("Card UID: ");
  Serial.println(uid);

  // Gửi UID lên Firebase
  if (Firebase.setString(firebaseData, "RFID", uid)) {
    Serial.println("RFID data sent successfully");
  } else {
    Serial.println("Failed to send RFID data");
  }

  // Dừng giao tiếp với thẻ để chuẩn bị đọc thẻ tiếp theo
  rfid.PICC_HaltA();
  rfid.PCD_StopCrypto1();
}

void initFirebase() {
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
  if (!Firebase.beginStream(firebaseData, path)) {
    Serial.println("REASON: " + firebaseData.errorReason());
    Serial.println();
  }
  Serial.println("Kết nối Firebase thành công");
}

void setup() {
  Serial.begin(115200);
  Serial.println();
  Serial.print("Connecting to wifi: ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected, IP address: ");
  Serial.println(WiFi.localIP());  // In ra địa chỉ IP của ESP32
  initFirebase();                  // Kết nối Firebase
  // initRFID();
  dht.begin();
  pinMode(den1, OUTPUT);
  pinMode(den2, OUTPUT);
  pinMode(den3, OUTPUT);
  pinMode(GAS_SENSOR_PIN, INPUT);
  myServo.attach(servoPin);
  myServo.write(0);
}
void sendGasData(){
  float gasValue = analogRead(GAS_SENSOR_PIN);
  if(Firebase.setFloat(firebaseData, "GAS", gasValue)){
    Serial.println(gasValue);
    Serial.println("Gas value data sent successfully");
  }
  else{
    Serial.println("Failed to send GAS data");
  }
}

// Hàm đọc và gửi dữ liệu nhiệt độ lên Firebase
void sendTemperatureData() {
  float temperature = dht.readTemperature();
  if (Firebase.setFloat(firebaseData, "TEMP", temperature)) {
    Serial.println(temperature);
    Serial.println("Temperature data sent successfully");
  } else {
    Serial.println("Failed to send temperature data");
  }
}

// Hàm đọc và gửi dữ liệu độ ẩm lên Firebase
void sendHumidityData() {
  float humidity = dht.readHumidity();
  if (Firebase.setFloat(firebaseData, "HUMINITY", humidity)) {
    Serial.println(humidity);
    Serial.println("Humidity data sent successfully");
  } else {
    Serial.println("Failed to send humidity data");
  }
}

// Hàm đọc và điều khiển đèn 1
void controlDen1() {
  if (Firebase.RTDB.getInt(&fbdo, "DEN 1")) {
    int den1Value = fbdo.intData();  // Lấy giá trị kiểu int
    Serial.print("Giá trị DEN 1: ");
    Serial.println(den1Value);

    // Điều khiển đèn dựa trên giá trị
    if (den1Value == 1) {
      digitalWrite(den1, HIGH);
    } else {
      digitalWrite(den1, LOW);
    }
  } else {
    // Nếu đọc thất bại, in ra lỗi
    Serial.print("Lỗi khi đọc DEN 1: ");
    Serial.println(fbdo.errorReason());
  }
}

// Hàm đọc và điều khiển đèn 2
void controlDen2() {
  if (Firebase.RTDB.getInt(&fbdo, "DEN 2")) {
    int den2Value = fbdo.intData();  // Lấy giá trị kiểu int
    Serial.print("Giá trị DEN 2: ");
    Serial.println(den2Value);

    // Điều khiển đèn dựa trên giá trị
    if (den2Value == 1) {
      digitalWrite(den2, HIGH);
    } else {
      digitalWrite(den2, LOW);
    }
  } else {
    // Nếu đọc thất bại, in ra lỗi
    Serial.print("Lỗi khi đọc DEN 2: ");
    Serial.println(fbdo.errorReason());
  }
}

// Hàm đọc và điều khiển đèn 3
void controlDen3() {
  if (Firebase.RTDB.getInt(&fbdo, "DEN 3")) {
    int den3Value = fbdo.intData();  // Lấy giá trị kiểu int
    Serial.print("Giá trị DEN 3: ");
    Serial.println(den3Value);

    // Điều khiển đèn dựa trên giá trị
    if (den3Value == 1) {
      digitalWrite(den3, HIGH);
    } else {
      digitalWrite(den3, LOW);
    }
  } else {
    // Nếu đọc thất bại, in ra lỗi
    Serial.print("Lỗi khi đọc DEN 3: ");
    Serial.println(fbdo.errorReason());
  }
}

void controlServo() {
  if (Firebase.RTDB.getInt(&fbdo, "SERVO")) {
    int servoValue = fbdo.intData();  // Lấy giá trị từ Firebase
    Serial.print("Giá trị SERVO: ");
    Serial.println(servoValue);

    if (servoValue == 1) {  // Nếu giá trị là 1, xoay servo đến 90 độ
      myServo.write(90);
      Serial.println("Servo xoay đến 90 độ");
    } else {  // Nếu không, trả về góc 0
      myServo.write(0);
      Serial.println("Servo xoay về 0 độ");
    }
  } else {
    // Nếu đọc thất bại, in ra lỗi
    Serial.print("Lỗi khi đọc SERVO: ");
    Serial.println(fbdo.errorReason());
  }
}

// Hàm loop chạy liên tục
void loop() {
  sendGasData();
  sendTemperatureData();
  sendHumidityData();
  controlDen1();
  controlDen2();
  controlDen3();
  controlServo();
  // readRFID();  // Gọi hàm đọc RFID
  delay(200);  // Đợi 2 giây trước khi lặp lại
}
