
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

public class DibujoAleatorios extends JPanel{
	private double[] puntos;
	private int tam;

	public DibujoAleatorios(double[] puntos, int tam){
		super();
		setBackground(new Color(240,240,240));
		this.puntos = puntos;
		this.tam=tam;
	}

	//heredado
	public Dimension getPreferredSize() {
		return new Dimension(150, 150);//150
    }

	//heredado
	public void paintComponent(Graphics g){
		//limpia el area de dibujado
		super.paintComponent(g);
		int height = getSize().height;
		int width = getSize().width;

		g.setColor(Color.RED);

		int delimitador;
		if(height<=width) delimitador=height; else delimitador=width;
		
		for(int i=0;i<puntos.length;i++){

				if(i+1 == puntos.length){ //final
					int val = (int)(puntos[i]*delimitador);
					if(val<0) val=-val;
					int val2 = (int)(puntos[i-1]*delimitador);
					if(val2<0) val2=-val2;

					//System.out.println("Punto: "+puntos[i]+" coorX: "+val+" coorY: "+val2);

					g.fillOval(val,val2,tam,tam);

				}else{
					int val = (int)(puntos[i]*delimitador);
					if(val<0) val=-val;
					int val2 = (int)(puntos[i+1]*delimitador);
					if(val2<0) val2=-val2;

					//System.out.println("Punto: "+puntos[i]+" coorX: "+val+" coorY: "+val2);

					g.fillOval(val,val2,tam,tam);
				}
		}
	}
}
