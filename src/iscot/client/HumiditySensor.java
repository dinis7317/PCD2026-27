package iscot.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.event.Event;
import iscot.event.sensor.HumiditySensorEvent;

public class HumiditySensor {

	public static void main(String[] args) {
		if (args.length > 1) {
			System.err.println("Uso: HumiditySensor [área]");
			return;
		}
		final Areas area = (args.length == 1) ? Areas.valueOf(args[0]) : Areas.WC;

		SensorControl slider = new SensorControl("💧", "Humidade do ar - " + area, "%",
				0, 100, 5, 60,
				valor -> emitir(new HumiditySensorEvent(area, (float) valor)));

		JPanel painel = new JPanel(new BorderLayout());
		painel.add(slider, BorderLayout.CENTER);

		JFrame janela = new JFrame("Sensor de humidade do ar - " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		janela.setContentPane(painel);
		janela.pack();
		janela.setLocationRelativeTo(null);
		janela.setVisible(true);

		emitir(new HumiditySensorEvent(area, (float) slider.getValor()));
	}

	private static void emitir(Event evento) {
		System.out.println("[sensor] " + evento);
	}
}
