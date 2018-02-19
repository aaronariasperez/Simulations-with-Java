import java.util.List;
import java.util.ArrayList;

public class interpreteUrm{
	private static String programa, inputs;
	private static String instruccion_actual;
	private static int cont;
	private static List<String> instrucciones;
	private static int[] registros;
	private static boolean error=false;
	private static boolean jumped=false;

	public static void setDatos(String pr, String in){
		programa = pr;
		inputs = in;
		cont = 0;
		instruccion_actual="";
		instrucciones = new ArrayList<String>();
		registros = new int[1000]; //limite del URM
		error=false;
	}

	public static int computa(){
		leer_instrucciones();
		leer_inputs();
		
		error=false;
		while(cont!=-1 && cont<instrucciones.size()){
			instruccion_actual = instrucciones.get(cont);

			//computar

			switch(instruccion_actual.charAt(0)){
				case 'J': jump(instruccion_actual);
				break;

				case 'S': suma(instruccion_actual);
				break;

				case 'Z': zero(instruccion_actual);
				break;

				case 'T': transfer(instruccion_actual);
				break;

				default: error=true;
				break;
			}

			if(error) break;

			if(!jumped) cont++; //para no avanzar una instruccion de más cuando se salta
			else jumped=false;

		}

		if(error) return -1;

		return registros[0];
	}

	private static void leer_instrucciones(){
		char aux;
		String ins="";
		for(int i=0;i<programa.length();i++){
			if(programa.charAt(i)=='\n'){
				instrucciones.add(ins);
				ins="";
			}else{
				ins+=programa.charAt(i);
			}
		}

		instrucciones.add(ins);
	}

	private static void leer_inputs(){
		String aux="";
		int k=0;
		for(int i=0; i<inputs.length();i++){
			Character c = inputs.charAt(i);
			if(!c.equals(',')){
				aux+=c;	
			}else{
				registros[k] = Integer.parseInt(aux);
				aux="";
				k++;
			}
		}

		registros[k]=Integer.parseInt(aux);
	}

	//*****Operadores del URM*****

	private static void suma(String ins){
		String aux="";
		for(int i=2; i<ins.length();i++){
			Character c = ins.charAt(i);
			if(!c.equals(')')) aux+=c;
			else break;
		}

		int index = Integer.parseInt(aux) - 1;

		registros[index]++;
	}

	private static void zero(String ins){
		String aux="";
		for(int i=2; i<ins.length();i++){
			Character c = ins.charAt(i);
			if(!c.equals(')')) aux+=c;
			else break;
		}

		int index = Integer.parseInt(aux) - 1;

		registros[index]=0;
	}

	private static void transfer(String ins){
		String aux1="";
		String aux2="";
		int val1=0;
		boolean first_lleno = false;

		for(int i=2; i<ins.length();i++){
			Character c = ins.charAt(i);
			if(!first_lleno){
				if(!c.equals(',')){
					aux1+=c;	
				}else{
					first_lleno=true;
					val1 = registros[Integer.parseInt(aux1)-1];
				}

			}else{
				if(!c.equals(')')) aux2+=c;
				else break;
			}
		}

		registros[Integer.parseInt(aux2)-1] = val1;
	}

	private static void jump(String ins){
		String aux1="";
		String aux2="";
		String aux3="";
		int val1=0;
		int val2=0;
		boolean first_lleno = false;
		boolean second_lleno = false;

		for(int i=2; i<ins.length();i++){
			Character c = ins.charAt(i);
			if(!first_lleno){
				if(!c.equals(',')){
					aux1+=c;	
				}else{
					first_lleno=true;
					val1 = registros[Integer.parseInt(aux1)-1];
				}

			}else if(!second_lleno){
				if(!c.equals(',')){
					aux2+=c;	
				}else{
					second_lleno=true;
					val2 = registros[Integer.parseInt(aux2)-1];
				}
			
			}else{
				if(!c.equals(')')) aux3+=c;
				else break;
			}
		}

		if(val1 == val2){
			int index = Integer.parseInt(aux3)-1;
			if(index<0 || index>=instrucciones.size()){
				cont = -1;
				jumped=true;
			}else{
				cont = index;
				jumped=true;
			}
		}
	}
}