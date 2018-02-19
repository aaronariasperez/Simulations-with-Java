
import javax.swing.*;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.nio.charset.Charset;

public class AutomataCifrado{
	private static JPanel panel_botonera, panel_configuracion, panel_textos;
	private static JTextPane text1;
	private static JTextArea text2;
	private static String frontera="nula";
	private static String clave="abc";
	private static int generaciones=0;
	private static int u=1; //rango de vencindad
	private static int k=2; //numero de estados
	private static int regla=2;
	private static Frame f = new Frame();
	private static FileDialog fd;
	private static int nCells;

	public static void initCifrado(JPanel pan_grafico, JPanel pan_bot, JPanel pan_con){
		panel_textos=pan_grafico;

		panel_botonera=pan_bot;
		panel_configuracion=pan_con;

		init_botonera();
		init_configuracion_vacio(); //se va autilizar este panel para pintar la grafica de poblacion
		init_textos_vacio();
	}

	private static void init_configuracion_vacio(){
		panel_configuracion.removeAll();
		panel_configuracion.repaint();
		panel_configuracion.revalidate();
	}

	private static void init_textos_vacio(){
		panel_textos.removeAll();
		panel_textos.repaint();

		panel_textos.setLayout(new BorderLayout());

		JTextPane texto1 = new JTextPane(); //editable
		JTextArea texto2 = new JTextArea(); //solo lectura
		texto2.setEditable(false);

		JSplitPane textos_split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, texto1, texto2);
		textos_split.setResizeWeight(0.5);

		panel_textos.add(textos_split, BorderLayout.CENTER);

		text1=texto1;
		text2=texto2;

		panel_textos.revalidate();
	}

	private static void init_botonera(){
		panel_botonera.removeAll();
		panel_botonera.repaint();
		
		panel_botonera.setBackground(new Color(190,190,190));

		JLabel text = new JLabel("Parametros de configuracion");
		panel_botonera.add(text);

		SpringLayout layout_inputs = new SpringLayout();
		panel_botonera.setLayout(layout_inputs);


		JLabel label1 = new JLabel("Clave:");
		JTextField input1 = new JTextField("abc", 12);
		panel_botonera.add(label1);
		panel_botonera.add(input1);
		
		JLabel adv = new JLabel("(Press Enter after write)");
		panel_botonera.add(adv);


		JButton fichero_button = new JButton("Cargar fichero");
		panel_botonera.add(fichero_button);

		JButton run_button = new JButton("RUN!");
		run_button.setBackground(new Color(255,86,49));
		run_button.setForeground(new Color(255,255,255));
		panel_botonera.add(run_button);


		layout_inputs.putConstraint(SpringLayout.NORTH, text, 5, SpringLayout.NORTH, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, text, 5, SpringLayout.WEST, panel_botonera);

		layout_inputs.putConstraint(SpringLayout.NORTH, label1, 5, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.NORTH, input1, 5, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.WEST, label1, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, input1, 5, SpringLayout.EAST, label1);

		layout_inputs.putConstraint(SpringLayout.WEST, fichero_button, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, fichero_button, 9, SpringLayout.SOUTH, label1);

		layout_inputs.putConstraint(SpringLayout.NORTH, run_button, 5, SpringLayout.SOUTH, fichero_button);
		layout_inputs.putConstraint(SpringLayout.WEST, run_button, 5, SpringLayout.WEST, panel_botonera);
		
		layout_inputs.putConstraint(SpringLayout.NORTH, adv, 5, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.WEST, adv, 2, SpringLayout.EAST, input1);


		input1.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent e) {
            	clave = input1.getText();
         	}
      	});


      	fichero_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				try {cargaFichero();}catch(Exception e){}
			}
		});


      	run_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				try{ iniciar_programa(); }catch(Exception e){}
			}
		});


		panel_botonera.revalidate();
	}

	private static void cargaFichero() throws Exception{
		text1.setText(null);
		fd = new FileDialog(f, "Choose a file", FileDialog.LOAD);
		fd.setVisible(true);
		String filename = fd.getFile();
		String dir = fd.getDirectory();

		if (filename == null)
		  JOptionPane.showMessageDialog(null,"No se ha seleccionado ningun fichero.");
		else{
			Path path = Paths.get(dir+filename);
			List<String> aux=null;
			try{ aux = Files.readAllLines(path, Charset.defaultCharset());  }catch(Exception e){}
			
			String all="";
			for(int i=0;i<aux.size();i++){
				if(i!= aux.size()-1) all=all+aux.get(i)+"\n";
				else all=all+aux.get(i);
			}
			text1.replaceSelection(all);
		}  
	}

	private static int[] getTextoBinario(){
		String string = "";
		String txt = text1.getText();
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

	private static void iniciar_programa() throws Exception{
		int[] texto_binario = getTextoBinario();
		text2.setText(null);
		
		ca1DSimulatorCifrado.setK_U(k,u);
		ca1DSimulatorCifrado.setFrontera(frontera);
		ca1DSimulatorCifrado.setRegla(regla);
		ca1DSimulatorCifrado.setClave(clave);
		ca1DSimulatorCifrado.setArraysValores(texto_binario.length);
		ca1DSimulatorCifrado.setGeneraciones(texto_binario.length);

		ca1DSimulatorCifrado.generacionInicial();

		try{ ca1DSimulatorCifrado.traduceRegla(); }catch(Exception e){JOptionPane.showMessageDialog(null,"Error en la regla"); throw new Exception();}

		nCells = ca1DSimulatorCifrado.getCells();
		int nCores = Runtime.getRuntime().availableProcessors();
		CyclicBarrier bar = new CyclicBarrier(nCores);
		ExecutorService pool = Executors.newCachedThreadPool();
		int ventana = nCells/nCores;
		int resto = nCells%nCores;
		int inf=0;
		int sup=ventana;


		for(int i=0;i<nCores;i++){
			if(i==nCores-1){
				pool.execute(new ca1DSimulatorCifrado(inf,sup+resto,bar));
			}else{
				pool.execute(new ca1DSimulatorCifrado(inf,sup,bar));
				inf=sup;
				sup+=ventana;
			}
		}

		pool.shutdown();
		try{ pool.awaitTermination(1,TimeUnit.DAYS); }catch(Exception e){}	

		int[] valores_celulaTemp = ca1DSimulatorCifrado.getValoresTemp();

		int[] valores_xor = calculaXOR(texto_binario,valores_celulaTemp);

		String texto_convertido = binarioToText(valores_xor);

		text2.replaceSelection(texto_convertido);

		System.out.println("Finalizado");

	}

	private static int opXOR(int a, int b){
		if(a==b){
			return 0;
		}else{
			return 1;
		}
	}

	private static int[] calculaXOR(int[] texto_binario, int[] valores_celulaTemp){
		int[] res = new int[texto_binario.length];

		for(int i=0;i<texto_binario.length;i++){
			res[i] = opXOR(texto_binario[i], valores_celulaTemp[i]);
		}

		return res;
	}

	private static String binarioToText(int[] array){
		String aux="";

		for(int i=0;i<array.length;i++){
			aux+=String.valueOf(array[i]);
		}

		String res="";
		int a=0;
		for (int i=0; i<aux.length()/8;i++) {
	        a = Integer.parseInt(aux.substring(8*i,(i+1)*8),2);
	        res += (char)(a);

    	}

		return res;
	}
		
}