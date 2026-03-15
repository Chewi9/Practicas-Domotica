#include <Arduino.h>

// Guardamos el estado anterior de cada botón
bool ant11 = HIGH;
bool ant10 = HIGH;
bool ant3  = HIGH;

void setup() {
  pinMode(10, INPUT_PULLUP);
  pinMode(3, INPUT_PULLUP);
  pinMode(11, INPUT_PULLUP);
  Serial.begin(115200);

  ant11 = digitalRead(11); // Para que no salga un mensaje al iniciar el programa
  ant10 = digitalRead(10); // Para que no salga un mensaje al iniciar el programa
  ant3  = digitalRead(3); // Para que no salga un mensaje al iniciar el programa
}

void loop() {
  // BOTÓN Octopulsador
  bool act11 = digitalRead(11);
  if (act11 != ant11) {
    delay(0);
    if (act11 == LOW) {
      Serial.println("Boton Octopulsador: PULSADO");
    } else {
      Serial.println("Boton Octopulsador: SOLTADO");
    }
  }
  ant11 = act11;

  // BOTÓN 1
  bool act10 = digitalRead(10);
  if (act10 != ant10) {
    delay(0);
    if (act10 == LOW) {
      Serial.println("Boton 1: SOLTADO");
    } else {
      Serial.println("Boton 1: PULSADO");
    }
  }
  ant10 = act10;

  // BOTÓN 2
  bool act3 = digitalRead(3);
  if(act3 != ant3){
    delay(0);
    if(act3 == LOW){
      Serial.println("Boton 2: SOLTADO");
    } else{
      Serial.println("Boton 2: PULSADO");
    }
  }
  ant3 = act3;
}