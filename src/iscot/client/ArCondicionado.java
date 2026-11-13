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
	 * @param message Evento recebido (AreaTemperatureOrderEvent ou AreaVentilationOrderEvent)
	 */
	public void aplicarEvento(Event message) {
		if (message instanceof AreaTemperatureOrderEvent) {
			AreaTemperatureOrderEvent order = (AreaTemperatureOrderEvent) message;
			if (order.area == this.area) {
				if (order.mode == AirConditioningMode.Desligado) {
					arCond.setModo(AirConditioningMode.Desligado);
					painelHistorico.registarAcao("AC desligado");
				} else {
					// o ACIcon só aceita temperaturas entre 16 e 30 C
					if (order.temperature >= 16 && order.temperature <= 30) {
						arCond.setTemperatura(order.temperature);
					}
					arCond.setModo(order.mode);
					painelHistorico.registarAcao("AC em " + order.mode + " (" + order.temperature + " ºC)");
				}
			}

		} else if (message instanceof AreaVentilationOrderEvent) {
			AreaVentilationOrderEvent order = (AreaVentilationOrderEvent) message;
			if (order.area == this.area) { // ordens de outras áreas são ignoradas
				AirConditioningMode atual = arCond.getModo();
				if (atual == AirConditioningMode.Frio || atual == AirConditioningMode.Aquecimento) {
					// Enunciado: só ventila se não estiver a arrefecer ou a aquecer
					System.out.println("Ordem de ventilação ignorada: AC em " + atual);
				} else if (order.state) {
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
		Areas area = (args.length == 1) ? Areas.valueOf(args[0]) : Areas.Quarto;
		ArCondicionado ac = new ArCondicionado(area);
		Thread.sleep(1000);
		ac.aplicarEvento(new AreaTemperatureOrderEvent(area, AirConditioningMode.Frio, 21.0f));
	}
}