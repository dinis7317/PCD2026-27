package iscot.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import iscot.client.gui.PainelHistorico;
import iscot.client.gui.VentilatorIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.RegistrationEvent;
import iscot.event.highlevelorder.AreaVentilationOrderEvent;

public class Ventilator {

	private final Areas area;
	private final JFrame janela;
	private final VentilatorIcon ventilator;
	private final PainelHistorico historico;

	/**
	 * Construtor do Atuador de Ventilação.
	 * Configura a interface com ícone visual e histórico de ações (Secção 1.3).
	 * 
	 * @param area Divisão da casa onde o atuador opera
	 */
	public Ventilator(Areas area) {
		this.area = area;

		janela = new JFrame("Ventilador em " + area);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel painel = new JPanel(new BorderLayout(5, 5));
		painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Ícone central
		ventilator = new VentilatorIcon();
		painel.add(ventilator, BorderLayout.CENTER);

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
		// TODO (Issue #3): Implementar a interpretação do evento
		// 1. Verificar se message instanceof AreaVentilationOrderEvent
		// 2. Se for, fazer cast e verificar se order.area == this.area
		// 3. Atualizar o componente visual (ventilator.setAtivo(...))
		// 4. Registar no histórico: historico.registarAcao(...)
	}

	/**
	 * Inicia o ciclo de receção de eventos a partir de um servidor via Socket.
	 */
	public void iniciarConexao(String host, int port) throws IOException, ClassNotFoundException {
		Socket socket = new Socket(host, port);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		out.writeObject(new RegistrationEvent(area, Type.ACTUATOR));

		while (true) {
			Event message = (Event) in.readObject();
			aplicarEvento(message);
		}
	}

	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if (args.length != 3) {
			System.out.println("Modo teste/demonstração (Fase 3): a abrir janela no WC...");
			Ventilator vent = new Ventilator(Areas.WC);
			Thread.sleep(1000);
			vent.aplicarEvento(new AreaVentilationOrderEvent(Areas.WC, true));
		} else {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			Areas area = Areas.valueOf(args[2]);

			Ventilator vent = new Ventilator(area);
			vent.iniciarConexao(host, port);
		}
	}
}
