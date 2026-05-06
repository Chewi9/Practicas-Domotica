/*
 * Gest_Modbus.cpp
 *
 *  Created on: 16/05/2014
 *      Author: jctejero
 *
 *  Modified on: 16/03/2025
 */

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include	<Arduino.h>
#include	"Gest_Modbus.h"
#include 	"utils_domoBoard.h"
#include    "config_practicas.h"
#include	"debuglog.h"

/****************************************************************************/
/***        Variables Locales                                             ***/
/****************************************************************************/
/* First step MBS: create an instance */
ModbusSlave mbs;

/****************************************************************************/
/***                 Functions                                            ***/
/****************************************************************************/
void Init_RTU_Modbus()
{
	/* configure modbus communication
	 * 115200 bps, 8E1, two-device network */
	/* Second step MBS: configure */
	/* the Modbus slave configuration parameters */
	const unsigned char 	SLAVE 	= ADDR_SLAVE;		//Address SLAVE
	const long 				BAUD 	= SERIAL_BPS;
	const char 				PARITY 	= SERIAL_PARITY;
	const char 				TXENPIN = 0; //EN_485;

	//Inicialmente configuramos 485 para recibir
	/*
	if(TXENPIN != 0)
		digitalWrite(EN_485, LOW);
	else digitalWrite(EN_485, HIGH);
	*/

	//Para la conexión 485/ModBus usamos
	Serial485 = &Serial;

	//We configure the ModBus Register Banks
	mbs.set_BankCoils(Cregs, MB_O_COILS);
	mbs.set_BankDiscreteInputs(Dregs, MB_I_CONTATCS);
	mbs.set_BankOutputHoldingRegister(Aregs, MB_A_REGS);

	mbs.configure(SLAVE,BAUD,PARITY,TXENPIN);
}

/*
 *
 */

 void writecoil(){

	for (int addrReg = 0; addrReg < MB_O_COILS; addrReg++) {		
		switch (addrReg) {
		case MB_RELE:
			mbDomoboard.setActuator(mbDomoboard.RELE.actuator, Cregs[MB_RELE] != 0x00);
			break;

		case MB_TRIAC:
			mbDomoboard.setActuator(mbDomoboard.TRIAC.actuator, Cregs[MB_TRIAC] != 0x00);
			break;	
		}
	}

}

/*
 *
 */

 void writeholdingregister(){

	for (int addrReg = 0; addrReg < MB_A_REGS; addrReg++) {
		switch (addrReg) {
		case MB_SELPRACT:
			SelectionConfiguration((uint8_t) (Aregs[MB_SELPRACT] & 0xFF));
			break;

		case MB_PIR_ACTIVO:
			// Maestro puede activar o desactivar el sensor PIR
			mbDomoboard.PIR.Sensor->Activo = (Aregs[MB_PIR_ACTIVO] != 0);

			// Si se desactiva apagamos TRIAC y limpiamos
			if(Aregs[MB_PIR_ACTIVO] == 0){
				mbDomoboard.manager_mbActuators(&(mbDomoboard.PIR.mbActuators), sOFF);
				mbDomoboard.PIR.Aux = OFF;
			}
			break;

		case MB_PIR_TIEMPO:
			// Maestro programa tiempo de activación
			// en Aregs[MB_PIR_TIEMPO] en ms
			break;

		}
	}
}
	

void RTU_ModBus()
{
	if(mbs.update()){
		writecoil();

		writeholdingregister();
	}
}

