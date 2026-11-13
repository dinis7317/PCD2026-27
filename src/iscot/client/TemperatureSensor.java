package iscot.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.event.Event;
import iscot.event.sensor.TemperatureSensorEvent;

public class TemperatureSensor {

	public static void main(String[] args) {
		if (args.length != 0 && args.length != 1) {
			System.err.println("Invalid arguments!");
			return;
		}

		final Areas area = (args.length == 1) ? Areas.valueOf(args[0]) : Areas.Quarto;
		System.out.println("Modo local: sensor em " + area + "...");

		JFrame janela = new JFrame("Temperatura em " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


		JPanel painel = new JPanel();
		painel.setLayout(new BorderLayout());
		SensorControl sensorControl = new SensorControl("🌡", "Temperatura - " + area, "°C",
				-10, 50, 0.5, 22.5,
				valor -> emitir(new TemperatureSensorEvent(area, (float) valor)));
		painel.add(sensorControl, BorderLayout.CENTER);
		janela.setContentPane(painel);
		janela.setLocation(600, 400);
		janela.setMinimumSize(janela.getSize());
		janela.pack();
		janela.setVisible(true);

		emitir(new TemperatureSensorEvent(area, (float) sensorControl.getValor()));
	}

	private static void emitir(Event evento) {
		System.out.println("Evento gerado: " + evento);
	}
}