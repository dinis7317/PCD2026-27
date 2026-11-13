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

public class PainelHistorico extends JPanel {

	private final DefaultListModel<String> modeloLista;
	private final JList<String> listaVisual;

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
}
