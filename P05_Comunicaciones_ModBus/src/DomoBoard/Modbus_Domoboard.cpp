#include "Modbus_Domoboard.h"

ModbusDomoboard modbusDomoboard;

ModbusDomoboard::ModbusDomoboard() : DomoBoard() {
    RELE.actuator = &(DomoBoard::RELE);
    RELE.mbRegs = &Cregs[MB_RELE];

    TRIAC.actuator = &(DomoBoard::TRIAC);
    TRIAC.mbRegs = &Cregs[MB_TRIAC];

    BOTON1.Sensor = &(DomoBoard::BOTON1);
    BOTON1.Aux = LOW;

    BOTON2.Sensor = &(DomoBoard::BOTON2);
    BOTON2.Aux = LOW;

    BTN_OPT.Sensor = &(DomoBoard::BTN_OPT);
    BTN_OPT.Aux = LOW;
}

void ModbusDomoboard::setmbActuator(TmbActuator *Actuator, TStateDigitalDev val){
    bool valor= (bool)val;
    if(val == TOGGLE){
        valor = !(*Actuator->mbRegs);
    }

    if(*(Actuator->mbRegs) != valor){
        *(Actuator->mbRegs) = valor;
        setActuator(Actuator->actuator, valor);
    }
}

void ModbusDomoboard::leerSensor(TpmbSensor Sensor){
    DomoBoard::leerSensor(Sensor->Sensor);

	if(Sensor->Aux != Sensor->Sensor->valor){
		Sensor->Aux = Sensor->Sensor->valor;
		if(Sensor->mbSensorEvent != NULL){
			Sensor->mbSensorEvent(&(Sensor->Sensor));
		}
	}
}

void ModbusDomoboard::leerAllSensor(void){
    leerSensor(&BOTON1);
    leerSensor(&BOTON2);
    leerSensor(&BTN_OPT);
}

void ModbusDomoboard::clear_Actuators(){
    DomoBoard::clear_managerActuators();

    BOTON1.mbActuators.clear();
    BOTON2.mbActuators.clear();
    BTN_OPT.mbActuators.clear();

    BOTON1.mbSensorEvent = NULL;
    BOTON2.mbSensorEvent = NULL;
    BTN_OPT.mbSensorEvent = NULL;
}

void ModbusDomoboard::manager_mbActuators(TmbActuators *Actuators, TStateDigitalDev state){
    for(int i = 0; i < Actuators->count(); i++){
        setmbActuator(Actuators->peek(i), state);
    }
}

void ModbusDomoboard::setModBusSlave(ModbusSlave *mbSlave){
    this->mbSlave = mbSlave;
}