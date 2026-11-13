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
		if (args.length > 1) {
			System.err.println("Uso: TemperatureSensor [área]");
			return;
		}
		final Areas area = (args.length == 1) ? Areas.valueOf(args[0]) : Areas.BedRoom;

		SensorControl slider = new SensorControl("🌡", "Temperatura - " + area, "°C",
				0, 40, 0.5, 20,
				valor -> emitir(new TemperatureSensorEvent(area, (float) valor)));

		JPanel painel = new JPanel(new BorderLayout());
		painel.add(slider, BorderLayout.CENTER);

		JFrame janela = new JFrame("Sensor de temperatura - " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		janela.setContentPane(painel);
		janela.pack();
		janela.setLocationRelativeTo(null);
		janela.setVisible(true);

		emitir(new TemperatureSensorEvent(area, (float) slider.getValor()));
	}

	private static void emitir(Event evento) {
		System.out.println("[sensor] " + evento);
	}
}
