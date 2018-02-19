
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;

public class Automata{
	private static JPanel panel_botonera, panel_configuracion;
	private static PanelGrafico panel_grafico;
	private static String eleccion="randu";
	private static String frontera="nula";
	private static int generaciones=700;
	private static int u=2; //rango de vencindad
	private static int k=2; //numero de estados
	private static int nCells=700;
	private static int regla=2;
	private static AtomicInteger[] contadores;
	private static int celula_temporal=0;


	public static void initAutomata(JSplitPane pan_general, JPanel pan_bot, JPanel pan_con){
		panel_grafico=new PanelGrafico();
		pan_general.setLeftComponent(panel_grafico);

		panel_botonera=pan_bot;
		panel_configuracion=pan_con;

		init_botonera();
		init_configuracion_vacio(); //se va autilizar este panel para pintar la grafica de poblacion
		init_grafico_vacio();
	}

	private static void init_configuracion_vacio(){
		panel_configuracion.removeAll();
		panel_configuracion.repaint();
		
		panel_configuracion.setBackground(new Color(190,190,190));
		
		SpringLayout layout_inputs = new SpringLayout();
		panel_configuracion.setLayout(layout_inputs);
		
		JLabel text = new JLabel("Algunas configuraciones interesantes:");
		panel_configuracion.add(text);
		
		JLabel text2 = new JLabel("regla=52, k=2, u=2");
		panel_configuracion.add(text2);
		
		JLabel text3 = new JLabel("regla=357, k=3, u=1");
		panel_configuracion.add(text3);
		
		JLabel text4 = new JLabel("regla=342, k=3, u=1");
		panel_configuracion.add(text4);
		
		JLabel text5 = new JLabel("regla=114, k=2, u=3");
		panel_configuracion.add(text5);
		
		JLabel text6 = new JLabel("regla=120, k=2, u=3");
		panel_configuracion.add(text6);
		
		JLabel text7 = new JLabel("regla=190, k=5, u=1");
		panel_configuracion.add(text7);
		
		
		layout_inputs.putConstraint(SpringLayout.NORTH, text, 5, SpringLayout.NORTH, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, text, 5, SpringLayout.WEST, panel_botonera);
		
		layout_inputs.putConstraint(SpringLayout.NORTH, text2, 2, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.WEST, text2, 5, SpringLayout.WEST, panel_botonera);
		
		layout_inputs.putConstraint(SpringLayout.NORTH, text3, 2, SpringLayout.SOUTH, text2);
		layout_inputs.putConstraint(SpringLayout.WEST, text3, 5, SpringLayout.WEST, panel_botonera);
		
		layout_inputs.putConstraint(SpringLayout.NORTH, text4, 2, SpringLayout.SOUTH, text3);
		layout_inputs.putConstraint(SpringLayout.WEST, text4, 5, SpringLayout.WEST, panel_botonera);
		
		layout_inputs.putConstraint(SpringLayout.NORTH, text5, 2, SpringLayout.SOUTH, text4);
		layout_inputs.putConstraint(SpringLayout.WEST, text5, 5, SpringLayout.WEST, panel_botonera);
		
		layout_inputs.putConstraint(SpringLayout.NORTH, text6, 2, SpringLayout.SOUTH, text5);
		layout_inputs.putConstraint(SpringLayout.WEST, text6, 5, SpringLayout.WEST, panel_botonera);
		
		layout_inputs.putConstraint(SpringLayout.NORTH, text7, 2, SpringLayout.SOUTH, text6);
		layout_inputs.putConstraint(SpringLayout.WEST, text7, 5, SpringLayout.WEST, panel_botonera);
		
		
		panel_configuracion.revalidate();
	}

	private static void init_grafico_vacio(){
		panel_grafico.removeAll();
		panel_grafico.repaint();
		panel_grafico.setBackground(new Color(0,0,0));
		panel_grafico.revalidate();
	}

	private static void init_botonera(){
		panel_botonera.removeAll();
		panel_botonera.repaint();
		
		panel_botonera.setBackground(new Color(190,190,190));

		JLabel text = new JLabel("Parametros de configuracion");
		panel_botonera.add(text);

		SpringLayout layout_inputs = new SpringLayout();
		panel_botonera.setLayout(layout_inputs);

		JLabel label1 = new JLabel("Generaciones:");
		JSpinner input1 = new JSpinner();
		input1.setValue(700);
		panel_botonera.add(label1);
		panel_botonera.add(input1);
		
		JLabel label2 = new JLabel("Numero celulas:");
		JSpinner input2 = new JSpinner();
		input2.setValue(700);
		panel_botonera.add(label2);
		panel_botonera.add(input2);

		JLabel label3 = new JLabel("Regla:");
		JSpinner input3 = new JSpinner();
		input3.setValue(2);
		panel_botonera.add(label3);
		panel_botonera.add(input3);

		JLabel label4 = new JLabel("Estados(k):");
		JSpinner input4 = new JSpinner();
		input4.setValue(2);
		panel_botonera.add(label4);
		panel_botonera.add(input4);

		JLabel label5 = new JLabel("Vecindad(u):");
		JSpinner input5 = new JSpinner();
		input5.setValue(2);
		panel_botonera.add(label5);
		panel_botonera.add(input5);

		JComboBox<String> box_generador = new JComboBox<String>();
		box_generador.setModel(new DefaultComboBoxModel<String>(new String[] {"randu", "26a", "26b", "26_2", "26_3", "combinada", "fishman", "moore"}));
		panel_botonera.add(box_generador);

		JComboBox<String> box_frontera = new JComboBox<String>();
		box_frontera.setModel(new DefaultComboBoxModel<String>(new String[] {"nula","cilindrica"}));
		panel_botonera.add(box_frontera);

		JButton run_button = new JButton("RUN!");
		run_button.setBackground(new Color(255,86,49));
		run_button.setForeground(new Color(255,255,255));
		panel_botonera.add(run_button);

		JButton poblacion_button = new JButton("Poblacion");
		panel_botonera.add(poblacion_button);

		JButton hammingGrafica_button = new JButton("Hamming G.");
		panel_botonera.add(hammingGrafica_button);

		JButton hammingMedia_button = new JButton("Hamming M.");
		panel_botonera.add(hammingMedia_button);

		JButton entropiaGrafica_button = new JButton("Entropia G.");
		panel_botonera.add(entropiaGrafica_button);

		JButton entropiaMedia_button = new JButton("Entropia M.");
		panel_botonera.add(entropiaMedia_button);

		JLabel label_entropiaTemporal = new JLabel("Celula EntropiaTemporal:");
		JSpinner input_entropiaTemporal = new JSpinner();
		input_entropiaTemporal.setValue(0);
		panel_botonera.add(label_entropiaTemporal);
		panel_botonera.add(input_entropiaTemporal);

		JButton entropiaTemp_button = new JButton("Entropia Temporal");
		panel_botonera.add(entropiaTemp_button);


		layout_inputs.putConstraint(SpringLayout.NORTH, text, 5, SpringLayout.NORTH, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, text, 5, SpringLayout.WEST, panel_botonera);

		layout_inputs.putConstraint(SpringLayout.NORTH, label1, 5, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.NORTH, input1, 5, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.WEST, label1, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, input1, 5, SpringLayout.EAST, label1);
		
		layout_inputs.putConstraint(SpringLayout.WEST, label2, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, input2, 5, SpringLayout.EAST, label2);
		layout_inputs.putConstraint(SpringLayout.NORTH, label2, 9, SpringLayout.SOUTH, label1);
		layout_inputs.putConstraint(SpringLayout.NORTH, input2, 5, SpringLayout.SOUTH, input1);

		layout_inputs.putConstraint(SpringLayout.WEST, label3, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, input3, 5, SpringLayout.EAST, label3);
		layout_inputs.putConstraint(SpringLayout.NORTH, label3, 9, SpringLayout.SOUTH, label2);
		layout_inputs.putConstraint(SpringLayout.NORTH, input3, 5, SpringLayout.SOUTH, input2);

		layout_inputs.putConstraint(SpringLayout.WEST, label4, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, input4, 5, SpringLayout.EAST, label4);
		layout_inputs.putConstraint(SpringLayout.NORTH, label4, 9, SpringLayout.SOUTH, label3);
		layout_inputs.putConstraint(SpringLayout.NORTH, input4, 5, SpringLayout.SOUTH, input3);

		layout_inputs.putConstraint(SpringLayout.WEST, label5, 5, SpringLayout.EAST, input4);
		layout_inputs.putConstraint(SpringLayout.NORTH, label5, 9, SpringLayout.SOUTH, label3);

		layout_inputs.putConstraint(SpringLayout.WEST, input5, 5, SpringLayout.EAST, label5);
		layout_inputs.putConstraint(SpringLayout.NORTH, input5, 9, SpringLayout.SOUTH, label3);

		layout_inputs.putConstraint(SpringLayout.WEST, box_generador, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, box_generador, 9, SpringLayout.SOUTH, label4);
		layout_inputs.putConstraint(SpringLayout.NORTH, box_frontera, 5, SpringLayout.SOUTH, input4);
		layout_inputs.putConstraint(SpringLayout.WEST, box_frontera, 5, SpringLayout.EAST, box_generador);

		layout_inputs.putConstraint(SpringLayout.NORTH, poblacion_button, 5, SpringLayout.SOUTH, box_generador);
		layout_inputs.putConstraint(SpringLayout.WEST, poblacion_button, 5, SpringLayout.WEST, panel_botonera);

		layout_inputs.putConstraint(SpringLayout.NORTH, run_button, 5, SpringLayout.SOUTH, poblacion_button);
		layout_inputs.putConstraint(SpringLayout.WEST, run_button, 5, SpringLayout.WEST, panel_botonera);

		layout_inputs.putConstraint(SpringLayout.NORTH, hammingGrafica_button, 5, SpringLayout.NORTH, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, hammingGrafica_button, 40, SpringLayout.EAST, text);
		
		layout_inputs.putConstraint(SpringLayout.NORTH, hammingMedia_button, 5, SpringLayout.NORTH, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, hammingMedia_button, 5, SpringLayout.EAST, hammingGrafica_button);

		layout_inputs.putConstraint(SpringLayout.NORTH, entropiaGrafica_button, 5, SpringLayout.SOUTH, hammingGrafica_button);
		layout_inputs.putConstraint(SpringLayout.WEST, entropiaGrafica_button, 40, SpringLayout.EAST, text);
		
		layout_inputs.putConstraint(SpringLayout.NORTH, entropiaMedia_button, 5, SpringLayout.SOUTH, hammingGrafica_button);
		layout_inputs.putConstraint(SpringLayout.WEST, entropiaMedia_button, 5, SpringLayout.EAST, entropiaGrafica_button);

		layout_inputs.putConstraint(SpringLayout.NORTH, label_entropiaTemporal, 8, SpringLayout.SOUTH, entropiaGrafica_button);
		layout_inputs.putConstraint(SpringLayout.WEST, label_entropiaTemporal, 40, SpringLayout.EAST, text);

		layout_inputs.putConstraint(SpringLayout.NORTH, input_entropiaTemporal, 5, SpringLayout.SOUTH, entropiaGrafica_button);
		layout_inputs.putConstraint(SpringLayout.WEST, input_entropiaTemporal, 5, SpringLayout.EAST, label_entropiaTemporal);

		layout_inputs.putConstraint(SpringLayout.NORTH, entropiaTemp_button, 5, SpringLayout.SOUTH, label_entropiaTemporal);
		layout_inputs.putConstraint(SpringLayout.WEST, entropiaTemp_button, 40, SpringLayout.EAST, text);

		
		input1.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
     			generaciones = (Integer)input1.getValue();
      		}
    	});
		
		input2.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
     			nCells = (Integer)input2.getValue();
      		}
    	});
		
		input3.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
     			regla = (Integer)input3.getValue();
      		}
    	});
		
		input4.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		k = (Integer)input4.getValue();
      		}
    	});
		
		input5.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		u = (Integer)input5.getValue();
      		}
    	});


		box_generador.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent e) {
            	eleccion = box_generador.getSelectedItem().toString();
         	}
      	});


      	box_frontera.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent e) {
            	frontera = box_frontera.getSelectedItem().toString();
         	}
      	});


      	run_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				panel_grafico.setClearable(true);
				panel_grafico.paintComponent(panel_grafico.getGraphics());
				panel_grafico.revalidate();
//				panel_configuracion.removeAll();
//				panel_configuracion.repaint();
//				panel_configuracion.revalidate();
				try{ iniciar_programa(); }catch(Exception e){}
			}
		});


		poblacion_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				panel_configuracion.revalidate();
				inicia_poblacion();
			}
		});


		hammingGrafica_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				panel_configuracion.revalidate();
				inicia_hammingGrafica();
			}
		});


		hammingMedia_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				inicia_hammingMedia();
			}
		});


		entropiaGrafica_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				panel_configuracion.revalidate();
				inicia_entropiaGrafica();
			}
		});


		entropiaMedia_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				inicia_entropiaMedia();
			}
		});
		
		input_entropiaTemporal.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
     			celula_temporal = (Integer)input_entropiaTemporal.getValue();
      		}
    	});


      	entropiaTemp_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				inicia_entropiaTemporal();
			}
		});


		panel_botonera.revalidate();
	}

	private static void reiniciaContadores(){
		contadores = new AtomicInteger[k];
		for(int i=0;i<k;i++){
			contadores[i] = new AtomicInteger(0);
		}
	}

	private static void iniciar_programa() throws Exception{
		panel_grafico.setClearable(false);

		int nCores = Runtime.getRuntime().availableProcessors();
		CyclicBarrier bar = new CyclicBarrier(nCores);
		ExecutorService pool = Executors.newCachedThreadPool();
		int ventana = nCells/nCores;
		int resto = nCells%nCores;
		int inf=0;
		int sup=ventana;

		ca1DSimulator.setK_U(k,u);
		ca1DSimulator.setPanelGrafico(panel_grafico);
		ca1DSimulator.setFrontera(frontera);
		ca1DSimulator.setGeneraciones(generaciones);
		ca1DSimulator.setRegla(regla);
		ca1DSimulator.setArraysValores(generaciones);
		ca1DSimulator.setHamm();
		ca1DSimulator.setCelulaTemporal(celula_temporal);

		reiniciaContadores();
		ca1DSimulator.setAtomics(contadores);



		try{ ca1DSimulator.traduceRegla(); }catch(Exception e){JOptionPane.showMessageDialog(null,"Error en la regla"); throw new Exception();}
		
		//ca1DSimulator.generacionInicialCentro(nCells);
		ca1DSimulator.generacionInicial(nCells,eleccion);

		
		//panel_grafico.paintComponent(panel_grafico.getGraphics());
		//panel_grafico.revalidate();

		for(int i=0;i<nCores;i++){
			if(i==nCores-1){
				pool.execute(new ca1DSimulator(inf,sup+resto,bar));
			}else{
				pool.execute(new ca1DSimulator(inf,sup,bar));
				inf=sup;
				sup+=ventana;
			}
		}

		pool.shutdown();
		try{ pool.awaitTermination(1,TimeUnit.DAYS); }catch(Exception e){}	

		System.out.println("Finalizado");
		
	}

	private static void inicia_poblacion(){
		XYSeriesCollection datos = new XYSeriesCollection();
		JFreeChart grafica = ChartFactory.createXYLineChart("Poblacion","generaciones","cantidad",datos,PlotOrientation.VERTICAL,true,true,true);
		
		int[] datos_gener = ca1DSimulator.getValoresGeneracion();
		int[][] datos_valores = ca1DSimulator.getValores();
		XYSeries aux;
		for(int i=0;i<k;i++){
			aux = new XYSeries(String.valueOf(i));

			for(int j=0;j<generaciones;j++){
				aux.add(datos_gener[j],datos_valores[i][j]);
			}

			datos.addSeries(aux);
		}

		panel_configuracion.removeAll();
		panel_configuracion.add(new ChartPanel(grafica));
	}

	private static void inicia_hammingGrafica(){
		XYSeriesCollection datos = new XYSeriesCollection();
		JFreeChart grafica = ChartFactory.createXYLineChart("Distancia Hamming","generaciones","distancia",datos,PlotOrientation.VERTICAL,true,true,true);
		
		int[] datos_valores = ca1DSimulator.getValoresHamming();
		XYSeries aux = new XYSeries("Hamming");
		
		for(int i=0;i<generaciones;i++){
			aux.add(i+1,datos_valores[i]);
		}

		datos.addSeries(aux);

		panel_configuracion.removeAll();
		panel_configuracion.add(new ChartPanel(grafica));
	}

	private static void inicia_hammingMedia(){
		int[] array = ca1DSimulator.getValoresHamming();
		int cont=0;
		int tam = array.length;
		for(int i=0;i<tam;i++){
			cont+=array[i];
		}

		double media = (double)cont/(double)tam;
		String formateado = String.format("%.2f", media);
		JOptionPane.showMessageDialog(null,"El valor hamming medio es: "+formateado);
	}

	private static void inicia_entropiaGrafica(){
		XYSeriesCollection datos = new XYSeriesCollection();
		JFreeChart grafica = ChartFactory.createXYLineChart("Entropia espacial","generaciones","entropia",datos,PlotOrientation.VERTICAL,true,true,true);
		
		int[][] datos_valores = ca1DSimulator.getValores();
		int total = nCells;

		XYSeries aux = new XYSeries("Entropia");

		double valorAux=0.0;
		double valor=0.0;
		for(int g=0;g<generaciones;g++){
			for(int i=0;i<k;i++){
				if(datos_valores[i][g]!=0){
					valorAux = (double)datos_valores[i][g]/(double)total;
					valor+=valorAux*(Math.log(valorAux)/Math.log(2));
				}
			}
			aux.add(g,-valor);
			valor=0.0;
		}

		datos.addSeries(aux);

		panel_configuracion.removeAll();
		panel_configuracion.add(new ChartPanel(grafica));
	}

	private static void inicia_entropiaMedia(){
		double[] array = new double[generaciones];

		int[][] datos_valores = ca1DSimulator.getValores();
		int total = nCells;

		double valorAux=0.0;
		double valor=0.0;
		for(int g=0;g<generaciones;g++){
			for(int i=0;i<k;i++){
				if(datos_valores[i][g]!=0){
					valorAux = (double)datos_valores[i][g]/(double)total;
					valor+=valorAux*(Math.log(valorAux)/Math.log(2));
				}
			}
			array[g]=-valor;
			valor=0.0;
		}
		
		double cont=0.0;
		int tam = array.length;
		for(int i=0;i<tam;i++){
			cont+=array[i];
		}

		double media = cont/(double)tam;
		String formateado = String.format("%.2f", media);
		JOptionPane.showMessageDialog(null,"El valor de entropia media es: "+formateado);
	}

	private static void inicia_entropiaTemporal(){
		int[] array = ca1DSimulator.getValoresTemp();
		int cont=0;
		int tam = array.length;
		for(int i=0;i<tam;i++){
			cont+=array[i];
		}

		double media = (double)cont/(double)tam;
		String formateado = String.format("%.2f", media);
		JOptionPane.showMessageDialog(null,"El valor de entropia temporal\n para la celula "+celula_temporal+" es: "+formateado);
	}

}