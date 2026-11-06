package iscot.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class LightIcon extends ComponentState {
	private int intensidade = 100;

	public void setLigada(boolean ligada) {
	    setAtivo(ligada);
	}

	public boolean isLigada() {
	    return isAtivo();
	}

	public void setIntensidade(int intensidade) {
	    if (intensidade < 0 || intensidade > 100) {
	        throw new IllegalArgumentException(
	            "A intensidade deve estar entre 0 e 100."
	        );
	    }

	    this.intensidade = intensidade;
	}

	public int getIntensidade() {
	    return intensidade;
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

        Color cor = isAtivo()
            ? new Color(255, 190, 0)
            : new Color(155, 160, 165);

        g2.setColor(cor);
        g2.fillOval(cx - 22, cy - 27, 44, 44);
        g2.fillRoundRect(cx - 12, cy + 15, 24, 16, 4, 4);

        if (isAtivo()) {
            g2.setStroke(new BasicStroke(3));

            for (int a = 0; a < 360; a += 45) {
                double rad = Math.toRadians(a);

                int x1 = cx + (int) (29 * Math.cos(rad));
                int y1 = cy - 5 + (int) (29 * Math.sin(rad));

                int x2 = cx + (int) (37 * Math.cos(rad));
                int y2 = cy - 5 + (int) (37 * Math.sin(rad));

                g2.drawLine(x1, y1, x2, y2);
            }
        }

        g2.dispose();
    }
}
