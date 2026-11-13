package iscot.client;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.IrrigationIcon;
import iscot.client.gui.PainelHistorico;
import iscot.enums.Areas;
import iscot.event.Event;
import iscot.event.highlevelorder.AreaIrrigationOrderEvent;

public class Irrigator {

	private final Areas area;
	private final JFrame janela;
	private final IrrigationIcon irrigationIcon;
	private final PainelHistorico historico;

	/**
	 * Construtor do Atuador de Rega (Irrigador).
	 * 
	 * @param area Divisão/área onde o atuador opera (normalmente Jardim)
	 */
	public Irrigator(Areas area) {
		this.area = area;

		janela = new JFrame("Irrigador em " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel painel = new JPanel(new BorderLayout(5, 5));
		painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		irrigationIcon = new IrrigationIcon();
		painel.add(irrigationIcon, BorderLayout.CENTER);

		// componente historico
		historico = new PainelHistorico();
		painel.add(historico, BorderLayout.SOUTH);

		janela.setContentPane(painel);
		janela.setLocation(600, 400);
		janela.pack();
		janela.setVisible(true);
	}

	/**
	 * @param message Evento recebido a ser interpretado pelo atuador
	 */
	public void aplicarEvento(Event message) {
		if (message instanceof AreaIrrigationOrderEvent) {
			AreaIrrigationOrderEvent order = (AreaIrrigationOrderEvent) message;
			if (order.area == this.area) {
				irrigationIcon.setAtiva(order.state);
				historico.registarAcao(order.state ? "Rega ligada" : "Rega desligada");
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		if (args.length != 0 && args.length != 1) {
			System.err.println("Invalid arguments!");
			return;
		}
		Areas area = (args.length == 1) ? Areas.valueOf(args[0]) : Areas.Jardim;
		Irrigator irr = new Irrigator(area);
		Thread.sleep(1000);
		irr.aplicarEvento(new AreaIrrigationOrderEvent(area, true));
	}
}
