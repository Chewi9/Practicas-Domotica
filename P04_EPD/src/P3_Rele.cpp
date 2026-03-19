#include <Arduino.h>
#include "debuglog.h"
#include "cfg_loop.h"
#include "types.h"
#include "QueueList.h"  
#include "domoBoard.h"
#include "config_practicas.h"

/*******************************************************/
/***                    Variables                    ***/
/*******************************************************/
QueueList<void_callback_f> _loop_callbacks;

/*********************************************************************/
/***                    Prototipos de funciones                    ***/
/*********************************************************************/
void main_loop();

void epdRegisterLoop(void_callback_f callback) {
    _loop_callbacks.push(callback);
}

void setup() {

    config_practica3_apt_2();

    Serial.begin(115200);

    epdRegisterLoop(leeSensores);

    DEBUGLNF("P03 Relé");
}

void loop() {
    EXECUTELOOP(){
		UPDATELOOP();

		main_loop();

	}
}

void main_loop(){
	// Call registered loop callbacks

	for (uint8_t i = 0; i < _loop_callbacks.count(); i++) {
		(_loop_callbacks.peek(i))();
	}

}