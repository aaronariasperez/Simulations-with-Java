
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import java.math.BigInteger;
import java.util.Arrays;



public class ca1DSimulatorCifrado implements Runnable{
	private static int[] automata_origen;
	private static int[] automata_siguiente;
	private static int[] tabla_traduccion;
	private static int generaciones;
	private static int ncells;
	private static int k;
	private static int u;
	private static String frontera;
	private static int regla;
	private static int[] valores_celulaTemp;
	private static int celula_temporal;
	private static Semaphore sem = new Semaphore(1);
	private static boolean actualizado = false;
	private static String clave;


	private CyclicBarrier bar;
	private int linf;
	private int lsup;

	public ca1DSimulatorCifrado(int inf, int sup, CyclicBarrier b){
		this.linf=inf;
		this.lsup=sup;
		this.bar=b;
	}

	public static void setArraysValores(int n){
		valores_celulaTemp = new int[n];
	}

	public static int[] getValoresTemp(){
		return valores_celulaTemp;
	}

	public static void setCelulaTemporal(int c){
		celula_temporal=c;
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

	public static void setClave(String cl){
		clave=cl;
	}

	public static int getCells(){
		return ncells;
	}

	private static int[] traduceClave2(){
		String binary = "";
		byte[] bytes = clave.getBytes();
		String aux=null;
		for (byte b : bytes) {
            aux = Integer.toBinaryString(b); 
			if ( aux.length() < 8 )
			 	aux = "0" + aux;

			binary+=aux;
        }


		int res[] = new int[binary.length()];

		for(int i=0;i<binary.length();i++){
			res[i] =  Character.getNumericValue(binary.charAt(i));
		}

		setCelulaTemporal(binary.length()/2);

		return res;
	}

	private static int[] traduceClave(){
		String string = "";
		String txt = clave;
		int[] unicodes = new int[txt.length()];
		
		for(int i=0;i<txt.length();i++){
			unicodes[i] = (int)txt.charAt(i);
		}

		for(int a:unicodes){
			String aux=Integer.toBinaryString(a);
			while ( aux.length() < 8 )
			 	aux = "0" + aux;

			string+=aux;
			
		}

		int[] res = new int[string.length()];

		for(int i=0;i<res.length;i++){
			res[i] = Character.getNumericValue(string.charAt(i));
		}

		return res;
	}

	public static void traduceRegla() throws Exception{
		int tam = (2*u+1)*(k-1)+1;
		tabla_traduccion = new int[tam];
		if(regla > (int)Math.pow(k,((2*u+1)*(k-1)+1)) -1) throw new Exception();
		for(int i=0;i<tam;i++) tabla_traduccion[i]=0;

		int aux = regla;
		int i=0;//tabla_traduccion.length-1;
		while(aux != 0){
			tabla_traduccion[i] = aux%k;
			aux=aux/k;

			i++;//--;
		}

		
	}

	public static void generacionInicial(){
		int array_binario[] = traduceClave();
		int n = array_binario.length;
		ncells = n;
		celula_temporal = ncells/2;

		automata_origen = new int[ncells];
		automata_siguiente = new int[ncells];
		
		for(int i=0;i<ncells;i++){
			automata_origen[i] = array_binario[i];
		}

		valores_celulaTemp[0]=automata_origen[celula_temporal];

	}

	public void run(){
		for(int g=0;g<generaciones-1;g++){
			if(actualizado){actualizado=false;}

			nextGen();
			try{ bar.await(); }catch(Exception e){}

			try{ sem.acquire();}catch(Exception e){}

			if(!actualizado){
				valores_celulaTemp[g+1]=automata_siguiente[celula_temporal];
				actualizado=true;

			} 

			sem.release();

			copiarAutomata();
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
	}

	public static int baseKtoDecimal(int[] bloque){
		int res=0;
		int tam = bloque.length;

		for(int i=0;i<tam;i++){
			res += bloque[i];//*Math.pow(k,tam-i-1);
		}

		return res;
	}
}
