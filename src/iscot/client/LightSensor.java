package iscot.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.event.Event;
import iscot.event.sensor.LightSensorEvent;

public class LightSensor {

	public static void main(String[] args) {
		if (args.length > 1) {
			System.err.println("Uso: LightSensor [área]");
			return;
		}
		final Areas area = (args.length == 1) ? Areas.valueOf(args[0]) : Areas.LivingRoom;

		SensorControl slider = new SensorControl("💡", "Luz ambiente - " + area, "lux",
				0, 1000, 5, 300,
				valor -> emitir(new LightSensorEvent(area, (float) valor)));

		JPanel painel = new JPanel(new BorderLayout());
		painel.add(slider, BorderLayout.CENTER);

		JFrame janela = new JFrame("Sensor de luz - " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		janela.setContentPane(painel);
		janela.pack();
		janela.setLocationRelativeTo(null);
		janela.setVisible(true);

		emitir(new LightSensorEvent(area, (float) slider.getValor()));
	}

	private static void emitir(Event evento) {
		System.out.println("[sensor] " + evento);
	}
}
