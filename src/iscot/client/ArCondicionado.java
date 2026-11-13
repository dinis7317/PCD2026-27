package iscot.client;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.ACIcon;
import iscot.client.gui.PainelHistorico;
import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.event.Event;
import iscot.event.highlevelorder.AreaTemperatureOrderEvent;
import iscot.event.highlevelorder.AreaVentilationOrderEvent;

public class ArCondicionado {

	private final Areas area;
	private final JFrame janela;
	private final ACIcon arCond;
	private final PainelHistorico painelHistorico;

	public ArCondicionado(Areas area) {
		this.area = area;

		janela = new JFrame("Ar Condicionado em " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel painel = new JPanel(new BorderLayout(5, 5));
		painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		arCond = new ACIcon();
		painel.add(arCond, BorderLayout.CENTER);

		painelHistorico = new PainelHistorico();
		painel.add(painelHistorico, BorderLayout.SOUTH);

		janela.setContentPane(painel);
		janela.setLocation(600, 400);
		janela.pack();
		janela.setVisible(true);
	}

	public void aplicarEvento(Event message) {
		if (message instanceof AreaTemperatureOrderEvent) {
			AreaTemperatureOrderEvent order = (AreaTemperatureOrderEvent) message;
			if (order.getArea() == this.area) {
				if (order.getModo() == AirConditioningMode.Desligado) {
					arCond.setModo(AirConditioningMode.Desligado);
					painelHistorico.registarAcao("AC desligado");
				} else {
					if (order.getTemperatura() >= 16 && order.getTemperatura() <= 30) {
						arCond.setTemperatura(order.getTemperatura());
					}
					arCond.setModo(order.getModo());
					painelHistorico.registarAcao("AC em " + order.getModo() + " (" + order.getTemperatura() + " ºC)");
				}
			}

		} else if (message instanceof AreaVentilationOrderEvent) {
			AreaVentilationOrderEvent order = (AreaVentilationOrderEvent) message;
			if (order.getArea() == this.area) {
				AirConditioningMode atual = arCond.getModo();
				if (atual == AirConditioningMode.Frio || atual == AirConditioningMode.Aquecimento) {
					System.out.println("Ordem de ventilação ignorada: AC em " + atual);
				} else if (order.isLigado()) {
					arCond.setModo(AirConditioningMode.Ventilação);
					painelHistorico.registarAcao("AC em Ventilação");
				} else if (atual == AirConditioningMode.Ventilação) {
					arCond.setModo(AirConditioningMode.Desligado);
					painelHistorico.registarAcao("AC desligado");
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		if (args.length != 0 && args.length != 1) {
			System.err.println("Invalid arguments!");
			return;
		}
		Areas area = (args.length == 1) ? Areas.valueOf(args[0]) : Areas.BedRoom;
		ArCondicionado ac = new ArCondicionado(area);
		Thread.sleep(1000);
		ac.aplicarEvento(new AreaTemperatureOrderEvent(area, AirConditioningMode.Frio, 21.0f));
	}
}