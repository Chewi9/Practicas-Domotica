package practicas_eldom.gui.visualizers;

import java.awt.Component;
import java.awt.Color;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import CommTransport.CommTransport;
import Utilidades.modbus.ModBus_Communications;
import eu.hansolo.steelseries.extras.LightBulb;
import modbus.Const_Modbus;
import modbus.ModBusEvent;
import practicas_eldom.config.MB_Registers;
import practicas_eldom.config.MB_Registers.MB_Analog_Input_Register;
import practicas_eldom.config.MB_Registers.MB_Analog_Output_Holding;
import practicas_eldom.config.MB_Registers.MB_Discrete_Input_Contacts;
import practicas_eldom.config.MB_Registers.MB_Discrete_Output_Coils;
import practicas_eldom.config.MB_Registers.TSwitchState;

import javax.swing.JLabel;

import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import eu.hansolo.steelseries.extras.Led;
import javax.swing.border.LineBorder;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import eu.hansolo.steelseries.gauges.DigitalRadial;
import eu.hansolo.steelseries.gauges.DisplaySingle;
import eu.hansolo.steelseries.gauges.Radial2Top;
import eu.hansolo.steelseries.tools.GaugeType;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.LedColor;
import eu.hansolo.steelseries.tools.PointerType;

public class DomoBoardGui extends JPanel implements Visualizer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8619767299083215147L;
	private 		MouseAdapter 		ma_lightBulb;
	private final 	String 				category;
	private final 	boolean				isCategory = true;
	private final	CommTransport 		sn_Transport;
	private final 	int					address;
	private			LightBulb 			lightBulb1;
	private			LightBulb 			lightBulb2;
	private			Led 				ledBtn1;
	private			Led 				ledBtn2;
	private			Led 				ledBtnOpt;
	private         JCheckBox 			cbActPIR;
	private			Led 				ledPIR;
	private			Radial2Top 			r2T_Pot1;
	private			Radial2Top 			r2T_Pot2;
	
	private 		DisplaySingle		displayTemp; // Temperatura
	
	private 		DigitalRadial		fotores; // Fotoresistencia
	private 		DigitalRadial		fototrans; // Fototransistor
	
	private 		JCheckBox			actFotores; // Activar fotorresistencia
	private 		JTextField			fotoresHi; // Umbral alto
	private 		JTextField			fotoresLo; // Umbral bajo
	
	private			JCheckBox			actFototrans; // Activar fototransistor
	private 		JTextField			fototransNivel; // nivel fototransistor
	
	
	//Banco de registros para mantener sincronizada la comunicación Modbus 
	private  		int 				Cregs[];
	private  		int 				Dregs[];
	private         int 				Aregs[];
	private         int 				Iregs[];
	
	private			boolean     		stActualize = true;
	private 		JTextField 			tiempoPIR;

	public DomoBoardGui(String category, int address, CommTransport sn_Transport) {
		
		super();
		
		this.category 		= category;
		this.address		= address;
		this.sn_Transport 	= sn_Transport;
		
		this.setLayout(null);
		
		//Crea Banco de registros para mantener sincronizada la comunicación Modbus 
		Cregs = new int [MB_Registers.MB_Discrete_Output_Coils.MB_O_COILS.getReg()];
		Dregs = new int [MB_Registers.MB_Discrete_Input_Contacts.MB_I_REGS.getReg()];
		Aregs = new int [MB_Registers.MB_Analog_Output_Holding.MB_AREGS.getReg()];
		Iregs = new int [MB_Registers.MB_Analog_Input_Register.MB_I_AREGS.getReg()];
		
		ma_lightBulb = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ONOFF_Bulb(((LightBulb)e.getComponent()));
			}
		};
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel.setBounds(10, 22, 172, 100);
		add(panel);
		
		JLabel label = new JLabel("Estado Pulsadores");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.RED);
		label.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label.setBounds(0, 75, 166, 14);
		panel.add(label);
		
		ledBtn1 = new Led();
		ledBtn1.setBounds(11, 11, 36, 36);
		panel.add(ledBtn1);
		
		ledBtn2 = new Led();
		ledBtn2.setBounds(67, 11, 36, 36);
		panel.add(ledBtn2);
		
		ledBtnOpt = new Led();
		ledBtnOpt.setBounds(120, 11, 36, 36);
		panel.add(ledBtnOpt);
		
		JLabel label_1 = new JLabel("BTN 1");
		label_1.setForeground(Color.BLUE);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(11, 43, 36, 14);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("BTN 2");
		label_2.setForeground(Color.BLUE);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(67, 43, 36, 14);
		panel.add(label_2);
		
		JLabel label_3 = new JLabel("BTN_OPT");
		label_3.setForeground(Color.BLUE);
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(110, 43, 56, 14);
		panel.add(label_3);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 255), 2));
		panel_1.setBounds(199, 11, 215, 123);
		add(panel_1);
		panel_1.setLayout(null);
		
		lightBulb1 = new LightBulb();
		lightBulb1.setOn(true);
		lightBulb1.setGlowColor(Color.RED);
		lightBulb1.setBounds(10, 11, 78, 78);
		panel_1.add(lightBulb1);
		lightBulb1.addMouseListener(ma_lightBulb);
		
		JLabel lblNewLabel = new JLabel("REL\u00C9");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNewLabel.setBounds(20, 92, 76, 24);
		panel_1.add(lblNewLabel);
		
		lightBulb2 = new LightBulb();
		lightBulb2.setOn(true);
		lightBulb2.setGlowColor(Color.YELLOW);
		lightBulb2.setBounds(111, 11, 78, 78);
		panel_1.add(lightBulb2);
		lightBulb2.addMouseListener(ma_lightBulb);
		
		JLabel lblRel = new JLabel("TRIAC");
		lblRel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblRel.setBounds(119, 92, 70, 24);
		panel_1.add(lblRel);
		
		JPanel panel_1_1 = new JPanel();
		panel_1_1.setLayout(null);
		panel_1_1.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 255, 0), Color.BLUE, Color.MAGENTA));
		panel_1_1.setBounds(10, 145, 248, 100);
		add(panel_1_1);
		
		ledPIR = new Led();
		ledPIR.setBounds(0, 2, 95, 95);
		panel_1_1.add(ledPIR);
		
		JLabel label_4 = new JLabel("PIR");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setForeground(Color.RED);
		label_4.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		label_4.setBounds(0, 73, 248, 24);
		panel_1_1.add(label_4);
		
		cbActPIR = new JCheckBox("Activar PIR");
		cbActPIR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TSwitchState vSel;
				
				if(((AbstractButton) arg0.getSource()).getModel().isSelected()) vSel = TSwitchState.ON;
				else vSel = TSwitchState.OFF;
				
				buildModBus(address, Const_Modbus.WRITE_COIL, 
						MB_Registers.MB_Discrete_Output_Coils.MB_ACTPIR.getReg(), TSwitchState.ToNumber(vSel), Cregs);
			}
		});
		cbActPIR.setSelected(true);
		cbActPIR.setBounds(101, 12, 87, 23);
		panel_1_1.add(cbActPIR);
		
		tiempoPIR = new JTextField();
		tiempoPIR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					int newval = Integer.parseInt(tiempoPIR.getText());
				
					//int newval = Integer.getInteger(tiempoPIR.getText(), 0);  
				
					if(newval != 0)
						ModBus_Communications.writeSingleRegister(address, MB_Registers.MB_Analog_Output_Holding.MB_TMP_PIR.getReg(),
								newval, sn_Transport);	
					
				} catch(NumberFormatException ex){
					
				}
								
			}
		});
		
		tiempoPIR.setColumns(10);
		tiempoPIR.setBounds(105, 42, 37, 20);
		panel_1_1.add(tiempoPIR);
		
		JLabel label_5 = new JLabel("Tiempo (Segs.)");
		label_5.setBounds(152, 45, 85, 14);
		panel_1_1.add(label_5);
		
		r2T_Pot1 = new Radial2Top();
		r2T_Pot1.setUserLedVisible(true);
		r2T_Pot1.setUnitString("%");
		r2T_Pot1.setTrackVisible(true);
		r2T_Pot1.setTitle("Pot. 1");
		r2T_Pot1.setPointerType(PointerType.TYPE5);
		r2T_Pot1.setLedColor(LedColor.GREEN);
		r2T_Pot1.setLcdUnitStringVisible(true);
		r2T_Pot1.setLcdScientificFormat(true);
		r2T_Pot1.setLcdInfoString("Test");
		r2T_Pot1.setLcdColor(LcdColor.BLUE_LCD);
		r2T_Pot1.setLcdBackgroundVisible(false);
		r2T_Pot1.setGaugeType(GaugeType.TYPE5);
		r2T_Pot1.setBounds(10, 256, 193, 193);
		add(r2T_Pot1);
		
		r2T_Pot2 = new Radial2Top();
		r2T_Pot2.setUserLedVisible(true);
		r2T_Pot2.setUnitString("%");
		r2T_Pot2.setTrackVisible(true);
		r2T_Pot2.setTitle("Pot. 2");
		r2T_Pot2.setPointerType(PointerType.TYPE5);
		r2T_Pot2.setLedColor(LedColor.GREEN);
		r2T_Pot2.setLcdUnitStringVisible(true);
		r2T_Pot2.setLcdScientificFormat(true);
		r2T_Pot2.setLcdInfoString("Test");
		r2T_Pot2.setLcdColor(LcdColor.BLUE_LCD);
		r2T_Pot2.setLcdBackgroundVisible(false);
		r2T_Pot2.setGaugeType(GaugeType.TYPE5);
		r2T_Pot2.setBounds(223, 256, 193, 193);
		add(r2T_Pot2);
		
		
		
		
		JPanel panelTemp = new JPanel();
		panelTemp.setLayout(null);
		panelTemp.setBorder(new LineBorder(new Color(255, 100, 0), 2));
		panelTemp.setBounds(430, 11, 200, 120);
		add(panelTemp);
		
		JLabel lblTemp = new JLabel("Temperatura");
		lblTemp.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblTemp.setBounds(0,0,200,20);
		panelTemp.add(lblTemp);
		
		displayTemp = new DisplaySingle();
		displayTemp.setLcdColor(LcdColor.BLACK_LCD);
		displayTemp.setBounds(10,25,180,80);
		displayTemp.setLcdDecimals(1);
		panelTemp.add(displayTemp);
		
		
		
		
		
		
		JPanel panelFotores = new JPanel();
		panelFotores.setLayout(null);
		panelFotores.setBorder(new LineBorder(new Color(255, 100, 0), 2));
		panelFotores.setBounds(430, 145, 380, 250);
		add(panelFotores);
		
		JLabel lblFotores = new JLabel("Fotorresistencia");
		lblFotores.setFont(new Font("Times New Roman", Font.BOLD, 13));
		lblFotores.setBounds(0, 3, 380, 18);
		panelFotores.add(lblFotores);
		
		
		fotores = new DigitalRadial();
		fotores.setBounds(5, 25, 180, 180);
		panelFotores.add(fotores);
		
		
		actFotores = new JCheckBox("Activar SRC");
		actFotores.setSelected(true);
		actFotores.setBounds(200, 30, 120, 23);
		actFotores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(actFotores.isSelected()) {
					enviarUmbralFotores();
				} else {
					ModBus_Communications.writeSingleRegister(address, MB_Registers.MB_Analog_Output_Holding.MB_FOTORES_HI.getReg(), 0, sn_Transport);
					ModBus_Communications.writeSingleRegister(address, MB_Registers.MB_Analog_Output_Holding.MB_FOTORES_LO.getReg(), 0, sn_Transport);
				}
			}
		});
		panelFotores.add(actFotores);
		
		
		
		JLabel lblFotoresHi = new JLabel("High");
		lblFotoresHi.setBounds(200, 65, 70, 20);
		panelFotores.add(lblFotoresHi);
		
		fotoresHi = new JTextField("500");
		fotoresHi.setBounds(275, 65, 60, 20);
		fotoresHi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarUmbralFotores();
			}
		});
		panelFotores.add(fotoresHi);
		
		
		
		JLabel lblFotoresLo = new JLabel("Low");
		lblFotoresLo.setBounds(200, 95, 70, 20);
		panelFotores.add(lblFotoresLo);

		fotoresLo = new JTextField("400");
		fotoresLo.setBounds(275, 95, 60, 20);
		fotoresLo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarUmbralFotores();
			}
		});
		panelFotores.add(fotoresLo);
		
		
		
		
		
		
		JPanel panelFotoTr = new JPanel();
		panelFotoTr.setLayout(null);
		panelFotoTr.setBorder(new LineBorder(new Color(255, 100, 0), 2));
		panelFotoTr.setBounds(430, 410, 380, 250);
		add(panelFotoTr);
		
		JLabel lblFotoTr = new JLabel("Fototransistor");
		lblFotoTr.setFont(new Font("Times New Roman", Font.BOLD, 13));
		lblFotoTr.setBounds(0, 3, 380, 18);
		panelFotoTr.add(lblFotoTr);
		
		fototrans = new DigitalRadial();
		fototrans.setBounds(5, 25, 180, 180);
		panelFotoTr.add(fototrans);
		
		
		actFototrans = new JCheckBox("Activar deteccion");
		actFototrans.setSelected(true);
		actFototrans.setBounds(200, 30, 150, 23);
		actFototrans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(actFototrans.isSelected()) {
					enviarNivelFototrans();
				} else {
					ModBus_Communications.writeSingleRegister(address, MB_Registers.MB_Analog_Output_Holding.MB_FOTOTRans.getReg(), 0, sn_Transport);
				}
			}
		});
		panelFotoTr.add(actFototrans);
		
		// Nivel deteccion
		JLabel lblFototransNivel = new JLabel("Nivel deteccion");
		lblFototransNivel.setBounds(200, 65, 100, 20);
		panelFotoTr.add(lblFototransNivel);
		
		
		fototransNivel = new JTextField("40");
		fototransNivel.setBounds(305, 65, 50, 20);
		fototransNivel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarNivelFototrans();
			}
		});
		panelFotoTr.add(fototransNivel);
		
		
		if(sn_Transport.isConnected())
			leerConfiguracionInicial();
	}
	
	private void enviarUmbralFotores() {
		if(!actFotores.isSelected()) return;
		try {
			int hi = Integer.parseInt(fotoresHi.getText());
			int lo = Integer.parseInt(fotoresLo.getText());
			
			ModBus_Communications.writeSingleRegister(address, MB_Registers.MB_Analog_Output_Holding.MB_FOTORES_HI.getReg(), hi, sn_Transport);
			ModBus_Communications.writeSingleRegister(address, MB_Registers.MB_Analog_Output_Holding.MB_FOTORES_LO.getReg(), lo, sn_Transport);
		} catch(NumberFormatException ex) {
			
		}
	}
	
	private void enviarNivelFototrans() {
		if(!actFototrans.isSelected()) return;
		try {
			int nivel = Integer.parseInt(fototransNivel.getText());
			ModBus_Communications.writeSingleRegister(address, MB_Registers.MB_Analog_Output_Holding.MB_FOTOTRans.getReg(), nivel, sn_Transport);
		} catch(NumberFormatException ex) {
			
		}
	}
	
	
	private void leerConfiguracionInicial(){
		//******************************************
		//Leer elementos de configuración Analógico
		//******************************************
		
		ModBus_Communications.readMultipleRegisters(address, MB_Registers.MB_Analog_Output_Holding.MB_TMP_PIR.getReg(), 
				1, sn_Transport, this::UpdateElements , Aregs);

		
		//******************************************
		//Leer elementos de configuración Digitales
		//******************************************
		buildModBus(address, Const_Modbus.READ_COILS, MB_Registers.MB_Discrete_Output_Coils.MB_ACTPIR.getReg(), 1, Cregs);
		
		enviarUmbralFotores();
		enviarNivelFototrans();
		
	}
	
	private void ONOFF_Bulb(LightBulb lightBulb){
		
		int vBulb;
		int vReg;
		
		lightBulb.setOn(!lightBulb.isOn());
		
		if(lightBulb.isOn()) vBulb = TSwitchState.ToNumber(TSwitchState.ON);
		else vBulb = TSwitchState.ToNumber(TSwitchState.OFF);
		
		if(lightBulb == lightBulb1) vReg = MB_Registers.MB_Discrete_Output_Coils.MB_RELE.getReg();
		else vReg = MB_Registers.MB_Discrete_Output_Coils.MB_TRIAC.getReg();
		
		
		buildModBus(address, Const_Modbus.WRITE_COIL, vReg, vBulb, Cregs);
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getTitle() {
		return "Address : "+address;
	}

	@Override
	public Component getPanel() {
		return this;
	}

	@Override
	public boolean isCategory() {
		return isCategory;
	}

	@Override
	public void Actualize() {
		
		// Actualizar dispositivos modbus
		
		if(stActualize) {	
			
			//Read OutputCoils
			buildModBus(1, Const_Modbus.READ_COILS, MB_Registers.MB_Discrete_Output_Coils.MB_RELE.getReg(), MB_Registers.MB_Discrete_Output_Coils.MB_O_COILS.getReg(), Cregs);
			
			//Read Discrete Inputs
			buildModBus(1, Const_Modbus.READ_INPUT_DISCRETES, MB_Registers.MB_Discrete_Input_Contacts.MB_BTN1.getReg(),MB_Registers.MB_Discrete_Input_Contacts.MB_I_REGS.getReg(), Dregs);
			
			//Read Input Register (Registros analógicos de entrada)
			buildModBus(1, Const_Modbus.READ_INPUT_REGISTERS, MB_Registers.MB_Analog_Input_Register.MB_POT1.getReg(),MB_Registers.MB_Analog_Input_Register.MB_I_AREGS.getReg(), Iregs);
			
		}
	}
	
	public void buildModBus(int address, int func, int iReg, int nReg, int[] bReg) {
		String[] args = {String.valueOf(address), String.valueOf(func), 
				String.valueOf(iReg), String.valueOf(nReg)};

		ModBus_Communications.InitModbusComunication(args, sn_Transport, this::UpdateElements, bReg);
		
	}
	
	public void UpdateElements(final ModBusEvent e){

		int addr = Integer.parseInt(e.get_Args()[2]);
		int nReg = Integer.parseInt(e.get_Args()[3]);
					
		switch(Integer.parseInt(e.get_Args()[1])){
		case Const_Modbus.READ_MULTIPLE_REGISTERS:
			//Cada registro esta compruesto de dos bytes "little endian"
			for(int i = addr;i<(addr+nReg); i++){
				switch(MB_Analog_Output_Holding.values()[i]){
				case MB_TMP_PIR:
					//tiempoPIR.setText(Integer.toString(((e.getRegs()[i+1]&0xFF)<<8)|((e.getRegs()[i]&0xFF))));
					tiempoPIR.setText(Integer.toString(e.getRegs()[i-addr]));
					break;
				default:
					break;
				}
			}
			break;
		
		case Const_Modbus.READ_COILS:
			
			for(int i = addr;i<(addr+nReg); i++){
				switch(MB_Discrete_Output_Coils.values()[i]){
				case MB_RELE:							
					lightBulb1.setOn((e.getRegs()[i] == 1));
					break;
							
				case MB_TRIAC:							
					lightBulb2.setOn((e.getRegs()[i] == 1));
					break;
					
				case MB_ACTPIR:
					cbActPIR.setSelected((e.getRegs()[i] == 1));
					break;	
					
				default:
					break;
				
				}
			}
			break;
						
		case Const_Modbus.READ_INPUT_DISCRETES:	
			
			MB_Discrete_Input_Contacts mbDIC; //= MB_Discrete_Input_Contacts.values()[addr];
			
			for(int i = addr;i<(addr+nReg); i++){
				mbDIC = MB_Discrete_Input_Contacts.values()[i];
				switch(mbDIC){
				case MB_BTN1:
					ledBtn1.setLedOn((e.getRegs()[i] != mbDIC.getDefaultValue()));
					break;
								
				case MB_BTN2:
					ledBtn2.setLedOn((e.getRegs()[i] != mbDIC.getDefaultValue()));
					break;
							
				case MB_OPT:
					ledBtnOpt.setLedOn((e.getRegs()[i] != mbDIC.getDefaultValue()));
					break;
					
				case MB_PIR:
					ledPIR.setLedOn((e.getRegs()[i] == 1));
					break;	
					
				default:
					break;
				}
			}
			break;
			
		case Const_Modbus.READ_INPUT_REGISTERS:
			
			MB_Analog_Input_Register mbAIR; //= MB_Discrete_Input_Contacts.values()[addr];
			
			for(int i = addr;i<(addr+nReg); i++){
				mbAIR = MB_Analog_Input_Register.values()[i];
				switch(mbAIR){
				case MB_POT1:
					r2T_Pot1.setValue(100 - ((e.getRegs()[i]*100)/1024));
					break;
					
				case MB_POT2:
					r2T_Pot2.setValue((e.getRegs()[i]*100)/1024);
					break;
				
				case MB_FOTORES:
					fotores.setValue(e.getRegs()[i]);
					break;
					
				case MB_FOTOTR:
					fototrans.setValue(e.getRegs()[i]);
					break;
				
				case MB_TEMP:
					displayTemp.setLcdValueAnimated(e.getRegs()[i]);
					break;
					
				default:
					break;
				}
			}
			break;
		}				
	}
	
	@Override
	public void setActualize(boolean st) {
		stActualize = st;		
	}

	@Override
	public boolean getActualize() {
		
		return stActualize;
	}

	@Override
	public void vlog(String message) {
		// TODO Auto-generated method stub
		
	}
}
