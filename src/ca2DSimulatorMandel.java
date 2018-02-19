import java.util.concurrent.*;
import java.awt.image.BufferedImage;

public class ca2DSimulatorMandel implements Runnable{
	private static int dimension;
	private static int iteraciones;
	private static BufferedImage imagen;
	private final double ZOOM = 220;

	private int linf;
	private int lsup;
	private double zx, zy, cX, cY, tmp;


	public ca2DSimulatorMandel(int inf, int sup){
		this.linf=inf;
		this.lsup=sup;
	}

	public static void setIter_Dim(int it, int d){
		iteraciones=it;
		dimension=d;
	}

	public static BufferedImage getImagen(){
		return imagen;
	}

	public static void generacionInicial(){
		imagen = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_RGB);
	}

	public void run(){
		computa();		
	}

	private void computa(){
		for (int y = linf; y < lsup; y++) {
     		for (int x = 0; x < dimension; x++) {
	     	    zx = zy = 0;
	     	    cX = (x - 400) / ZOOM;
	     	    cY = (y - 300) / ZOOM;
	     	    int iter = iteraciones;
	     	    while (zx * zx + zy * zy < 4 && iter > 0) {
	     	     	    tmp = zx * zx - zy * zy + cX;
	     	     	    zy = 2.0 * zx * zy + cY;
	     	     	    zx = tmp;
	     	     	    iter--;
	     	    }	
	     	    
	     	    imagen.setRGB(x, y, iter | (iter << 8));
     		}
     	}
	}
}