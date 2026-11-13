package iscot.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.event.Event;
import iscot.event.sensor.GroundMoistureSensorEvent;

public class GroundMoistureSensor {

	public static void main(String[] args) {
		if (args.length > 1) {
			System.err.println("Uso: GroundMoistureSensor [área]");
			return;
		}
		final Areas area = (args.length == 1) ? Areas.valueOf(args[0]) : Areas.Garden;

		SensorControl slider = new SensorControl("🌱", "Humidade do solo - " + area, "%",
				0, 100, 5, 40,
				valor -> emitir(new GroundMoistureSensorEvent(area, (float) valor)));

		JPanel painel = new JPanel(new BorderLayout());
		painel.add(slider, BorderLayout.CENTER);

		JFrame janela = new JFrame("Sensor de humidade do solo - " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		janela.setContentPane(painel);
		janela.pack();
		janela.setLocationRelativeTo(null);
		janela.setVisible(true);

		emitir(new GroundMoistureSensorEvent(area, (float) slider.getValor()));
	}

	private static void emitir(Event evento) {
		System.out.println("[sensor] " + evento);
	}
}
