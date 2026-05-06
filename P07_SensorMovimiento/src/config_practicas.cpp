/*
 * config_practicas.cpp
 *
 *  Created on: 10/03/2015
 *      Author: jctejero
 * 
 *  Modified on: 17/03/2025
 */

/****************************************************************************/
/***        Include files                                                 ***/
/****************************************************************************/
#include	"config_practicas.h"
#include    "debuglog.h"

/****************************************************************************/
/***                 Functions                                            ***/
/****************************************************************************/
void config_practica1_apt_1(){
	domoboard.Clear_SensorsConfiguration();

	domoboard.BOTON1.SensorEvent = Pulsado_Soltado;

	domoboard.BOTON2.SensorEvent = Pulsado_Soltado;

	domoboard.BTN_OPT.SensorEvent = Pulsado_Soltado;
}

void config_practica1_apt_2(){
	domoboard.Clear_SensorsConfiguration();

	domoboard.BOTON1.SensorEvent = Interruptor;
	domoboard.BOTON1.Aux = OFF;

	domoboard.BOTON2.SensorEvent = Interruptor;
	domoboard.BOTON2.Aux = OFF;

	domoboard.BTN_OPT.SensorEvent = Interruptor;
	domoboard.BTN_OPT.Aux = OFF;
}

void config_practica1_apt_3(){
	domoboard.Clear_SensorsConfiguration();

	domoboard.BOTON1.SensorEvent = conmutador;

	domoboard.BOTON2.SensorEvent = conmutador;

	domoboard.BTN_OPT.SensorEvent = conmutador;
}

void config_practica3_apt_2(){
	domoboard.Clear_SensorsConfiguration();

	domoboard.BOTON1.SensorEvent = conmutador_sal;
	domoboard.BOTON1.managerActuators.push(&(domoboard.RELE));

	domoboard.BOTON2.SensorEvent = conmutador_sal;
	domoboard.BOTON2.managerActuators.push(&(domoboard.RELE));

	domoboard.BTN_OPT.SensorEvent = conmutador_sal;
	domoboard.BTN_OPT.managerActuators.push(&(domoboard.RELE));

	stateConmutador = domoboard.RELE.estado;
}

void config_practica4_apt_2(void){

	domoboard.Clear_SensorsConfiguration();

	domoboard.BOTON1.SensorEvent = Pulsado_Soltado_sal;
	domoboard.BOTON1.managerActuators.push(&(domoboard.RELE));

	domoboard.BOTON2.SensorEvent = Pulsado_Soltado_sal;
	domoboard.BOTON2.managerActuators.push(&(domoboard.TRIAC));

	domoboard.BTN_OPT.SensorEvent = Pulsado_Soltado_sal;
}

void config_practica4_apt_3(void){

	domoboard.Clear_SensorsConfiguration();

	domoboard.BOTON1.managerActuators.clear();
	domoboard.BOTON1.SensorEvent = interruptor_sal;
	domoboard.BOTON1.managerActuators.push(&(domoboard.RELE));


	domoboard.BOTON2.SensorEvent = interruptor_sal;
	domoboard.BOTON2.managerActuators.push(&(domoboard.TRIAC));

	domoboard.BTN_OPT.SensorEvent = interruptor_sal;
}

void config_practica4_apt_4(void){

	domoboard.Clear_SensorsConfiguration();

	domoboard.BOTON1.SensorEvent = conmutador_sal;
	domoboard.BOTON1.managerActuators.push(&(domoboard.TRIAC));

	domoboard.BOTON2.SensorEvent = conmutador_sal;

	domoboard.BOTON2.managerActuators.push(&(domoboard.TRIAC));

	domoboard.BTN_OPT.SensorEvent = interruptor_sal;
	domoboard.BTN_OPT.managerActuators.push(&(domoboard.RELE));
}

void config_practica4_apt_5(void){

	domoboard.Clear_SensorsConfiguration();

	domoboard.BOTON1.SensorEvent = conmutador_sal;
	domoboard.BOTON1.managerActuators.push(&(domoboard.RELE));
	domoboard.BOTON1.managerActuators.push(&(domoboard.TRIAC));

	domoboard.BOTON2.SensorEvent = conmutador_sal;
	domoboard.BOTON2.managerActuators.push(&(domoboard.RELE));
	domoboard.BOTON2.managerActuators.push(&(domoboard.TRIAC));

	domoboard.BTN_OPT.SensorEvent = conmutador_sal;
	domoboard.BTN_OPT.managerActuators.push(&(domoboard.RELE));
	domoboard.BTN_OPT.managerActuators.push(&(domoboard.TRIAC));
}

void config_practica5_apt_4(){
	mbDomoboard.Clear_SensorsConfiguration();

	mbDomoboard.BOTON1.mbSensorEvent = mbInterruptor;
	mbDomoboard.BOTON1.mbActuators.push(&mbDomoboard.RELE);

	mbDomoboard.BOTON2.mbSensorEvent = mbInterruptor;
	mbDomoboard.BOTON2.mbActuators.push(&mbDomoboard.TRIAC);

}


void config_practica7_pir(void){
	mbDomoboard.Clear_SensorsConfiguration();

	mbDomoboard.PIR.mbSensorEvent = mbPIR;
	mbDomoboard.PIR.mbActuators.push(&mbDomoboard.TRIAC);

	Aregs[MB_PIR_ACTIVO] = 1; // Activar detección PIR desde maestro
	Aregs[MB_PIR_TIEMPO] = 5000; // Tiempo activación PIR en ms
	Aregs[MB_PIR_TIEMPO_TIMER] = 0; // millis de la última detección PIR

	// Activamos sensor PIR en domoboard
	mbDomoboard.PIR.Sensor->valor = true;

	// Conmutador 3 botones --> Relé
	mbDomoboard.BOTON1.mbSensorEvent = mbConmutador;
	mbDomoboard.BOTON1.mbActuators.push(&mbDomoboard.RELE);

	mbDomoboard.BOTON2.mbSensorEvent = mbConmutador;
	mbDomoboard.BOTON2.mbActuators.push(&mbDomoboard.RELE);

	mbDomoboard.BTN_OPT.mbSensorEvent = mbConmutador;
	mbDomoboard.BTN_OPT.mbActuators.push(&mbDomoboard.RELE);

}

void gestionarTemporizadorPIR(void){
	// Solo actuar si estamos en la practica 7
	if(Aregs[MB_SELPRACT] != P7_PIR) return;

	// Solo si el TRIAC está encendido por el PIR
	if(mbDomoboard.PIR.Aux == OFF) return;

	unsigned long ahora = millis();
	unsigned long inicio = Aregs[MB_PIR_TIEMPO_TIMER];
	unsigned long tiempoActivacion = Aregs[MB_PIR_TIEMPO];

	if((ahora - inicio) >= tiempoActivacion){
		// Tiempo expirado sin detección, apagamos TRIAC y limpiamos
		mbDomoboard.manager_mbActuators(&(mbDomoboard.PIR.mbActuators), sOFF);
		mbDomoboard.PIR.Aux = OFF;
		DEBUGLNF("PIR: Temporizador expirado");
	}
}


void SelectionConfiguration(uint8_t selConf){
	static uint8_t actConf = 0x00;

	if(selConf == actConf)
		return;

	actConf = selConf;

	switch(selConf){
		case P1_PULSADORES:
			DEBUGLNF("P1 PULSADORES Seleccionado");
			config_practica1_apt_1();
			break;

		case P1_INTERRUPTOR:
			DEBUGLNF("P1 INTERRUPTOR Seleccionado");
			config_practica1_apt_2();
			break;

		case P1_CONMUTADOR:
			DEBUGLNF("P1 CONMUTADOR Seleccionado");
			config_practica1_apt_3();
			break;

		case P3_CONMUTADOR:
			DEBUGLNF("P3 CONMUTADOR Seleccionado");
			config_practica3_apt_2();
			break;

		case P4_PULSADORES:
			Serial.println("P4 PULSADORES Seleccionado ");
			config_practica4_apt_2();
			break;

		case P4_INTERRUPTOR:
			DEBUGLNF("P4 INTERRUPTOR Seleccionado");
			config_practica4_apt_3();
			break;

		case P4_CONMUTADOR_INT:
			DEBUGLNF("P4 CONMUTADOR BTN1, BTN2 e INTERRUPTOR BTN_OPT Seleccionado");
			config_practica4_apt_4();
			break;

		case P4_CONMUTADOR_2SAL:
			DEBUGLNF("P4 CONMUTADOR Salidas RELÉ y TRIAC Seleccionado");
			config_practica4_apt_5();
			break;

		case P5_INTERRUPTOR:
			DEBUGLNF("P5 INTERRUPTOR Seleccionado");
			config_practica5_apt_4();
			break;

		case P6_INTERRUPTOR:
			DEBUGLNF("P6 INTERRUPTOR Seleccionado");
			config_practica5_apt_4();
			break;

		case P7_PIR:
			DEBUGLNF("P7 PIR Seleccionado");
			config_practica7_pir();
			break;	

	}
}
