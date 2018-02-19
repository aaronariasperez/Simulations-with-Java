import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.util.Random;


public class ca2DSimulatorBelzab implements Runnable{
	private static float[][][] a, b, c;
	private static int p=0, q=1;
	private static float alfa=1.2f, beta=1.0f, gamma=1.0f;
  	private static Color[][] matriz_imagen;
  	private static Random random = new Random();

	private static int dimension;
	private static JPanel panel_grafico;
	private static Semaphore sem = new Semaphore(1);
	private static boolean actualizado = false;
	private static double TARGET_FPS=60;
	private static boolean stop =false;

	private CyclicBarrier bar;
	private int linf;
	private int lsup;


	public ca2DSimulatorBelzab(int inf, int sup, CyclicBarrier b){
		this.linf=inf;
		this.lsup=sup;
		this.bar=b;
	}

	public static void setPanelGrafico(JPanel panel){
		panel_grafico=panel;
	}

	public static void setDim(int d){
		dimension=d;
	}

	public static void reiniciaBool(){
		actualizado=false;
	}

	public static void stopNow(){
		stop=true;
	}

	public static void resetStop(){
		stop=false;
	}

	public static void setFPS(int f){
		TARGET_FPS=f;
	}

	public static void setAlfa(float a){
	 	alfa=a;
	}

 	public static void setBeta(float b){ 
 		beta=b;
 	}

  	public static void setGamma(float g){ 
  		gamma=g;
  	}

	public static void generacionInicial(){
		a = new float[dimension][dimension][2];
   		b = new float[dimension][dimension][2];
    	c = new float[dimension][dimension][2];

    	matriz_imagen = new Color[dimension][dimension];

	    for(int i=0; i<dimension; i++){
	      	for(int j=0; j<dimension ; j++){
		        a[i][j][p] = random.nextFloat();
		        b[i][j][p] = random.nextFloat();
		        c[i][j][p] = random.nextFloat();
	      	}
   		}

		
		try{ paint(); }catch(Exception e){}
		panel_grafico.revalidate();

	}

	public void run(){
		while(true){
			
			if(stop) break;

			if(actualizado){actualizado=false;}

			nextGen();
			try{ bar.await(); }catch(Exception e){}

			try{ sem.acquire();}catch(Exception e){}
			if(!actualizado){

				long end = System.nanoTime() + (long) (1000000000.0 / TARGET_FPS);
				while (System.nanoTime() < end);

				try{ paint(); }catch(Exception e){}

				panel_grafico.revalidate();

				if(p==0){ 
					p=1; q=0;	
				}else{ 
					p=0; q=1;
				}

				actualizado=true;
			}
			sem.release();

			try{ bar.await(); }catch(Exception e){}

		}
			
	}

	private void nextGen(){
		for(int i=0;i<dimension;i++){
			for(int j=linf;j<lsup;j++){
				computa(i,j);
			}
		}
	}

	private void computa(int i, int j){
		float c_a=0.0f, c_b=0.0f, c_c=0.0f;

        for(int k=i-1; k<=i+1; k++){
	        for(int l=j-1; l<=j+1 ; l++){
	            c_a+=a[(k+dimension)%dimension][(l+dimension)%dimension][p];
	            c_b+=b[(k+dimension)%dimension][(l+dimension)%dimension][p];
	        	c_c+=c[(k+dimension)%dimension][(l+dimension)%dimension][p];
	        }
        }

        c_a/=9.0; 
        c_b/=9.0; 
        c_c/=9.0;

        a[i][j][q] = constrain(c_a+c_a*(alfa*c_b-gamma*c_c));
        b[i][j][q] = constrain(c_b+c_b*(beta*c_c-alfa*c_a));
        c[i][j][q] = constrain(c_c+c_c*(gamma*c_a-beta*c_b));
        
        matriz_imagen[i][j] = new Color(a[i][j][q], b[i][j][q], c[i][j][q]);

	}

	public float constrain(float i){
	    float res=i;

	    if(i<0){
	    	res = 0;
	    }else{
	      	if(i>1) res = 1;
	    }

	    return res;
  }

	public static void paint() throws InterruptedException{
	    BufferedImage image=new BufferedImage(matriz_imagen.length, matriz_imagen[0].length, BufferedImage.TYPE_INT_RGB);

	    for (int i=0;i<matriz_imagen.length ; i++){
	      	for(int j=0; j<matriz_imagen[0].length; j++){
		    	image.setRGB(i,j,matriz_imagen[i][j].getRGB());
	     	}
	    }
	    Graphics g = panel_grafico.getGraphics();
	    g.drawImage(image, 0, 0, panel_grafico.getHeight(),panel_grafico.getWidth(), panel_grafico);
  	}
}