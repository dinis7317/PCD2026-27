package iscot.client.gui;


import java.awt.BorderLayout;
import java.util.function.DoubleConsumer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class SensorControl extends JPanel {

    private final JSlider slider;
    private final JLabel valorLabel;
    private final double minimo;
    private final double passo;
    private final String unidade;
    private final DoubleConsumer atualizador;

    public SensorControl(
            String icone,
            String nome,
            String unidade,
            double minimo,
            double maximo,
            double passo,
            double valorInicial,
            DoubleConsumer atualizador) {

        super(new BorderLayout(5, 5));

        if (passo <= 0
                || maximo <= minimo
                || valorInicial < minimo
                || valorInicial > maximo
                || !Double.isFinite(minimo)
                || !Double.isFinite(maximo)
                || !Double.isFinite(passo)
                || !Double.isFinite(valorInicial)) {
            throw new IllegalArgumentException(
                "Intervalo, passo ou valor inicial inválido."
            );
        }

        this.minimo = minimo;
        this.passo = passo;
        this.unidade = unidade;
        this.atualizador = atualizador;

        setBorder(BorderFactory.createTitledBorder(
            icone + " " + nome
        ));

        int numeroPassos = (int) Math.round(
            (maximo - minimo) / passo
        );

        if (numeroPassos < 1) {
            throw new IllegalArgumentException(
                "O intervalo tem de conter pelo menos um passo."
            );
        }

        int passoInicial = (int) Math.round(
            (valorInicial - minimo) / passo
        );

        slider = new JSlider(0, numeroPassos, passoInicial);
        slider.setMajorTickSpacing(
            Math.max(1, numeroPassos / 4)
        );
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        valorLabel = new JLabel("", SwingConstants.CENTER);
        atualizarEtiqueta();

        slider.addChangeListener(e -> {
            atualizarEtiqueta();

            // Comunicar apenas quando o utilizador
            // termina de arrastar o slider.
            if (!slider.getValueIsAdjusting()
                    && this.atualizador != null) {
                this.atualizador.accept(getValor());
            }
        });

        add(slider, BorderLayout.CENTER);
        add(valorLabel, BorderLayout.SOUTH);
    }

    private void atualizarEtiqueta() {
        valorLabel.setText(
            String.format(
                "%.2f %s",
                getValor(),
                unidade
            )
        );
    }

    public double getValor() {
        return minimo + slider.getValue() * passo;
    }

    public void setValor(double valor) {
        if (!Double.isFinite(valor)
                || valor < minimo
                || valor > minimo + slider.getMaximum() * passo) {
            throw new IllegalArgumentException(
                "Valor fora do intervalo permitido."
            );
        }

        Runnable tarefa = () -> slider.setValue(
            (int) Math.round((valor - minimo) / passo)
        );

        if (SwingUtilities.isEventDispatchThread()) {
            tarefa.run();
        } else {
            SwingUtilities.invokeLater(tarefa);
        }
    }
}
