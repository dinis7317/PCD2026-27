package iscot.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import iscot.client.gui.PainelHistorico;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import iscot.client.gui.ACIcon;
import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.RegistrationEvent;
import iscot.event.highlevelorder.AreaTemperatureOrderEvent;
import iscot.event.highlevelorder.AreaVentilationOrderEvent;

public class ArCondicionado {

	private final Areas area;
	private final JFrame janela;
	private final ACIcon arCond;
	private final PainelHistorico painelHistorico;

	/**
	 * Construtor do Atuador de Ar Condicionado.
	 * 
	 * @param area Divisão da casa onde o atuador opera
	 */
	public ArCondicionado(Areas area) {
		this.area = area;

		janela = new JFrame("Ar Condicionado em " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel painel = new JPanel(new BorderLayout(5, 5));
		painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		arCond = new ACIcon();
		painel.add(arCond, BorderLayout.CENTER);

		// Componente de Histórico de Ações (Issue #1 / Secção 1.3)
		painelHistorico = new PainelHistorico();
		painel.add(painelHistorico, BorderLayout.SOUTH);

		janela.setContentPane(painel);
		janela.setLocation(600, 400);
		janela.pack();
		janela.setVisible(true);
	}

	/**
	 * ISSUE #3: Desacopla a interpretação de eventos da camada de rede.
	 * 
	 * @param message Evento recebido (AreaTemperatureOrderEvent ou AreaVentilationOrderEvent)
	 */
	public void aplicarEvento(Event message) {
		if (message instanceof AreaTemperatureOrderEvent) {
			AreaTemperatureOrderEvent order = (AreaTemperatureOrderEvent) message;
			if (order.area == this.area) {
				arCond.setModo(order.mode);
				if (order.mode == AirConditioningMode.Desligado) {
					painelHistorico.registarAcao("AC desligado");
				} else {
					painelHistorico.registarAcao("AC desligado" + order.mode + " (" + order.temperature + "ºC)");
				}
			}
				
		} else if (message instanceof AreaVentilationOrderEvent) {
			AreaVentilationOrderEvent order = (AreaVentilationOrderEvent) message;
			if (order.area == this.area) {
				arCond.setModo(AirConditioningMode.Ventilação);
				painelHistorico.registarAcao("AC em Ventilação");
			}else {
				arCond.setModo(AirConditioningMode.Desligado);
				painelHistorico.registarAcao("AC desligado");
			}

		}
	}

	/**
	 * Inicia o ciclo de receção de eventos a partir de um servidor via Socket.
	 */
	public void iniciarConexao(String host, int port) throws IOException, ClassNotFoundException {
		Socket socket = new Socket(host, port);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		out.writeObject(new RegistrationEvent(area, Type.ACTUATOR));

		while (true) {
			Event message = (Event) in.readObject();
			aplicarEvento(message);
		}
	}

	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if (args.length != 3) {
			System.out.println("Modo teste/demonstração (Fase 3): a abrir janela no Quarto...");
			ArCondicionado ac = new ArCondicionado(Areas.Quarto);
			Thread.sleep(1000);
			ac.aplicarEvento(new AreaTemperatureOrderEvent(Areas.Quarto, AirConditioningMode.Frio, 21.0f));
		} else {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			Areas area = Areas.valueOf(args[2]);

			ArCondicionado ac = new ArCondicionado(area);
			ac.iniciarConexao(host, port);
		}
	}
}
