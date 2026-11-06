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

import iscot.client.gui.LightIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.RegistrationEvent;
import iscot.event.highlevelorder.AreaLightingOrderEvent;
import iscot.event.sensor.LightSensorEvent;

public class Light {

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

			JFrame janela = new JFrame("Luz em "+area);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			
			// Criar os componentes gráficos
			JPanel painel=new JPanel();
			painel.setLayout(new BorderLayout());
			LightIcon luz = new LightIcon();
			painel.add(luz,BorderLayout.CENTER);
			janela.setContentPane(painel);
			janela.setLocation(600, 400);
			janela.setMinimumSize(janela.getSize());
			janela.pack();
			janela.setVisible(true);

			while(true) {
				Event message=(Event) in.readObject();
				if( message instanceof AreaLightingOrderEvent) {
					AreaLightingOrderEvent areaLightingOrderMessage=(AreaLightingOrderEvent)message;
					if(areaLightingOrderMessage.area == area) {
						luz.setAtivo(areaLightingOrderMessage.state);
						System.err.println("Altering light  at:"+area+" to;"+areaLightingOrderMessage.state);
					}
					else
						System.err.println("Message for unexpected area!");
				}
				else
					System.err.println("Unexpected message in Light:"+message);

			}
		}
	}
}
