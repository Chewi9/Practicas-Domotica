#include <Arduino.h>

// Guardamos el estado anterior de cada botón
bool ant11 = HIGH;
bool ant10 = HIGH;
bool ant3  = HIGH;

// Estados de los Interruptores(ON/OFF)
bool estadoInterruptor11 = false;
bool estadoInterruptor10 = false;
bool estadoInterruptor3  = false;

void setup() {
  pinMode(11, INPUT_PULLUP);
  pinMode(10, INPUT_PULLUP);
  pinMode(3, INPUT_PULLUP);
  Serial.begin(115200);
  ant11 = digitalRead(11);
  ant10 = digitalRead(10);
  ant3  = digitalRead(3);
}

void loop() {
  // INTERRUPTOR OCTOPULSADOR
  bool act11 = digitalRead(11);
  if (act11 == LOW && ant11 == HIGH) {
    delay(50); // Posible cambiar a 100 ms para evitar rebotes
    estadoInterruptor11 = !estadoInterruptor11;
    Serial.print("Boton Octopulsador: ");
    Serial.println(estadoInterruptor11 ? "ON" : "OFF"); // Imprime el estado actual del interruptor, si es true imprime "ON", si es false imprime "OFF"
  }
  ant11 = act11;

  // INTERRUPTOR BOTÓN 1
  bool act10 = digitalRead(10);
  if (act10 == LOW && ant10 == HIGH) {
    delay(50);
    estadoInterruptor10 = !estadoInterruptor10;
    Serial.print("Boton 1: ");
    Serial.println(estadoInterruptor10 ? "ON" : "OFF"); // Imprime el estado actual del interruptor, si es true imprime "ON", si es false imprime "OFF"
  }
  
  ant10 = act10;

  // INTERRUPTOR BOTÓN 2
  bool act3 = digitalRead(3);
  if (act3 == LOW && ant3 == HIGH) {
    delay(50);
    estadoInterruptor3 = !estadoInterruptor3;
    Serial.print("Boton 2: ");
    Serial.println(estadoInterruptor3 ? "ON" : "OFF"); // Imprime el estado actual del interruptor, si es true imprime "ON", si es false imprime "OFF"
  }
  ant3 = act3;
}