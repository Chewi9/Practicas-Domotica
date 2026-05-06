/*
 * utils_domoBoard.cpp
 *
 *  Created on: 10/03/2015
 *      Author: jctejero
 * 
 *  modified on: 17/03/2025
 */

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include "utils_domoBoard.h"
#include "debuglog.h"

bool stateConmutador = OFF;

void Interruptor(void *Sensor)
{
	if(((ptsSensor)Sensor)->valor_Df == ((ptsSensor)Sensor)->valor){
		DEBUG(((ptsSensor)Sensor)->name);
		DEBUG(F(" : Interruptor --> "));
		if(((ptsSensor)Sensor)->Aux == OFF){
			((ptsSensor)Sensor)->Aux = ON;
			DEBUGLNF("ON");
		}else{
			((ptsSensor)Sensor)->Aux = OFF;
			DEBUGLNF("OFF");
		}
	}
}

void Pulsado_Soltado(void *Sensor){
	ptsSensor sensor = reinterpret_cast<ptsSensor>(Sensor);

	DEBUG(sensor->name);
	if(sensor->valor_Df == sensor->valor){
		DEBUGLNF(" --> Soltado");
		sensor->Aux = OFF;
	}else{
		DEBUGLNF(" --> Pulsado");
		sensor->Aux = ON;
	}
}

void conmutador(void *Sensor)
{
	//static bool valor = OFF;

	ptsSensor sensor = reinterpret_cast<ptsSensor>(Sensor);

	if(sensor->valor_Df == sensor->valor){
		DEBUG(F("Conmutador --> "));
		if(stateConmutador == OFF){
			stateConmutador = ON;
			DEBUGLN(F("ON"));
		}else{
			stateConmutador = OFF;
			DEBUGLN(F("OFF"));
		}
	}

	sensor->Aux = stateConmutador;
}

void conmutador_sal_P03(void *Sensor)
{
	conmutador(Sensor);
	
	//Actualiza Actuadores
	ptsSensor sensor = reinterpret_cast<ptsSensor>(Sensor);

	//Actualiza Actuadores
	DomoBoard::setActuator(&domoboard.RELE, sensor->Aux);
}

void Pulsado_Soltado_sal(void *Sensor)
{
	Pulsado_Soltado(Sensor);

	//Actualiza Actuadores
	for(uint8_t i = 0; i < ((ptsSensor)Sensor)->managerActuators.count(); i++){
		DomoBoard::setActuator(((ptsSensor)Sensor)->managerActuators.peek(i), ((ptsSensor)Sensor)->Aux);
	}
}

void interruptor_sal(void *Sensor)
{
	Interruptor(Sensor);

	//Actualiza Actuadores
	for(uint8_t i = 0; i < ((ptsSensor)Sensor)->managerActuators.count(); i++){
		DomoBoard::setActuator(((ptsSensor)Sensor)->managerActuators.peek(i), ((ptsSensor)Sensor)->Aux);
	}
}

void conmutador_sal(void *Sensor)
{
	conmutador(Sensor);

	//Actualiza Actuadores
	for(uint8_t i = 0; i < ((ptsSensor)Sensor)->managerActuators.count(); i++){
		DomoBoard::setActuator(((ptsSensor)Sensor)->managerActuators.peek(i), ((ptsSensor)Sensor)->Aux);
	}
}

void mbInterruptor(void *mbSensor)
{
	TpmbSensor sensor = reinterpret_cast<TpmbSensor>(mbSensor);

	if(sensor->Sensor->valor_Df == sensor->Sensor->valor){
		mbDomoboard.manager_mbActuators(&(sensor->mbActuators), TOGGLE);
	}
}

// Conmutador MobBus para 3 botones
void mbConmutador(void *mbSensor){
	// Cambia el mbSensor a TpmbSensor para acceder a sus campos
	TpmbSensor sensor = reinterpret_cast<TpmbSensor>(mbSensor);
	
	// Si las dos variables tienen el mismo valor, se activa el conmutador, si no, no se hace nada
	if(sensor->Sensor->valor_Df == sensor->Sensor->valor){
		mbDomoboard.manager_mbActuators(&(sensor->mbActuators), TOGGLE);
	}
}

void mbPIR(void *mbSensor){
	// Cambia el mbSensor a TpmbSensor para acceder a sus campos
	TpmbSensor sensor = reinterpret_cast<TpmbSensor>(mbSensor);

	// Si el sensor PIR se ha activado, se actualiza el tiempo de activación en el registro correspondiente
	if(sensor->Sensor->valor == HIGH){
		// Cuando detecta movimiento se activa todo indicando que se debe encender
		mbDomoboard.manager_mbActuators(&(sensor->mbActuators), sON);
		sensor->Aux = ON; // Lo enciende
		Aregs[MB_PIR_TIEMPO_TIMER] = (uint16_t)(millis() & 0xFFFF); // Se almacena el tiempo de activación en el registro correspondiente
		// Con el millis cogemos el valor solo de los 16 bits menos significativos, y se actualiza cada vez que se detecta movimiento, para reiniciar el tiempo cuando se detecta movimiento
		// Si el valor almacenado en el registro supera el tiempo de activación configurado para el PIR, se desactiva la salida correspondiente.
		
		// Con millis a int16 lo limitamos a 65 segundos
	}
}


