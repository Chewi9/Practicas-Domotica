#include <Arduino.h>

// Guardamos el estado anterior de cada botón
bool anterior11 = HIGH;
bool anterior10 = HIGH;
bool anterior3  = HIGH;

// Variable compartida por los tres botones
bool estadoConmutador = false; 

void setup() {
  pinMode(11, INPUT_PULLUP);
  pinMode(10, INPUT_PULLUP);
  pinMode(3, INPUT_PULLUP);
  
  Serial.begin(115200); // Es el monitor speed que configuramos en platformio.ini

  anterior11 = digitalRead(11);
  anterior10 = digitalRead(10);
  anterior3  = digitalRead(3);
}

void loop() {
  // Leemos los tres botones
  bool actual11 = digitalRead(11);
  bool actual10 = digitalRead(10);
  bool actual3  = digitalRead(3);

  bool huboPulsacion = false; // Variable para saber si alguno de los botones fue pulsado

  // Comprobamos si el Botón Octopulsador se ha pulsado
  if (actual11 == LOW && anterior11 == HIGH) {
    huboPulsacion = true;
    Serial.print("[Boton Octopulsador] ");
  }

  // Comprobamos si el Botón 1 se ha pulsado
  else if (actual10 == LOW && anterior10 == HIGH) {
    huboPulsacion = true;
    Serial.print("[Boton 1] ");
  }

  // Comprobamos si el Botón 2 se ha pulsado
  else if (actual3 == LOW && anterior3 == HIGH) {
    huboPulsacion = true;
    Serial.print("[Boton 2] ");
  }

  // Si cualquiera fue pulsado
  if (huboPulsacion) {
    delay(0); // Posible cambiar a 100
    estadoConmutador = !estadoConmutador;
    
    Serial.print("Estado del sistema: ");
    Serial.println(estadoConmutador ? "ACTIVADO (ON)" : "DESACTIVADO (OFF)");
  }

  // Actualizamos los estados anteriores
  anterior11 = actual11;
  anterior10 = actual10;
  anterior3  = actual3;
}