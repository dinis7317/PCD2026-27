package iscot.client;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.LightIcon;
import iscot.client.gui.PainelHistorico;
import iscot.enums.Areas;
import iscot.event.Event;
import iscot.event.highlevelorder.AreaLightingOrderEvent;

public class Light {

	private final Areas area;
	private final JFrame janela;
	private final LightIcon luz;
	private final PainelHistorico historico;

	/**
	 * Construtor do Atuador de Luz.
	 * 
	 * @param area Divisão da casa onde o atuador opera
	 */
	public Light(Areas area) {
		this.area = area;

		janela = new JFrame("Luz em " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel painel = new JPanel(new BorderLayout(5, 5));
		painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Ícone gráfico central
		luz = new LightIcon();
		painel.add(luz, BorderLayout.CENTER);

		// Componente reutilizável de Histórico de Ações (Issue #1 / Secção 1.3)
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
		if (message instanceof AreaLightingOrderEvent) {
			AreaLightingOrderEvent order = (AreaLightingOrderEvent) message;
			if (order.area == this.area) {
				luz.setLigada(order.state);
				historico.registarAcao(order.state ? "Lâmpada ligada" : "Lâmpada desligada");
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		if (args.length != 0 && args.length != 1) {
			System.err.println("Invalid arguments!");
			return;
		}

		Areas area = (args.length == 1) ? Areas.valueOf(args[0]) : Areas.Sala;
		Light light = new Light(area);
		Thread.sleep(1000);
		light.aplicarEvento(new AreaLightingOrderEvent(area, true));
	}
}
