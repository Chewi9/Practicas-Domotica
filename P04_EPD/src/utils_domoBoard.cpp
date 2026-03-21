/*
 * utils_domoBoard.cpp
 *
 *  Created on: 10/03/2015
 *      Author: jctejero
 */

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include "utils_domoBoard.h"
#include "HardwareSerial.h"

void Interruptor(void *Sensor)
{
	if(((ptsSensor)Sensor)->valor_Df == ((ptsSensor)Sensor)->valor){
		Serial.print(((ptsSensor)Sensor)->name);
		Serial.print(" : Interruptor --> ");
		if(((ptsSensor)Sensor)->Aux == OFF){
			((ptsSensor)Sensor)->Aux = ON;
			Serial.println("ON");
		}else{
			((ptsSensor)Sensor)->Aux = OFF;
			Serial.println("OFF");
		}
	}
}

void Pulsado_Soltado(void *Sensor){
	ptsSensor sensor = reinterpret_cast<ptsSensor>(Sensor);

	Serial.print(sensor->name);
	if(sensor->valor_Df == sensor->valor){
		Serial.println(" --> Soltado");
	}else{
		Serial.println(" --> Pulsado");
	}
}

void conmutador(void *Sensor)
{
	static bool valor = OFF;

	ptsSensor sensor = reinterpret_cast<ptsSensor>(Sensor);

	if(sensor->valor_Df == sensor->valor){
		Serial.print("Conmutador --> ");
		if(valor == OFF){
			valor = ON;
			Serial.println("ON");
		}else{
			valor = OFF;
			Serial.println("OFF");
		}
	}

	sensor->Aux = valor;
}

void conmutador_sal(void *Sensor)
{
	conmutador(Sensor);

	//Actualiza Actuadores
	DomoBoard::setActuator(&domoboard.RELE, ((ptsSensor)Sensor)->Aux);
}


void control_rele_pulsador(void *Sensor)
{
	Pulsado_Soltado(Sensor);
	ptsSensor s = (ptsSensor)Sensor;
	DomoBoard::setActuator(&domoboard.RELE, (s->valor != s->valor_Df));
}

void control_triac_pulsador(void *Sensor)
{
	Pulsado_Soltado(Sensor);
	ptsSensor s = (ptsSensor)Sensor;
	DomoBoard::setActuator(&domoboard.TRIAC, (s->valor != s->valor_Df));
}

void control_rele_interruptor(void *Sensor)
{
	Interruptor(Sensor);
	DomoBoard::setActuator(&domoboard.RELE, ((ptsSensor)Sensor)->Aux);
}

void control_triac_interruptor(void *Sensor)
{
	Interruptor(Sensor);
	DomoBoard::setActuator(&domoboard.TRIAC, ((ptsSensor)Sensor)->Aux);
}

void conmutador_triac(void *Sensor)
{
	conmutador(Sensor);
	DomoBoard::setActuator(&domoboard.TRIAC, ((ptsSensor)Sensor)->Aux);
}

void conmutador_total(void *Sensor)
{
	conmutador(Sensor);
	bool estado = ((ptsSensor)Sensor)->Aux;
	DomoBoard::setActuator(&domoboard.RELE, estado);
	DomoBoard::setActuator(&domoboard.TRIAC, estado);
}