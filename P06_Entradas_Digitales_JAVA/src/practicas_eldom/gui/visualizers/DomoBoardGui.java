package practicas_eldom.gui.visualizers;

import java.awt.Component;
import java.awt.Color;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import CommTransport.CommTransport;
import Utilidades.modbus.ModBus_Communications;
import eu.hansolo.steelseries.extras.LightBulb;
import eu.hansolo.steelseries.extras.Led;
import eu.hansolo.steelseries.tools.LedColor;
import modbus.Const_Modbus;
import practicas_eldom.config.MB_Registers;
import practicas_eldom.config.MB_Registers.TSwitchState;


import javax.swing.JLabel;

import java.awt.Font;

public class DomoBoardGui extends JPanel implements Visualizer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8619767299083215147L;
	
	private 		LightBulb 			lightBulb1;
	private 		LightBulb 			lightBulb2;
	private 		MouseAdapter 		ma_lightBulb;
	private final 	String 				category;
	private final 	boolean				isCategory = true;
	private final	CommTransport 		sn_Transport;
	private final 	String				address;
	
	private Led btn1;
	private Led btn2;
	private Led btnOpt;
	
	// Array para sincronizar la lectura de botones con el arduino
	// Se guardar las respuestas del Arduino
	private int[] Regs;
	
	private int lastBtn1 = 0;
	private int lastBtn2 = 0;
	private int lastOpt = 0;
	
//	public App_Connection serialConnection;

	public DomoBoardGui(String category, String address, CommTransport sn_Transport) {
		
		super();
		
		this.category 		= category;
		this.address		= address;
		this.sn_Transport 	= sn_Transport;
		
		this.setLayout(null);
		
		// Array para los 3 botones que hay, 1, 2 y OPT
		// Regs[0] es el botón 1
		// Regs[1] es el botón 2
		// Regs[2] es el botón Opt
		Regs = new int[3];
		
		// Boton 1
		btn1 = new Led();
		btn1.setLedColor(LedColor.GREEN);
		btn1.setBounds(20, 30, 60, 60);
		btn1.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				btn1.setLedColor(LedColor.RED);
				btn1.setLedOn(true);
				cambioRele();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				btn1.setLedColor(LedColor.GREEN);
				btn1.setLedOn(false);
			}
		});
		this.add(btn1);
		
		// Boton 2
		btn2 = new Led();
		btn2.setLedColor(LedColor.GREEN);
		btn2.setBounds(110, 30, 60, 60);
		btn2.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				btn2.setLedColor(LedColor.RED);
				btn2.setLedOn(true);
				cambioTriac();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				btn2.setLedColor(LedColor.GREEN);
				btn2.setLedOn(false);
			}
		});
		this.add(btn2);
		
		// Boton OPT
		btnOpt = new Led();
		btnOpt.setLedColor(LedColor.GREEN);
		btnOpt.setBounds(200, 30, 60, 60);
		btnOpt.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				btnOpt.setLedColor(LedColor.RED);
				btnOpt.setLedOn(true);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				btnOpt.setLedColor(LedColor.GREEN);
				btnOpt.setLedOn(false);
			}
		});
		this.add(btnOpt);
		
		
		// Etiquetas
		JLabel LabelBtn1 = new JLabel("Botón 1");
		LabelBtn1.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		LabelBtn1.setBounds(30, 95, 50, 20);
		add(LabelBtn1);
		
		JLabel LabelBtn2 = new JLabel("Botón 2");
		LabelBtn2.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		LabelBtn2.setBounds(120, 95, 50, 20);
		add(LabelBtn2);
		
		JLabel LabelBtnOpt = new JLabel("OPT");
		LabelBtnOpt.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		LabelBtnOpt.setBounds(215, 95, 50, 20);
		add(LabelBtnOpt);		
		
		ma_lightBulb = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				ONOFF_Bulb(((LightBulb)e.getComponent()));
			}
		};
		
		// ETIQUETAS DE LOS LEDS		
		lightBulb1 = new LightBulb();
		lightBulb1.setToolTipText("REL\u00C9");
		lightBulb1.setGlowColor(Color.YELLOW);
		lightBulb1.setBounds(380, 11, 78, 78);
		lightBulb1.addMouseListener(ma_lightBulb);
		this.add(lightBulb1);
		
		lightBulb2 = new LightBulb();
		lightBulb2.setToolTipText("TRIAC");
		lightBulb2.setGlowColor(Color.RED);
		lightBulb2.setBounds(480, 11, 78, 78);
		lightBulb2.addMouseListener(ma_lightBulb);
		this.add(lightBulb2);
		
		JLabel lblNewLabel = new JLabel("RELÉ");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNewLabel.setBounds(395, 92, 63, 24);
		add(lblNewLabel);
		
		JLabel lblRel = new JLabel("TRIAC");
		lblRel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblRel.setBounds(490, 92, 63, 24);
		add(lblRel);
	}
	
	// Cambiamos estado de la bombilla en rele
	private void cambioRele() {
		boolean newState = !lightBulb1.isOn(); 
		lightBulb1.setOn(newState);
		ModBus_Communications.writeCoil(1, MB_Registers.ModBusRegisters.MB_RELE.getReg(), newState ? TSwitchState.ON : TSwitchState.OFF, sn_Transport);
	}
	
	// Cambiamos estado de la bombilla en triac
	private void cambioTriac() {
		boolean newState = !lightBulb2.isOn();
		lightBulb2.setOn(newState);
		ModBus_Communications.writeCoil(1, MB_Registers.ModBusRegisters.MB_TRIAC.getReg(), newState ? TSwitchState.ON : TSwitchState.OFF, sn_Transport);
	}
	
	private void ONOFF_Bulb(LightBulb lightBulb){
		TSwitchState vBulb;
		Integer vReg;
		
		lightBulb.setOn(!lightBulb.isOn());
		
		if(lightBulb.isOn()) vBulb = MB_Registers.TSwitchState.ON;
		else vBulb = MB_Registers.TSwitchState.OFF;
		
		if(lightBulb == lightBulb1) vReg = MB_Registers.ModBusRegisters.MB_RELE.getReg();
		else vReg = MB_Registers.ModBusRegisters.MB_TRIAC.getReg();
		
		ModBus_Communications.writeCoil(1, vReg, vBulb , sn_Transport);  
		
	}
	
	// Actualizar para comprobar el estado de cada uno de los led en todo momento
	@Override
	public void Actualize() {
		String[] args = {address, String.valueOf(Const_Modbus.READ_INPUT_DISCRETES), "0", "3"};
		ModBus_Communications.InitModbusComunication(args, sn_Transport, () -> {
			// Botón 1
			if (Regs[0] != lastBtn1) {
				lastBtn1 = Regs[0];
				if (Regs[0] == 1) {
					btn1.setLedColor(LedColor.RED);
					btn1.setLedOn(true);
					cambioRele();
				} else {
					btn1.setLedColor(LedColor.GREEN);
					btn1.setLedOn(false);
				}
			}
			
			if (Regs[1] != lastBtn2) {
				lastBtn2 = Regs[1];
				if (Regs[1] == 1) {
					btn2.setLedColor(LedColor.RED);
					btn2.setLedOn(true);
					cambioTriac();
				} else {
					btn2.setLedColor(LedColor.GREEN);
					btn2.setLedOn(false);
				}
			}
			
			if (Regs[2] != lastOpt) {
				lastOpt = Regs[2];
				if (Regs[2] == 1) {
					btnOpt.setLedColor(LedColor.RED);
					btnOpt.setLedOn(true);
				} else {
					btnOpt.setLedColor(LedColor.GREEN);
					btnOpt.setLedOn(false);
				}
				
			}
			
		}, Regs);
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
}
