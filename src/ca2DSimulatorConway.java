import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;


public class ca2DSimulatorConway implements Runnable{
	private static int[][] matriz_origen;
	private static int[][] matriz_siguiente;
	private static int dimension;
	private static int iteraciones;
	private static JPanel panel_grafico;
	private static int[][] valores; //para cada iteracion, poblacion de 0 y 1
	private static AtomicInteger contCeros = new AtomicInteger();
	private static AtomicInteger contUnos = new AtomicInteger();
	private static Semaphore sem = new Semaphore(1);
	private static boolean actualizado = false;
	private static int seed=1;
	private static String option;
	private static double TARGET_FPS=60;
	private static boolean stop =false;

	private CyclicBarrier bar;
	private int linf;
	private int lsup;


	public ca2DSimulatorConway(int inf, int sup, CyclicBarrier b){
		this.linf=inf;
		this.lsup=sup;
		this.bar=b;
	}

	public static void setPanelGrafico(JPanel panel){
		panel_grafico=panel;
	}

	public static void setIter_Dim(int it, int d){
		iteraciones=it;
		dimension=d;
	}

	public static void setArraysValores(int n){
		valores = new int[2][n+1];
	}

	public static int[][] getValores(){
		return valores;
	}

	public static void setAtomics(AtomicInteger c1, AtomicInteger c2){
		contCeros = c1;
		contUnos = c2;
	}

	public static void reiniciaBool(){
		actualizado=false;
	}

	public static void setSeed(int sd){
		seed=sd;
	}

	public static void setOption(String opt){
		option=opt;
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

	public static void generacionInicial(){
		matriz_origen = new int[dimension][dimension];
		matriz_siguiente = new int[dimension][dimension];

		double seed_aux=seed;
		double aux=0.0;

		if(option.equals("Glider")){
			
			matriz_origen[0+50][4+50]=1;  matriz_origen[0+50][5+50]=1;  matriz_origen[1+50][4+50]=1;  matriz_origen[1+50][5+50]=1;
	        matriz_origen[10+50][4+50]=1; matriz_origen[10+50][5+50]=1; matriz_origen[10+50][6+50]=1; matriz_origen[11+50][3+50]=1;
	        matriz_origen[11+50][7+50]=1; matriz_origen[12+50][2+50]=1; matriz_origen[12+50][8+50]=1; matriz_origen[13+50][2+50]=1;
	        matriz_origen[13+50][8+50]=1; matriz_origen[14+50][5+50]=1; matriz_origen[15+50][3+50]=1; matriz_origen[15+50][7+50]=1;
	        matriz_origen[16+50][4+50]=1; matriz_origen[16+50][5+50]=1; matriz_origen[16+50][6+50]=1; matriz_origen[17+50][5+50]=1;
	        matriz_origen[20+50][2+50]=1; matriz_origen[20+50][3+50]=1; matriz_origen[20+50][4+50]=1; matriz_origen[21+50][2+50]=1;
	        matriz_origen[21+50][3+50]=1; matriz_origen[21+50][4+50]=1; matriz_origen[22+50][1+50]=1; matriz_origen[22+50][5+50]=1;
	        matriz_origen[24+50][0+50]=1; matriz_origen[24+50][1+50]=1; matriz_origen[24+50][5+50]=1; matriz_origen[24+50][6+50]=1;
	        matriz_origen[34+50][2+50]=1; matriz_origen[34+50][3+50]=1; matriz_origen[35+50][2+50]=1; matriz_origen[35+50][3+50]=1;

        	contUnos.set(36);
        	contCeros.set(dimension*dimension - 36);

		}else if(option.equals("Random")){
			for(int i=0;i<dimension;i++){
				for(int j=0;j<dimension;j++){
					seed_aux=randomGenerator.fRandu(seed_aux);
					aux=seed_aux/Math.pow(2,31);
					matriz_origen[i][j] = (int)Math.floor(aux*2);
					if((int)Math.floor(aux*2) == 0){
						contCeros.incrementAndGet();
					}else{
						contUnos.incrementAndGet();
					}
				}
			}
		}else if(option.equals("Center")){
			for(int i=(dimension/2)-15;i<(dimension/2)+15;i++){
				for(int j=(dimension/2)-15;j<(dimension/2)+15;j++){
					seed_aux=randomGenerator.fRandu(seed_aux);
					aux=seed_aux/Math.pow(2,31);
					matriz_origen[i][j] = (int)Math.floor(aux*2);
					if((int)Math.floor(aux*2) == 0){
						contCeros.incrementAndGet();
					}else{
						contUnos.incrementAndGet();
					}
				}
			}

		}else if(option.equals("Infinite")){
			matriz_origen[50][50]=1;	matriz_origen[50][51]=1;	matriz_origen[50][52]=0;	matriz_origen[50][53]=0;	matriz_origen[50][54]=1;
	        matriz_origen[51][50]=1;	matriz_origen[51][51]=0;	matriz_origen[51][52]=0;	matriz_origen[51][53]=1;	matriz_origen[51][54]=0;
	        matriz_origen[52][50]=1;	matriz_origen[52][51]=0;	matriz_origen[52][52]=0;	matriz_origen[52][53]=1;	matriz_origen[52][54]=1;
	        matriz_origen[53][50]=0;	matriz_origen[53][51]=0;	matriz_origen[53][52]=1;	matriz_origen[53][53]=0;	matriz_origen[53][54]=0;
	        matriz_origen[54][50]=1;	matriz_origen[54][51]=0;	matriz_origen[54][52]=1;	matriz_origen[54][53]=1;	matriz_origen[54][54]=1;

	        contUnos.set(13);
	        contCeros.set(dimension*dimension - 13);
		}

		valores[0][0] = contCeros.get();
		valores[1][0] = contUnos.get();

		contCeros.set(0);
		contUnos.set(0);
		
		try{ paint(); }catch(Exception e){}
		panel_grafico.revalidate();

	}

	public void run(){
		for(int g=0;g<iteraciones;g++){
			if(stop) break;

			if(actualizado){actualizado=false;}

			nextGen();
			try{ bar.await(); }catch(Exception e){}

			copiarMatriz();
			try{ bar.await(); }catch(Exception e){}

			try{ sem.acquire();}catch(Exception e){}
			if(!actualizado){

				long end = System.nanoTime() + (long) (1000000000.0 / TARGET_FPS);
  				while (System.nanoTime() < end);
				try{ paint(); }catch(Exception e){}

				panel_grafico.revalidate();

				valores[0][g+1] = contCeros.get();
				valores[1][g+1] = contUnos.get();

				contCeros.set(0);
				contUnos.set(0);

				actualizado=true;
			}
			sem.release();

			try{ bar.await(); }catch(Exception e){}
		}		
	}

	private void copiarMatriz(){
		for(int i=0;i<dimension;i++){
			for(int j=0;j<dimension;j++){
				matriz_origen[i][j] = matriz_siguiente[i][j];
			}
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
		if(matriz_origen[i][j] == 1){ //si esta viva
			int num = countVecinas(i,j);
			if(num < 2 || num > 3) matriz_siguiente[i][j] = 0; //muere
			else matriz_siguiente[i][j] = 1; //muere

		}else{ //si esta muerta

			if(countVecinas(i,j) == 3) matriz_siguiente[i][j] = 1;
			else matriz_siguiente[i][j] = 0;
		}

		int valor = matriz_siguiente[i][j];

		if(valor==0){
			contCeros.incrementAndGet();
		}else{
			contUnos.incrementAndGet();
		}

	}

	private int countVecinas(int a, int b){
		int aux_i;
		int aux_j;
		int cont=0;
		for(int i=a-1; i<a+2; i++){
			for(int j=b-1; j<b+2; j++){
				aux_i = i;
				aux_j = j;
				if(i<0) aux_i = matriz_origen.length-1;
				if(i>=matriz_origen.length) aux_i = 0;
				if(j<0) aux_j = matriz_origen.length-1;
				if(j>=matriz_origen.length) aux_j = 0;

				if(matriz_origen[aux_i][aux_j] == 1 && !(aux_i==a && aux_j==b)) cont++;
			}
		}

		return cont;
	}

	public static void paint() throws InterruptedException{
	    BufferedImage image=new BufferedImage(matriz_origen.length, matriz_origen[0].length, BufferedImage.TYPE_INT_RGB);
	    Color colorW = Color.WHITE;
 		Color colorB = Color.BLACK;

	    for (int i=0;i<matriz_origen.length ; i++){
	      	for(int j=0; j<matriz_origen[0].length; j++){
		        if(matriz_origen[i][j]==0){
		          	image.setRGB(i,j,colorW.getRGB());

		        }else{
		        	image.setRGB(i,j,colorB.getRGB());

		        }
	     	}
	    }
	    Graphics g = panel_grafico.getGraphics();
	    g.drawImage(image, 0, 0, panel_grafico.getHeight(),panel_grafico.getWidth(), panel_grafico);
  	}
}