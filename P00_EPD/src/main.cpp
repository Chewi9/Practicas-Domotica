#include <Arduino.h>

void setup() {
  // put your setup code here, to run once:
  pinMode(13, OUTPUT); // Led
  pinMode(10, INPUT_PULLUP); // Button 1
}

void loop() {
  // put your main code here, to run repeatedly:
  if(digitalRead(10) == HIGH){ // If button 1 is pressed
    digitalWrite(13, HIGH);   // turn the LED on by making the voltage HIGH
  }

  if(digitalRead(3) == HIGH){ // If button 2 is pressed
    digitalWrite(13, LOW);    // turn the LED off by making the voltage LOW
  }
}