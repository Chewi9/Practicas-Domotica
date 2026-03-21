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

#define 	HEADER_BYTE     '#'  	// Byte de cabecera

byte 	cmdSerial[2];

QueueList<void_callback_f> _loop_callbacks;

/*********************************************************************/
/***                    Prototipos de funciones                    ***/
/*********************************************************************/
void foundCommand(){
	DomoBoard::setActuator(&domoboard.RELE, LOW);
	DomoBoard::setActuator(&domoboard.TRIAC, LOW);

	switch(cmdSerial[1]){
		case '2':
			config_practica4_apt_2();
			Serial.println("Apartado 2 Seleccionado");
			break;
		case '3':
			config_practica4_apt_3();
			Serial.println("Apartado 3 Seleccionado");
			break;
		case '4':
			config_practica4_apt_4();
			Serial.println("Apartado 4 Seleccionado");
			break;
		case '5':
			config_practica4_apt_5();
			Serial.println("Apartado 5 Seleccionado");
			break;
	}
}

void readCommand(){
	while(Serial.available()){
        byte c = Serial.read();

        // Desplazar los bytes en el buffer (Tu lógica de la P1)
        for (uint8_t i = 0; i < sizeof(cmdSerial) - 1; i++) {
            cmdSerial[i] = cmdSerial[i + 1];
        }
        
        cmdSerial[sizeof(cmdSerial) - 1] = c; 

        // Verificar si comando encontrado
        if ((cmdSerial[0] == HEADER_BYTE)) {
            // Comprueba si el comando es de la P1 (1-3) o de la P4 (4-7)
            if(cmdSerial[1] >= '1' && cmdSerial[1] <= '7'){
                foundCommand();
            }
        }
    }
}



void main_loop();

void epdRegisterLoop(void_callback_f callback) {
    _loop_callbacks.push(callback);
}

void setup() {

    config_practica4_apt_2();

    Serial.begin(115200);

    epdRegisterLoop(leeSensores);

    DEBUGLNF("P04 Triac");
}

void loop() {
    EXECUTELOOP(){
		UPDATELOOP();

		main_loop();


		readCommand();
	}
}

void main_loop(){
	// Call registered loop callbacks

	for (uint8_t i = 0; i < _loop_callbacks.count(); i++) {
		(_loop_callbacks.peek(i))();
	}

}