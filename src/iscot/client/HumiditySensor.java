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
		if (args.length != 0 && args.length != 1) {
			System.err.println("Invalid arguments!");
			return;
		}

		final Areas area = (args.length == 1) ? Areas.valueOf(args[0]) : Areas.WC;
		System.out.println("Modo local: sensor em " + area + "...");

		JFrame janela = new JFrame("Humidade em " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel painel = new JPanel();
		painel.setLayout(new BorderLayout());
		SensorControl sensorControl = new SensorControl("☀", "Humidade - " + area, "%",
				0, 100, 1, 50,
				valor -> emitir(new HumiditySensorEvent(area, (float) valor)));
		painel.add(sensorControl, BorderLayout.CENTER);
		janela.setContentPane(painel);
		janela.setLocation(600, 400);
		janela.setMinimumSize(janela.getSize());
		janela.pack();
		janela.setVisible(true);


		emitir(new HumiditySensorEvent(area, (float) sensorControl.getValor()));
	}


	private static void emitir(Event evento) {
		System.out.println("Evento gerado: " + evento);
	}
}