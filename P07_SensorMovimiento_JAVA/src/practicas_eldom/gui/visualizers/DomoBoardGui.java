package practicas_eldom.gui.visualizers;

import java.awt.Component;
import java.awt.Color;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import CommTransport.CommTransport;
import Utilidades.modbus.ModBus_Communications;
import eu.hansolo.steelseries.extras.LightBulb;
import eu.hansolo.steelseries.tools.LedColor;
import modbus.Const_Modbus;
import modbus.ModBusEvent;
import practicas_eldom.config.MB_Registers;
import practicas_eldom.config.MB_Registers.MB_Discrete_Input_Contacts;
import practicas_eldom.config.MB_Registers.MB_Discrete_Output_Coils;
import practicas_eldom.config.MB_Registers.TSwitchState;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import eu.hansolo.steelseries.extras.Led;
import javax.swing.border.LineBorder;

import eu.hansolo.steelseries.extras.*;

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
	
	private 		Led					ledPIR;
	private 		JToggleButton		btnPirActivo;
	private 		JTextField			txtPirTiempo;
	private 		JButton				btnPirTiempo;
	
	
	//Banco de registros para mantener sincronizada la comunicaci�n Modbus 
	private  		int 				Cregs[];
	private  		int 				Dregs[];
	private 		int 				Aregs[];
	
	private			 boolean     		stActualize = true;

	public DomoBoardGui(String category, int address, CommTransport sn_Transport) {
		
		super();
		
		this.category 		= category;
		this.address		= address;
		this.sn_Transport 	= sn_Transport;
		
		this.setLayout(null);
		
		//Crea Banco de registros para mantener sincronizada la comunicaci�n Modbus 
		Cregs = new int [MB_Registers.MB_Discrete_Output_Coils.MB_O_COILS.getReg()];
		Dregs = new int [MB_Registers.MB_Discrete_Input_Contacts.MB_I_REGS.getReg()];
		Aregs = new int [MB_Registers.MB_Analog_Output_Holding.MB_AREGS.getReg()];
		
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
		
		// PIR
		JPanel panelPIR = new JPanel();
		panelPIR.setBorder(new LineBorder(new Color(255, 100, 0), 2));
		panelPIR.setBounds(10,145,404,110);
		panelPIR.setLayout(null);
		add(panelPIR);
		
		JLabel lblPirTitulo = new JLabel("Sensor PIR");
		lblPirTitulo.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblPirTitulo.setBounds(10, 5, 120, 20);
		panelPIR.add(lblPirTitulo);
		
		// Led que se enciende cuando PIR detecta movimiento
		ledPIR = new Led();
		ledPIR.setLedColor(LedColor.RED_LED);
		ledPIR.setBounds(10,30,36,36);
		panelPIR.add(ledPIR);
		
		JLabel lblPirEstado = new JLabel("Movimiento");
		lblPirEstado.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblPirEstado.setBounds(5, 68, 80, 14);
		panelPIR.add(lblPirEstado);
		
		
		// Botón ON OFF del PIR
		btnPirActivo = new JToggleButton("PIR ON");
		btnPirActivo.setSelected(true);
		btnPirActivo.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnPirActivo.setBounds(70,28,90,40);
		panelPIR.add(btnPirActivo);
		
		btnPirActivo.addActionListener(e2 ->{
			boolean activo = btnPirActivo.isSelected();
			btnPirActivo.setText(activo ? "PIR ON" : "PIR OFF");
			btnPirActivo.setBackground(activo ? new Color(0,180,0) : Color.RED);
			int valor = activo ? 1 : 0;
			
			buildModBus(address, modbus.Const_Modbus.WRITE_SINGLE_REGISTER,
			        MB_Registers.MB_Analog_Output_Holding.MB_PIR_ACTIVO.getReg(), valor, Aregs);
			
		});
		
		// Programar el tiempo
		JLabel lblTiempo = new JLabel("Tiempo: ");
		lblTiempo.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		lblTiempo.setBounds(180,10,90,20);
		panelPIR.add(lblTiempo);
		
		txtPirTiempo = new JTextField("5000");
		txtPirTiempo.setBounds(180,30,90,25);
		panelPIR.add(txtPirTiempo);
		
		btnPirTiempo = new JButton("enviar");
		btnPirTiempo.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		btnPirTiempo.setBounds(280,30,80,25);
		panelPIR.add(btnPirTiempo);
		
		btnPirTiempo.addActionListener(e2 -> {
			try {
				int tiempo = Integer.parseInt(txtPirTiempo.getText().trim());
				
				buildModBus(address, modbus.Const_Modbus.WRITE_SINGLE_REGISTER,
			            MB_Registers.MB_Analog_Output_Holding.MB_PIR_TIEMPO.getReg(), tiempo, Aregs);
			} catch (NumberFormatException ex) {
				txtPirTiempo.setText("5000");
			}
		});
		
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
			
			// Añadido
			// Read Holding Registers
			buildModBus(1, Const_Modbus.READ_MULTIPLE_REGISTERS, MB_Registers.MB_Analog_Output_Holding.MB_PRACT.getReg(), MB_Registers.MB_Analog_Output_Holding.MB_AREGS.getReg(), Aregs);
			
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
		case Const_Modbus.READ_COILS:
			
			for(int i = addr;i<(addr+nReg); i++){
				switch(MB_Discrete_Output_Coils.values()[i]){
				case MB_RELE:							
					lightBulb1.setOn((e.getRegs()[i] == 1));
					break;
							
				case MB_TRIAC:							
					lightBulb2.setOn((e.getRegs()[i] == 1));
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
					ledPIR.setLedOn(e.getRegs()[i] == 1);
					break;
					
				default:
					break;
				}
			}
			break;
			
		case modbus.Const_Modbus.READ_MULTIPLE_REGISTERS:
			for(int i = addr; i< (addr+nReg); i++) {
				switch(MB_Registers.MB_Analog_Output_Holding.values()[i]) {
				case MB_PIR_ACTIVO:
					boolean pirActivo = (e.getRegs()[i] != 0);
					btnPirActivo.setSelected(pirActivo);
					btnPirActivo.setText(pirActivo ? "PIR ON" : "PIR OFF");
					btnPirActivo.setBackground(pirActivo ? new Color(0,180,0) : Color.RED);
					break;
					
				case MB_PIR_TIEMPO:
					if(!txtPirTiempo.hasFocus()){
						txtPirTiempo.setText(String.valueOf(e.getRegs()[i]));
					}
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
