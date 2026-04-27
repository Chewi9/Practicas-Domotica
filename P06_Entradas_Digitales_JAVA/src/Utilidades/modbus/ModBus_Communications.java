package Utilidades.modbus;

import CommTransport.CommTransport;
import Utilidades.threadpool.DefaultExecutorSupplier;
import modbus.Const_Modbus;
import modbus.Modbus;
import practicas_eldom.config.MB_Registers.TSwitchState;

public class ModBus_Communications {
	
	public static void writeCoil(int SlaveAddress, int ModBusRegister, TSwitchState coilState, CommTransport sn_Transport) {
		String[] args = {Integer.toString(SlaveAddress), String.valueOf(Const_Modbus.WRITE_COIL), 
				Integer.toString(ModBusRegister), 
				TSwitchState.ToNumberString(coilState)};
		
		InitModbusComunication(args, sn_Transport);	
	}
	
	// 2 parámetros
	public static void InitModbusComunication(String[] args, CommTransport sCon) {
		DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(new Runnable() {
			@Override
			public void run() {
				Modbus.InitComunication(args, sCon);
			}
		});	
	}
	
	// 4 parámetros, para leer botones
	public static void InitModbusComunication(String[] args, CommTransport sCon, Runnable callback, int[] bReg) {
		DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(new Runnable() {
			@Override
			public void run() {
				
				// Se rellenan los datos dentro del array bReg
				Modbus.InitComunication(args, sCon, bReg);
				
				// Se llama a la interfaz para avisar
				if (callback != null) {
					callback.run();
				}
				
			}
		});	
	}
}