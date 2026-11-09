package iscot.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Componente gráfico reutilizável para o Histórico de Ações dos Atuadores.
 * Cumpre os requisitos da Secção 1.3 do enunciado do projeto PCD.
 */
public class PainelHistorico extends JPanel {

	private final DefaultListModel<String> modeloLista;
	private final JList<String> listaVisual;

	/**
	 * Construtor: Inicializa a lista gráfica e coloca-a dentro de um painel com scroll.
	 */
	public PainelHistorico() {
		super(new BorderLayout());

		modeloLista = new DefaultListModel<>();
		listaVisual = new JList<>(modeloLista);
		listaVisual.setVisibleRowCount(5);

		JScrollPane scrollPane = new JScrollPane(listaVisual);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Histórico de Ações"));
		scrollPane.setPreferredSize(new Dimension(260, 120));

		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * ISSUE #1 (Secção 1.3 do Enunciado):
	 * Regista uma ação tomada pelo atuador no histórico visível, acompanhada de timestamp.
	 * 
	 * Formato exigido pelo enunciado: "Ação - HH:MM:SS" (ex: "Lâmpada ligada - 14:30:15").
	 * 
	 * @param descricao Texto descritivo da ação realizada (ex: "Lâmpada ligada", "Ventilador desligado")
	 */
	public void registarAcao(String descricao) {
		if (descricao != null) {
			SwingUtilities.invokeLater(() -> {
				modeloLista.addElement(descricao + " - " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
				listaVisual.ensureIndexIsVisible(modeloLista.size() - 1);
			});
		}else {
			System.err.println("Descrição da ação não pode ser nula.");
		}
	}

	/**
	 * Método auxiliar para obter o modelo de dados, se necessário.
	 */
	public DefaultListModel<String> getModeloLista() {
		return modeloLista;
	}
}
