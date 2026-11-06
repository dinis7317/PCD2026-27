package iscot.client.gui;

import java.awt.*;
import java.awt.geom.*;

public class IrrigationIcon extends ComponentState {
	private int zona = 1;
	private int duracao = 10;

	public void setAtiva(boolean ativa) {
	    setAtivo(ativa);
	}

	public boolean isAtiva() {
	    return isAtivo();
	}

	public void setZona(int zona) {
	    if (zona < 1 || zona > 3) {
	        throw new IllegalArgumentException(
	            "A zona deve ser 1, 2 ou 3."
	        );
	    }

	    this.zona = zona;
	}

	public int getZona() {
	    return zona;
	}

	public void setDuracao(int duracao) {
	    if (duracao < 0) {
	        throw new IllegalArgumentException(
	            "A duração não pode ser negativa."
	        );
	    }

	    this.duracao = duracao;
	}

	public int getDuracao() {
	    return duracao;
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

        Path2D gota = new Path2D.Double();

        gota.moveTo(cx, cy - 35);
        gota.curveTo(
            cx - 5, cy - 20,
            cx - 25, cy + 3,
            cx - 25, cy + 15
        );
        gota.curveTo(
            cx - 25, cy + 32,
            cx - 12, cy + 40,
            cx, cy + 40
        );
        gota.curveTo(
            cx + 12, cy + 40,
            cx + 25, cy + 32,
            cx + 25, cy + 15
        );
        gota.curveTo(
            cx + 25, cy + 3,
            cx + 5, cy - 20,
            cx, cy - 35
        );

        gota.closePath();

        g2.setColor(
            isAtivo()
                ? new Color(0, 145, 225)
                : new Color(160, 165, 170)
        );

        g2.fill(gota);

        g2.dispose();
    }
}