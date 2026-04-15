/*
 * Modbus_Domoboard.h
 *
 *  Created on: 29/3/2016
 *      Author: JuanCarlos
 *
 *  Modified on: 16/03/2022
 */

#ifndef DOMOBOARD_MODBUS_DOMOBOARD_H_
#define DOMOBOARD_MODBUS_DOMOBOARD_H_

/****************************************************************************/
/***        Type Definitions                                              ***/
/****************************************************************************/
/* slave registers */

#include "domoBoard.h"
#include "ModbusSlave/ModbusSlave.h"

//Discrete Output Coils
enum {
	MB_RELE,
	MB_TRIAC,
	MB_O_COILS
};

typedef enum{
	MB_OFF,
	MB_ON,
	TOGGLE
}TStateDigitalDev;

typedef struct {
    tpsActuator actuator;
    uint16_t    *mbRegs;
} TmbActuator, *TpmbActuator;

typedef QueueList<TpmbActuator> TmbActuators;

typedef struct {
    ptsSensor    Sensor;
    int          Aux;
    TmbActuators mbActuators;
    TNotifyEvent mbSensorEvent;
} TmbSensor, *TpmbSensor;

class ModbusDomoboard : public DomoBoard {
	private:
		ModbusSlave *mbSlave;
	public:
		TmbSensor		BOTON1;
		TmbSensor		BOTON2;
		TmbSensor 		BTN_OPT;

		TmbActuator 	RELE;
		TmbActuator 	TRIAC;

		ModbusDomoboard();
		virtual ~ModbusDomoboard(){};

		void leerAllSensor(void);
		void leerSensor(TpmbSensor Sensor);

		void setmbActuator(TmbActuator *Actuator, TStateDigitalDev state);
		void manager_mbActuators(TmbActuators *Actuators, TStateDigitalDev state);
		void setModBusSlave(ModbusSlave *mbSlave);
		void clear_Actuators();
};


extern ModbusDomoboard modbusDomoboard;
extern uint16_t Cregs[MB_O_COILS];
#endif /* DOMOBOARD_MODBUS_DOMOBOARD_H_ */
