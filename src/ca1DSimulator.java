

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;


public class ca1DSimulator implements Runnable, ca1DSim{
	private static int[] automata_origen;
	private static int[] automata_siguiente;
	private static int[] tabla_traduccion;
	private static int generaciones;
	private static int ncells;
	private static int k;
	private static int u;
	private static PanelGrafico panel_grafico;
	private static String frontera;
	private static int regla;
	private static AtomicInteger[] contadores;
	private static Semaphore sem = new Semaphore(1);
	private static boolean actualizado = false;
	private static boolean actualizado2 = false;
	private static int[][] valores;
	private static int[] valores_generacion;
	private static int[] valores_hamming;
	private static AtomicInteger hamm_aux = new AtomicInteger();
	private static int[] valores_celulaTemp;
	private static int celula_temporal;

	private CyclicBarrier bar;
	private int linf;
	private int lsup;

	public ca1DSimulator(int inf, int sup, CyclicBarrier b){
		this.linf=inf;
		this.lsup=sup;
		this.bar=b;
	}

	public static void setPanelGrafico(PanelGrafico panel){
		panel_grafico=panel;
	}

	public static void setArraysValores(int n){
		valores = new int[k][n+1];
		valores_generacion = new int[n+1];
		valores_hamming = new int[n];
		valores_celulaTemp = new int[n+1];
	}

	public static int[][] getValores(){
		return valores;
	}

	public static int[] getValoresGeneracion(){
		return valores_generacion;
	}

	public static int[] getValoresHamming(){
		return valores_hamming;
	}

	public static int[] getValoresTemp(){
		return valores_celulaTemp;
	}

	public static void setCelulaTemporal(int c){
		celula_temporal=c;
	}

	public static void setHamm(){
		hamm_aux.set(0);
	}

	public static void setAtomics(AtomicInteger[] c){
		contadores = c;
	}

	public static void setFrontera(String f){
		frontera=f;
	}

	public static void setGeneraciones(int g){
		generaciones=g;
	}

	public static void setRegla(int r){
		regla=r;
	}

	public static void setK_U(int a, int b){
		k=a;
		u=b;
	}

	public static void generacionInicialCentro(int n){
		ncells=n;
		automata_origen = new int[ncells];
		automata_siguiente = new int[ncells];

		automata_origen[n/2]=1;

		valores_generacion[0]=0;
		for(int i=0;i<k;i++){
			valores[i][0] = contadores[i].get();
			contadores[i].set(0);
		}

		panel_grafico.actualizaPanelGrafico(automata_origen,0);
	}

	public static void generacionInicial(int n, String eleccion){
		ncells = n;
		automata_origen = new int[ncells];
		automata_siguiente = new int[ncells];
		
		double seed=1;
		double aux=0.0;

		if(eleccion.equals("combinada")){
			double[] ptos = randomGenerator.fcombinada(seed, ncells);
			for(int i=0;i<ncells;i++){
				automata_origen[i] = (int)Math.floor((ptos[i]/32362)*k);
				contadores[(int)Math.floor((ptos[i]/32362)*k)].incrementAndGet();
			}

		}else{
			for(int i=0;i<ncells;i++){
				switch(eleccion){
					case "26a":
					seed=randomGenerator.f26a(seed);
					aux=seed/Math.pow(2,5);
					automata_origen[i] = (int)Math.floor(aux*k);
					contadores[(int)Math.floor(aux*k)].incrementAndGet();
					break;

					case "26b":
					seed=randomGenerator.f26b(seed);
					aux=seed/Math.pow(2,5);
					automata_origen[i] = (int)Math.floor(aux*k);
					contadores[(int)Math.floor(aux*k)].incrementAndGet();
					break;

					case "26_2":
					seed=randomGenerator.f26_2(seed);
					aux=seed/31;
					automata_origen[i] = (int)Math.floor(aux*k);
					contadores[(int)Math.floor(aux*k)].incrementAndGet();
					break;

					case "26_3":
					seed=randomGenerator.f26_3(seed);
					aux=seed/(Math.pow(2,31)-1);
					automata_origen[i] = (int)Math.floor(aux*k);
					contadores[(int)Math.floor(aux*k)].incrementAndGet();
					break;

					case "fishman":
					seed=randomGenerator.fFishman(seed);
					aux=seed/(Math.pow(2,31)-1);
					automata_origen[i] = (int)Math.floor(aux*k);
					contadores[(int)Math.floor(aux*k)].incrementAndGet();
					break;

					case "moore":
					seed=randomGenerator.fMoore(seed);
					aux=seed/(Math.pow(2,31)-1);
					automata_origen[i] = (int)Math.floor(aux*k);
					contadores[(int)Math.floor(aux*k)].incrementAndGet();
					break;

					case "randu":
					seed=randomGenerator.fRandu(seed);
					aux=seed/Math.pow(2,31);
					automata_origen[i] = (int)Math.floor(aux*k);
					contadores[(int)Math.floor(aux*k)].incrementAndGet();
					break;
				}
			}
		}


		valores_generacion[0]=0;
		for(int i=0;i<k;i++){
			valores[i][0] = contadores[i].get();
			contadores[i].set(0);
		}

		valores_celulaTemp[0]=automata_origen[celula_temporal];

		panel_grafico.actualizaPanelGrafico(automata_origen,0);
	}

	public static void traduceRegla() throws Exception{
		int tam = (2*u+1)*(k-1)+1;
		tabla_traduccion = new int[tam];
		if(regla > (int)Math.pow(k,((2*u+1)*(k-1)+1)) -1) throw new Exception();
		for(int i=0;i<tam;i++) tabla_traduccion[i]=0;

		int aux = regla;
		int i=0;
		while(aux != 0){
			tabla_traduccion[i] = aux%k;
			aux=aux/k;

			i++;
		}

		
	}

	public void run(){
		for(int g=0;g<generaciones;g++){
			if(actualizado){actualizado=false;}
			if(actualizado2){actualizado2=false;}

			nextGen();
			try{ bar.await(); }catch(Exception e){}
			
			calculaHamming();
			try{ bar.await(); }catch(Exception e){}

			try{ sem.acquire();}catch(Exception e){}
			if(!actualizado2){
				valores_hamming[g]=hamm_aux.get();
				hamm_aux.set(0);

				valores_celulaTemp[g+1]=automata_siguiente[celula_temporal];

				actualizado2=true;
			} 
			sem.release();

			copiarAutomata();
			try{ bar.await(); }catch(Exception e){}

			try{ sem.acquire();}catch(Exception e){}
			if(!actualizado){
				panel_grafico.actualizaPanelGrafico(automata_origen,g+1);
				
				panel_grafico.paintComponent(panel_grafico.getGraphics());
				panel_grafico.revalidate();

				valores_generacion[g+1]=g+1;

				for(int z=0;z<k;z++){
					valores[z][g+1] = contadores[z].get();
					contadores[z].set(0);
				}

				actualizado=true;
			}
			sem.release();

			try{ bar.await(); }catch(Exception e){}
		}
	}

	private void copiarAutomata(){
		for(int i=linf;i<lsup;i++){
			automata_origen[i] = automata_siguiente[i];
		}
	}

	public void nextGen(){
		for(int i=linf;i<lsup;i++){
			computa(i);
		}
	}

	private void computa(int i){
		int tam = (2*u +1);
		int bloque[] = new int[tam];
		int j=0;
		int diff=0;

		for(int a=i-u;a<i+u+1;a++){
			if(a<0){
				if(frontera.equals("nula")){
					bloque[j] = 0;
				}else if(frontera.equals("cilindrica")){
					diff = -a;
					bloque[j] = automata_origen[ncells-diff];
				}

			}else if(a>=automata_origen.length){
				if(frontera.equals("nula")){
					bloque[j] = 0;
				}else if(frontera.equals("cilindrica")){
					diff = a-(ncells-1);
					bloque[j] = automata_origen[diff-1];
				}

			}else{
				bloque[j] = automata_origen[a];
			}

			j++;
		}

		int indice = baseKtoDecimal(bloque);

		int valor = tabla_traduccion[indice];

		automata_siguiente[i] = valor;

		contadores[valor].incrementAndGet();
	}

	public static int baseKtoDecimal(int[] bloque){
		int res=0;
		int tam = bloque.length;

		for(int i=0;i<tam;i++){
			res += bloque[i];//*Math.pow(k,tam-i-1);
		}

		return res;
	}


	public static void pintaTest(){
		System.out.print("Final: ");
		for(int i=0;i<ncells;i++){
			System.out.print(automata_siguiente[i]+" ");
		}
		System.out.println();
	}

	private void calculaHamming(){
		for(int i=linf;i<lsup;i++){
			if(automata_origen[i] != automata_siguiente[i]){
				hamm_aux.incrementAndGet();
			}
		}
	}

	private void calculaEntropia(){

	}

	public void caComputation(int nGen){
		//dummy
	}
}
