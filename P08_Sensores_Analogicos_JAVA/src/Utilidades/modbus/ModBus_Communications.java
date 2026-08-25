package Utilidades.modbus;

import CommTransport.CommTransport;
import Utilidades.threadpool.DefaultExecutorSupplier;
import modbus.Const_Modbus;
import modbus.ModBusEvent;
import modbus.Modbus;
import practicas_eldom.config.MB_Registers.TSwitchState;

public class ModBus_Communications {

	public static void writeCoil(int SlaveAddress, int ModBusRegister, TSwitchState coilState,
			CommTransport sn_Transport) {

		String[] args = { Integer.toString(SlaveAddress), String.valueOf(Const_Modbus.WRITE_COIL),
				Integer.toString(ModBusRegister), TSwitchState.ToNumberString(coilState) };

		// Iniciamos Comunicaci�n
		InitModbusComunication(args, sn_Transport, null);
	}
	
	public static void writeSingleRegister(int SlaveAddress, int ModBusRegister, int val, CommTransport sn_Transport) {
		
		String[] args = {Integer.toString(SlaveAddress),String.valueOf(Const_Modbus.WRITE_SINGLE_REGISTER), 
				Integer.toString(ModBusRegister), String.valueOf(val)};
		
		// Iniciamos Comunicaci�n
		InitModbusComunication(args, sn_Transport, null);
	}
	
	public static void readMultipleRegisters(int SlaveAddress, int firstRegister, int numRegisters, CommTransport sn_Transport, IModBusResponse modBusResponse,
			int[]... reg) {
		
		
		String[] args = {Integer.toString(SlaveAddress),String.valueOf(Const_Modbus.READ_MULTIPLE_REGISTERS), 
				Integer.toString(firstRegister), Integer.toString(numRegisters)};
		
		// Iniciamos Comunicaci�n
		InitModbusComunication(args, sn_Transport, modBusResponse, reg);
		
	}
	
	public static void readInputRegisters(int slaveAddress, int firstRegister, int nRegisters, CommTransport sn_Transport, IModBusResponse modbusResponse, int[]...reg) {
		String[] args = {
				Integer.toString(slaveAddress),
				String.valueOf(Const_Modbus.READ_INPUT_REGISTERS), // FC = 0x04
				Integer.toString(firstRegister),
				Integer.toString(nRegisters)
		};
		
		InitModbusComunication(args, sn_Transport, modbusResponse, reg);
	}

	public static void InitModbusComunication(String[] args, CommTransport sCon, IModBusResponse modBusResponse,
			int[]... reg) {

		// Iniciamos Comunicaci�n
				
		DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(new Runnable() {
			
			@Override
			public void run() {
				initCommunications();
			}
			
			public synchronized void initCommunications() {
				Modbus.InitComunication(args, sCon, reg);

				if (modBusResponse != null) {
					ModBusEvent e = new ModBusEvent(reg[0]);
					e.set_Args(args);

					modBusResponse.onModBusResponse(e);
				}
		    }
		});
	}	
}
