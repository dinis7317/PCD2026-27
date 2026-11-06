package iscot.client.gui;

import java.awt.*;
import java.awt.geom.*;

import iscot.enums.AirConditioningMode;

public class ACIcon extends ComponentState {
	
	
	private AirConditioningMode mode = AirConditioningMode.Desligado;
	private double temperature = -1;

	public boolean isLigado() {
	    return isAtivo();
	}
/** 
 * 
 * @param modo: valores 
 */
	public void setModo(AirConditioningMode mode) {
	    this.mode = mode;
	    setAtivo(mode!=AirConditioningMode.Desligado);
	}

	public AirConditioningMode getModo() {
	    return mode;
	}

	public void setTemperatura(double temperatura) {
	    if (temperatura < 16 || temperatura > 30) {
	        throw new IllegalArgumentException(
	            "A temperatura deve estar entre 16 e 30 °C."
	        );
	    }

	    this.temperature = temperatura;
	}

	public double getTemperatura() {
	    return temperature;
	}
	
    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        if (!isAtivo()) {
            g2.setColor(corEstado());
            g2.fillOval(cx - 22, cy - 22, 44, 44);
        } else {
            g2.setColor(new Color(0, 135, 220));
            g2.setStroke(new BasicStroke(
                4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND
            ));

            for (int angulo = 0; angulo < 180; angulo += 60) {
                double a = Math.toRadians(angulo);

                int dx = (int) (28 * Math.cos(a));
                int dy = (int) (28 * Math.sin(a));

                g2.drawLine(
                    cx - dx, cy - dy,
                    cx + dx, cy + dy
                );
            }
        }

        g2.dispose();
    }
}