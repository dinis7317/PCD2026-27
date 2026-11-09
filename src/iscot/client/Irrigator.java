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

import iscot.client.gui.IrrigationIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.RegistrationEvent;
import iscot.event.highlevelorder.AreaIrrigationOrderEvent;

public class Irrigator {

	private final Areas area;
	private final JFrame janela;
	private final IrrigationIcon irrigationIcon;
	private final DefaultListModel<String> historicoModel;
	private final JList<String> historicoList;

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

		// Componente de Histórico de Ações (Issue #1 / Secção 1.3)
		historicoModel = new DefaultListModel<>();
		historicoList = new JList<>(historicoModel);
		historicoList.setVisibleRowCount(5);
		JScrollPane scrollHistorico = new JScrollPane(historicoList);
		scrollHistorico.setBorder(BorderFactory.createTitledBorder("Histórico de Ações"));
		scrollHistorico.setPreferredSize(new Dimension(260, 120));
		painel.add(scrollHistorico, BorderLayout.SOUTH);

		janela.setContentPane(painel);
		janela.setLocation(600, 400);
		janela.pack();
		janela.setVisible(true);
	}

	/**
	 * ISSUE #1 (Secção 1.3): Regista uma ação tomada no histórico verificável pelo utilizador com timestamp.
	 * 
	 * @param descricao Texto descritivo da ação realizada (ex: "Rega ativada")
	 */
	public void adicionarAcaoAoHistorico(String descricao) {
		// TODO (Issue #1): Implementar a adição ao histórico
		// 1. Obter a hora atual formatada em "HH:mm:ss"
		// 2. Construir a mensagem: descricao + " - " + timestamp
		// 3. Adicionar ao historicoModel
		// 4. Fazer scroll para o último item
	}

	/**
	 * ISSUE #3: Desacopla a interpretação de eventos da camada de rede.
	 * 
	 * @param message Evento recebido a ser interpretado pelo atuador
	 */
	public void aplicarEvento(Event message) {
		// TODO (Issue #3): Implementar a interpretação do evento
		// 1. Verificar se message instanceof AreaIrrigationOrderEvent
		// 2. Se for, fazer cast e verificar se order.area == this.area
		// 3. Atualizar o componente visual (irrigationIcon.setAtivo(...))
		// 4. Chamar adicionarAcaoAoHistorico(...) com a descrição da ação
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
			System.out.println("Modo teste/demonstração (Fase 3): a abrir janela no Jardim...");
			Irrigator irr = new Irrigator(Areas.Jardim);
			Thread.sleep(1000);
			irr.aplicarEvento(new AreaIrrigationOrderEvent(Areas.Jardim, true));
		} else {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			Areas area = Areas.valueOf(args[2]);

			Irrigator irr = new Irrigator(area);
			irr.iniciarConexao(host, port);
		}
	}
}
