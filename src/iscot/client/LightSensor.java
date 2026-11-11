package iscot.client;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.RegistrationEvent;
import iscot.event.sensor.LightSensorEvent;

public class LightSensor {

	/**
	 * Executável do sensor.
	 * Modo rede: 3 argumentos (IP PORTO Área).
	 * Modo teste/demonstração (Fase 3): sem argumentos, ou servidor indisponível.
	 * Em ambos os modos, cada evento gerado é impresso na consola (Issue #2).
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 0 && args.length != 3) {
			System.err.println("Invalid arguments!");
			return;
		}

		final Areas area = (args.length == 3) ? Areas.valueOf(args[2]) : Areas.Sala;
		ObjectOutputStream ligacao = null;

		if (args.length == 3) {
			try {
				Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
				ligacao = new ObjectOutputStream(socket.getOutputStream());
				new ObjectInputStream(socket.getInputStream());
				ligacao.writeObject(new RegistrationEvent(area, Type.SENSOR));
			} catch (IOException e) {
				System.out.println("Servidor indisponível: a correr em modo local.");
				ligacao = null;
			}
		} else {
			System.out.println("Modo teste/demonstração: sensor em " + area + "...");
		}
		final ObjectOutputStream out = ligacao;

		JFrame janela = new JFrame("Luz em " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


		JPanel painel = new JPanel();
		painel.setLayout(new BorderLayout());
		SensorControl sensorControl = new SensorControl("☀", "Luminosidade - " + area, "lux",
				0, 2000, 10, 320,
				valor -> emitir(new LightSensorEvent(area, (float) valor), out));
		painel.add(sensorControl, BorderLayout.CENTER);
		janela.setContentPane(painel);
		janela.setLocation(600, 400);
		janela.setMinimumSize(janela.getSize());
		janela.pack();
		janela.setVisible(true);


		emitir(new LightSensorEvent(area, (float) sensorControl.getValor()), out);
	}


	private static void emitir(Event evento, ObjectOutputStream out) {
		System.out.println("Evento gerado: " + evento);
		if (out != null) {
			try {
				out.writeObject(evento);
			} catch (IOException e) {
				System.err.println("Falha ao enviar o evento: " + e.getMessage());
			}
		}
	}
}