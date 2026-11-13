package iscot.client;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.PainelHistorico;
import iscot.client.gui.VentilatorIcon;
import iscot.enums.Areas;
import iscot.event.Event;
import iscot.event.highlevelorder.AreaVentilationOrderEvent;

public class Ventilator {

	private final Areas area;
	private final JFrame janela;
	private final VentilatorIcon ventilator;
	private final PainelHistorico historico;

	public Ventilator(Areas area) {
		this.area = area;

		janela = new JFrame("Ventilador em " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel painel = new JPanel(new BorderLayout(5, 5));
		painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		ventilator = new VentilatorIcon();
		painel.add(ventilator, BorderLayout.CENTER);

		historico = new PainelHistorico();
		painel.add(historico, BorderLayout.SOUTH);

		janela.setContentPane(painel);
		janela.setLocation(600, 400);
		janela.pack();
		janela.setVisible(true);
	}

	public void aplicarEvento(Event message) {
		if (message instanceof AreaVentilationOrderEvent) {
			AreaVentilationOrderEvent order = (AreaVentilationOrderEvent) message;
			if (order.getArea() == this.area) {
				ventilator.setLigado(order.isLigado());
				historico.registarAcao(order.isLigado() ? "Ventilador ligado" : "Ventilador desligado");
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		if (args.length != 0 && args.length != 1) {
			System.err.println("Invalid arguments!");
			return;
		}
		Areas area = (args.length == 1) ? Areas.valueOf(args[0]) : Areas.WC;
		Ventilator vent = new Ventilator(area);
		Thread.sleep(1000);
		vent.aplicarEvento(new AreaVentilationOrderEvent(area, true));
	}
}
