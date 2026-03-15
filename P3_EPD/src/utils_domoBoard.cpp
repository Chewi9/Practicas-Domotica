/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include "utils_domoBoard.h"
#include "HardwareSerial.h"

void Interruptor(tsSensor *Sensor)
{
	if(Sensor->valor_Df == Sensor->valor){
		Serial.print(Sensor->name);
		Serial.print(" : Interruptor --> ");
		if(Sensor->Aux == OFF){
			Sensor->Aux = ON;
			Serial.println("ON");
		}else{
			Sensor->Aux = OFF;
			Serial.println("OFF");
		}
	}

	if(Sensor->pin == BUTTON1_P){ // Provoca que solo funcione con el boton 1
		DomoBoard::setActuador(&domoboard.RELE, Sensor->Aux);
	}
}

void Pulsado_Soltado(tsSensor *Sensor){
	Serial.print(Sensor->name);
	if(Sensor->valor_Df == Sensor->valor){
		Serial.println(" --> Soltado");
	}else{
		Serial.println(" --> Pulsado");
	}

	if(Sensor->pin == BUTTON1_P){ // Provoca que solo funcione con el boton 1
		DomoBoard::setActuador(&domoboard.RELE, Sensor->valor);
	}
}

void conmutador(tsSensor *Sensor)
{
	static int valor = OFF;

	if(Sensor->valor_Df == Sensor->valor){
		Serial.print("Conmutador --> ");
		if(valor == OFF){
			valor = ON;
			Serial.println("ON");
		}else{
			valor = OFF;
			Serial.println("OFF");
		}

		DomoBoard::setActuador(&domoboard.RELE, valor);
	}
}





