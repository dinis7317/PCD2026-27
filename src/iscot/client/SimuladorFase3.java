package iscot.client;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.event.Event;
import iscot.event.highlevelorder.AreaIrrigationOrderEvent;
import iscot.event.highlevelorder.AreaLightingOrderEvent;
import iscot.event.highlevelorder.AreaTemperatureOrderEvent;
import iscot.event.highlevelorder.AreaVentilationOrderEvent;

/**
 * ISSUE #4: Simulador para a apresentação intermédia (Fases 1 a 3).
 * Abre, sem servidor, os 4 sensores, os 4 atuadores e um painel de controlo
 * com botões que injetam eventos de exemplo nos atuadores.
 */
public class SimuladorFase3 {

    private static Light luz;
    private static ArCondicionado ac;
    private static Ventilator ventilador;
    private static Irrigator rega;

    // posição onde vai ser colocada a próxima janela
    private static int x = 10, y = 10, alturaLinha = 0;

    /** Interface para poder usar lambdas que lançam exceções (os main dos sensores). */
    private interface Abertura {
        void abrir() throws Exception;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Simulador Fase 3 (sem servidor) ===");

        // 1. Painel de controlo
        JFrame controlo = criarPainelControlo();
        colocar(controlo);

        // 2. Atuadores (cada construtor cria e mostra a sua janela)
        abrir("Atuador - Luz - Sala", () -> luz = new Light(Areas.Sala));
        abrir("Atuador - Ar condicionado - Quarto", () -> ac = new ArCondicionado(Areas.Quarto));
        abrir("Atuador - Ventilador - WC", () -> ventilador = new Ventilator(Areas.WC));
        abrir("Atuador - Rega - Jardim", () -> rega = new Irrigator(Areas.Jardim));

        // 3. Sensores (main sem argumentos = modo local, imprimem os eventos na consola)
        abrir("Sensor - Luz - Sala", () -> LightSensor.main(new String[0]));
        abrir("Sensor - Temperatura - Quarto", () -> TemperatureSensor.main(new String[0]));
        abrir("Sensor - Humidade - WC", () -> HumiditySensor.main(new String[0]));
        abrir("Sensor - Humidade do solo - Jardim", () -> GroundMoistureSensor.main(new String[0]));
    }

    /** Janela com os botões que injetam eventos de exemplo nos atuadores. */
    private static JFrame criarPainelControlo() {
        JPanel painel = new JPanel(new GridLayout(0, 1, 5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pLuz = grupo(painel, "Luz - Sala");
        botao(pLuz, "Ligar luz", new AreaLightingOrderEvent(Areas.Sala, true), e -> luz.aplicarEvento(e));
        botao(pLuz, "Desligar luz", new AreaLightingOrderEvent(Areas.Sala, false), e -> luz.aplicarEvento(e));

        JPanel pAc = grupo(painel, "Ar condicionado - Quarto");
        botao(pAc, "AC frio 19 ºC", new AreaTemperatureOrderEvent(Areas.Quarto, AirConditioningMode.Frio, 19), e -> ac.aplicarEvento(e));
        botao(pAc, "AC quente 23 ºC", new AreaTemperatureOrderEvent(Areas.Quarto, AirConditioningMode.Aquecimento, 23), e -> ac.aplicarEvento(e));
        botao(pAc, "AC ventilação", new AreaVentilationOrderEvent(Areas.Quarto, true), e -> ac.aplicarEvento(e));
        botao(pAc, "AC desligar", new AreaTemperatureOrderEvent(Areas.Quarto, AirConditioningMode.Desligado, 0), e -> ac.aplicarEvento(e));

        JPanel pVent = grupo(painel, "Ventilador - WC");
        botao(pVent, "Ligar ventilador", new AreaVentilationOrderEvent(Areas.WC, true), e -> ventilador.aplicarEvento(e));
        botao(pVent, "Desligar ventilador", new AreaVentilationOrderEvent(Areas.WC, false), e -> ventilador.aplicarEvento(e));

        JPanel pRega = grupo(painel, "Rega - Jardim");
        botao(pRega, "Ligar rega", new AreaIrrigationOrderEvent(Areas.Jardim, true), e -> rega.aplicarEvento(e));
        botao(pRega, "Desligar rega", new AreaIrrigationOrderEvent(Areas.Jardim, false), e -> rega.aplicarEvento(e));

        JFrame janela = new JFrame("Painel de controlo - Fase 3");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fechar esta janela termina o simulador
        janela.setContentPane(painel);
        janela.pack();
        janela.setVisible(true);
        return janela;
    }

    private static JPanel grupo(JPanel pai, String titulo) {
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder(titulo));
        pai.add(p);
        return p;
    }

    /** Cria um botão que, ao ser carregado, imprime o evento e entrega-o ao atuador. */
    private static void botao(JPanel p, String texto, Event evento, Consumer<Event> atuador) {
        JButton b = new JButton(texto);
        b.addActionListener(a -> {
            System.out.println("Evento injetado: " + evento);
            atuador.accept(evento);
        });
        p.add(b);
    }

    /** Executa o código que cria uma janela, muda-lhe o título e coloca-a no ecrã. */
    private static void abrir(String titulo, Abertura abertura) throws Exception {
        List<Frame> antes = Arrays.asList(Frame.getFrames());
        abertura.abrir();
        for (Frame f : Frame.getFrames()) {
            if (!antes.contains(f)) { // é a janela que acabou de ser criada
                f.setTitle(titulo);
                colocar(f);
            }
        }
    }

    /** Coloca as janelas lado a lado, passando para a linha seguinte quando não cabem. */
    private static void colocar(Frame f) {
        Dimension ecra = Toolkit.getDefaultToolkit().getScreenSize();
        if (x + f.getWidth() > ecra.width) {
            x = 10;
            y += alturaLinha + 10;
            alturaLinha = 0;
        }
        f.setLocation(x, y);
        x += f.getWidth() + 10;
        alturaLinha = Math.max(alturaLinha, f.getHeight());
    }
}