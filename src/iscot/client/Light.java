package iscot.client;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.LightIcon;
import iscot.client.gui.PainelHistorico;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.RegistrationEvent;
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
	 * ISSUE #3: Desacopla o processamento de eventos da camada de rede.
	 * 
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

	/**
	 * Inicia o ciclo de receção de eventos a partir de um servidor via Socket.
	 */
	public void iniciarConexao(String host, int port) throws IOException, ClassNotFoundException {
		Socket socket = new Socket(host, port);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		// Registo do atuador no servidor
		out.writeObject(new RegistrationEvent(area, Type.ACTUATOR));

		while (true) {
			Event message = (Event) in.readObject();
			aplicarEvento(message);
		}
	}

	/**
	 * Executável do cliente de luz.
	 * Suporta modo rede (3 argumentos) e modo teste/demonstração sem rede (0 argumentos).
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if (args.length != 3) {
			System.out.println("Modo teste/demonstração (Fase 3): a abrir janela na Sala...");
			Light light = new Light(Areas.Sala);
			// Exemplo de teste para a Fase 3
			Thread.sleep(1000);
			light.aplicarEvento(new AreaLightingOrderEvent(Areas.Sala, true));
		} else {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			Areas area = Areas.valueOf(args[2]);

			Light light = new Light(area);
			light.iniciarConexao(host, port);
		}
	}
}
