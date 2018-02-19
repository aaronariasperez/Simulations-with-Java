

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class PanelGrafico extends JPanel{
	private static int tam=1;
	private static int[] array;
	private static int generacion;
	private static boolean clearable;
	private static String[] colors = {"#000000","#F80606","#04DBF8","#2BED04","#F801BA","#FCCE00"};

	public PanelGrafico(){
		array = new int[0];
		generacion=0;
		setBackground(new Color(0,0,0));
	}

	public void setClearable(boolean b){
		clearable=b;
	}

	public void actualizaPanelGrafico(int[] ar, int g){
		array = new int[ar.length];
		for(int i=0;i<ar.length;i++){
			array[i] = ar[i];
		}
		generacion = g;
	}

	private int hex( String color_hex )
        {
            return Integer.parseInt(color_hex,  16 );
    }

	public void paintComponent(Graphics g){
		if(clearable){
			super.paintComponent(g);
		} 


		for(int i=0;i<array.length;i++){
			g.setColor(Color.decode(colors[array[i]]));
			g.fillRect(i,generacion,tam,tam);
		}
	}
}