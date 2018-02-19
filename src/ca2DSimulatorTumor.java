import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;


public class ca2DSimulatorTumor implements Runnable{
	private static int[][] matriz_p;
	private static int[][][] matriz;
	private static int p=0, q=1;
	private static double p_survive = 0.95;
	private static double p_die = 1-p_survive;
	private static double p_migration = 0.10;
	private static double p_prolif = 0.10;
	private static double p_quies = 1 - p_migration - p_prolif;
	private static int np = 1;
	private static String option = "Central";
	private static List<Integer> valores;
	private static AtomicInteger contUnos = new AtomicInteger();

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


	public ca2DSimulatorTumor(int inf, int sup, CyclicBarrier b){
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

	public static void setSurv(double p){
	 	p_survive=p;
	 	p_die = 1-p_survive;
	}

 	public static void setMigr(double p){ 
 		p_migration=p;
 		p_quies = 1 - p_migration - p_prolif;
 	}

  	public static void setProl(double p){ 
  		p_prolif=p;
  		p_quies = 1 - p_migration - p_prolif;
  	}

  	public static void setNP(int n){ 
  		np=n;
  	}

  	public static void setOption(String opt){
  		option=opt;
  	}

  	public static void setArraysValores(){
		valores = new ArrayList<Integer>();
	}

	public static List<Integer> getValores(){
		return valores;
	}

	public static void setAtomics(AtomicInteger c1){
		contUnos=c1;
	}


	public static void generacionInicial(){
		matriz = new int[dimension][dimension][2];
    	matriz_p = new int[dimension][dimension];

    	int medio = dimension/2;
	    
	    switch(option){
		    case "Central":
		    	matriz[medio][medio][p] = 1;
		    	matriz_p[medio][medio] = 1;

		    	contUnos.incrementAndGet();
		    break;

		    case "A":
		        matriz[medio][medio][p] = 1;
		        matriz[medio-1][medio][p] = 1;
		        matriz[medio-2][medio][p] = 1;
		        matriz[medio-3][medio][p] = 1;

		        matriz_p[medio][medio] = 1;
		        matriz_p[medio-1][medio] = 1;
		        matriz_p[medio-2][medio] = 1;
		        matriz_p[medio-3][medio] = 1;

		        contUnos.incrementAndGet();
		        contUnos.incrementAndGet();
		        contUnos.incrementAndGet();
		        contUnos.incrementAndGet();
		    break;

		    case "B":
		        matriz[medio][medio][p] = 1;
		        matriz[medio+2][medio-2][p] = 1;

		        matriz_p[medio][medio] = 1;
		        matriz_p[medio+2][medio-2] = 1;

		        contUnos.incrementAndGet();
		        contUnos.incrementAndGet();
		    break;

		    case "C":
		        matriz[medio][medio][p] = 1;
		        matriz_p[medio][medio] = 1;

		        contUnos.incrementAndGet();
		    break;

		    case "D":
		        matriz[medio][medio][p] = 1;
		        matriz_p[medio][medio] = 1;

		        contUnos.incrementAndGet();
		    break;
	    }

	    valores.add(contUnos.get());
	    contUnos.set(0);
		
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

				valores.add(contUnos.get());
				contUnos.set(0);

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
				matriz[i][j][q] = matriz[i][j][p];
			}
		}

		for(int i=0;i<dimension;i++){
			for(int j=linf;j<lsup;j++){
				computa(i,j);
			}
		}
	}

	private void computa(int i, int j){
		if(matriz[i][j][p]==1){
			double rr = random.nextDouble();
			if(rr<p_survive){
				int ph=0;
				boolean prolif = true;
				double rpo = random.nextDouble();
				double[] probs = probabilidades(i,j);
				int x=i;
				int y=j;

				//direccion movimiento
				if(0<=rpo && rpo<=probs[0]){
					x=i-1;
					y=j;
				}else if(probs[0]<rpo && rpo<=probs[0]+probs[1]){
					x=i+1;
					y=j;
				}else if(probs[0]+probs[1]<rpo && rpo<=probs[0]+probs[1]+probs[2]){
					x=i;
					y=j-1;
				}else if(probs[0]+probs[1]+probs[2]<rpo && rpo<=1){
					x=i;
					y=j+1;
				}

				
				while(ph!=np && prolif){
					double rrp = random.nextDouble();
					if(rrp>=p_prolif){
						prolif=false;
						double rrm = random.nextDouble();

						if(rrm<p_migration){
							matriz[i][j][q]=0;
							if(x>=0 && x<dimension && y>=0 && y<dimension){
								matriz[x][y][q]=1;

							}else{
								matriz[i][j][q]=1;
							}

						}else{
							matriz[i][j][q]=1;
						}

					}else{
						ph++;
					}
				}

				//proliferacion
				if(prolif){
					matriz[i][j][q]=1;
					if(!( x<0 || x>dimension-1 || y<0 || y>dimension-1 )){
						matriz[x][y][q]=1;
					}
				}

			}else{
				matriz[i][j][q]=0;
			}

		}
		if(matriz[i][j][q] == 1) contUnos.incrementAndGet();
		matriz_p[i][j]=matriz[i][j][q];

	}

	public double[] probabilidades(int x, int y){
	    double P1, P2, P3, P4, aux;

	    if(x==0){
	      if(y==0){
	        aux=4-matriz[x+1][y][p]-matriz[x][y+1][p];
	        P1=(1)/aux;
	        P2=(1-matriz[x+1][y][p])/aux;
	        P3=(1)/aux;
	        P4=(1-matriz[x][y+1][p])/aux;
	      }else{
	        if(y==dimension-1){
	          aux=4-matriz[x+1][y][p]-matriz[x][y-1][p];
	          P1=(1)/aux;
	          P2=(1-matriz[x+1][y][p])/aux;
	          P3=(1-matriz[x][y-1][p])/aux;
	          P4=(1)/aux;
	        }else{
	        aux=4-matriz[x+1][y][p]-matriz[x][y+1][p]-matriz[x][y-1][p];
	          P1=(1)/aux;
	          P2=(1-matriz[x+1][y][p])/aux;
	          P3=(1-matriz[x][y-1][p])/aux;
	          P4=(1-matriz[x][y+1][p])/aux;
	        }
	      }
	    }else{
	      if(x==dimension-1){
	        if(y==0){
	          aux=4-matriz[x-1][y][p]-matriz[x][y+1][p];
	          P1=(1-matriz[x-1][y][p])/aux;
	          P2=(1)/aux;
	          P3=(1)/aux;
	          P4=(1-matriz[x][y+1][p])/aux;
	        }else{
	          if(y==dimension-1){
	            aux=4-matriz[x-1][y][p]-matriz[x][y-1][p];
	            P1=(1-matriz[x-1][y][p])/aux;
	            P2=(1)/aux;
	            P3=(1-matriz[x][y-1][p])/aux;
	            P4=(1)/aux;
	          }else{
	            aux=4-matriz[x-1][y][p]-matriz[x][y+1][p]-matriz[x][y-1][p];
	            P1=(1-matriz[x-1][y][p])/aux;
	            P2=(1)/aux;
	            P3=(1-matriz[x][y-1][p])/aux;
	            P4=(1-matriz[x][y+1][p])/aux;
	          }
	        }
	      }else{
	        if(y==0){
	          aux=4-matriz[x+1][y][p]-matriz[x-1][y][p]-matriz[x][y+1][p];
	          P1=(1-matriz[x-1][y][p])/aux;
	          P2=(1-matriz[x+1][y][p])/aux;
	          P3=(1)/aux;
	          P4=(1-matriz[x][y+1][p])/aux;
	        }else{
	          if(y==dimension-1){
	            aux=4-matriz[x+1][y][p]-matriz[x-1][y][p]-matriz[x][y-1][p];
	            P1=(1-matriz[x-1][y][p])/aux;
	            P2=(1-matriz[x+1][y][p])/aux;
	            P3=(1-matriz[x][y-1][p])/aux;
	            P4=(1)/aux;
	          }else{
	            aux=4-matriz[x+1][y][p]-matriz[x-1][y][p]-matriz[x][y+1][p]-matriz[x][y-1][p];
	            if(aux!=0){
	              P1=(1-matriz[x-1][y][p])/aux;
	              P2=(1-matriz[x+1][y][p])/aux;
	              P3=(1-matriz[x][y-1][p])/aux;
	              P4=(1-matriz[x][y+1][p])/aux;
	            }else{P1=0.25;P2=0.25;P3=0.25;P4=0.25;}
	          }
	        }
	      }
	    }

	    double[] P = {P1,P2,P3,P4};

	    return P;
  	}

	public static void paint() throws InterruptedException{
	    BufferedImage image=new BufferedImage(matriz_p.length, matriz_p[0].length, BufferedImage.TYPE_INT_RGB);
	    Color colorW = Color.WHITE;
 		Color colorB = Color.BLACK;

	    for (int i=0;i<matriz_p.length ; i++){
	      	for(int j=0; j<matriz_p[0].length; j++){
		        if(matriz_p[i][j]==0){
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