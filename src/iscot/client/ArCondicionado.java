package iscot.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.ACIcon;
import iscot.client.gui.LightIcon;
import iscot.client.gui.VentilatorIcon;
import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.RegistrationEvent;
import iscot.event.highlevelorder.AreaLightingOrderEvent;
import iscot.event.highlevelorder.AreaTemperatureOrderEvent;
import iscot.event.highlevelorder.AreaVentilationOrderEvent;
import iscot.event.sensor.LightSensorEvent;

public class ArCondicionado {

	/**
	 * 
	 * @param args server address and port
	 * @param args roomName: string
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if(args.length!=3) {
			System.err.println("Invalid arguments!");
		}
		else {
			Socket socket=new Socket(args[0],Integer.parseInt(args[1]));
			ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream()); // useless, no outgoing messages
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			// register with the server
			Areas area=Areas.valueOf(args[2]);
			out.writeObject(new RegistrationEvent(area, Type.ACTUATOR));

			JFrame janela = new JFrame("Ventilador em "+area);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



			// Criar os componentes gráficos
			JPanel painel=new JPanel();
			painel.setLayout(new BorderLayout());
			ACIcon arCond = new ACIcon();
			painel.add(arCond,BorderLayout.CENTER);
			janela.setContentPane(painel);
			janela.setLocation(600, 400);
			janela.setMinimumSize(janela.getSize());
			janela.pack();
			janela.setVisible(true);

			while(true) {
				Event message=(Event) in.readObject();
				if( message instanceof AreaTemperatureOrderEvent) {
					AreaTemperatureOrderEvent areaTemperatureOrderMessage=(AreaTemperatureOrderEvent)message;
					if(areaTemperatureOrderMessage.area == area) {
						arCond.setModo(areaTemperatureOrderMessage.mode);
						if(areaTemperatureOrderMessage.mode==AirConditioningMode.Ventilação || areaTemperatureOrderMessage.mode==AirConditioningMode.Desligado) 
							arCond.setTemperatura(-1);
						else if(areaTemperatureOrderMessage.mode==AirConditioningMode.Aquecimento || areaTemperatureOrderMessage.mode==AirConditioningMode.Frio) 
							arCond.setTemperatura(areaTemperatureOrderMessage.temperature);

					}
				}
				else if( message instanceof AreaVentilationOrderEvent) {
					AreaVentilationOrderEvent areaVentilationOrderMessage=(AreaVentilationOrderEvent)message;
					if(areaVentilationOrderMessage.area == area) {
						arCond.setModo(AirConditioningMode.Ventilação);
						arCond.setTemperatura(-1);
						System.err.println("Altering AC  at:"+area+" to;"+areaVentilationOrderMessage.state);
					}
					else
						System.err.println("Message for unexpected area!");
				}
				else
					System.err.println("Unexpected message in AC:"+message);

			}
		}
	}
}
