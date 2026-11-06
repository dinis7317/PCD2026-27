package iscot.client.gui;

import java.awt.*;
import java.awt.geom.*;

public class VentilatorIcon extends ComponentState {
	private int velocidade = 1;

	public void setLigado(boolean ligado) {
	    setAtivo(ligado);
	}

	public boolean isLigado() {
	    return isAtivo();
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

        g2.setColor(corEstado());

        for (int i = 0; i < 3; i++) {
            Graphics2D folha = (Graphics2D) g2.create();

            folha.rotate(
                Math.toRadians(i * 120), cx, cy
            );

            Path2D forma = new Path2D.Double();

            forma.moveTo(cx, cy - 5);
            forma.curveTo(
                cx - 8, cy - 18,
                cx - 12, cy - 35,
                cx, cy - 39
            );
            forma.curveTo(
                cx + 12, cy - 35,
                cx + 8, cy - 18,
                cx, cy - 5
            );

            forma.closePath();
            folha.fill(forma);
            folha.dispose();
        }

        g2.setColor(Color.GRAY);
        g2.fillOval(cx - 9, cy - 9, 18, 18);

        g2.dispose();
    }
}