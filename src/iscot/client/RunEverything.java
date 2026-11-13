package iscot.client;

import java.awt.*;
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
 * Corre todos os sensores e atuadores localmente, e cria uma janela com botões que enviam ordens aos atuadores.
 */
public class RunEverything {

	public static void main(String[] args) throws Exception {
		//sensores
		LightSensor.main(new String[] { Areas.Sala.name() });
		TemperatureSensor.main(new String[] { Areas.Quarto.name() });
		HumiditySensor.main(new String[] { Areas.WC.name() });
		GroundMoistureSensor.main(new String[] { Areas.Jardim.name() });

		// Atuadores
		Light luz = new Light(Areas.Sala);
		ArCondicionado ac = new ArCondicionado(Areas.Quarto);
		Ventilator ventilador = new Ventilator(Areas.WC);
		Irrigator rega = new Irrigator(Areas.Jardim);

		criarCentroDeComando(luz, ac, ventilador, rega);
	}

	/** Janela com os botões que enviam ordens aos atuadores. */
	private static void criarCentroDeComando(Light luz, ArCondicionado ac, Ventilator ventilador, Irrigator rega) {
		JPanel painel = new JPanel(new GridLayout(0, 1, 5, 5));
		painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel pLuz = grupo(painel, "Luz - Sala");
		botao(pLuz, "Ligar luz", new AreaLightingOrderEvent(Areas.Sala, true), luz::aplicarEvento);
		botao(pLuz, "Desligar luz", new AreaLightingOrderEvent(Areas.Sala, false), luz::aplicarEvento);

		JPanel pAc = grupo(painel, "Ar condicionado - Quarto");
		botao(pAc, "AC frio 19 ºC", new AreaTemperatureOrderEvent(Areas.Quarto, AirConditioningMode.Frio, 19), ac::aplicarEvento);
		botao(pAc, "AC quente 23 ºC", new AreaTemperatureOrderEvent(Areas.Quarto, AirConditioningMode.Aquecimento, 23), ac::aplicarEvento);
		botao(pAc, "AC ventilação", new AreaVentilationOrderEvent(Areas.Quarto, true), ac::aplicarEvento);
		botao(pAc, "AC desligar", new AreaTemperatureOrderEvent(Areas.Quarto, AirConditioningMode.Desligado, 0), ac::aplicarEvento);

		JPanel pVent = grupo(painel, "Ventilador - WC");
		botao(pVent, "Ligar ventilador", new AreaVentilationOrderEvent(Areas.WC, true), ventilador::aplicarEvento);
		botao(pVent, "Desligar ventilador", new AreaVentilationOrderEvent(Areas.WC, false), ventilador::aplicarEvento);

		JPanel pRega = grupo(painel, "Rega - Jardim");
		botao(pRega, "Ligar rega", new AreaIrrigationOrderEvent(Areas.Jardim, true), rega::aplicarEvento);
		botao(pRega, "Desligar rega", new AreaIrrigationOrderEvent(Areas.Jardim, false), rega::aplicarEvento);

		JFrame janela = new JFrame("Centro de comando");
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setContentPane(painel);
		janela.pack();
		janela.setLocation(10, 10);
		janela.setVisible(true);
	}

	private static JPanel grupo(JPanel pai, String titulo) {
		JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
		p.setBorder(BorderFactory.createTitledBorder(titulo));
		pai.add(p);
		return p;
	}

	// botao que imprime a ordem e a entrega ao atuador.
	private static void botao(JPanel p, String texto, Event evento, Consumer<Event> atuador) {
		JButton b = new JButton(texto);
		b.addActionListener(a -> {
			System.out.println("Ordem enviada: " + evento);
			atuador.accept(evento);
		});
		p.add(b);
	}
}
