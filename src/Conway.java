
import javax.swing.*;
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
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class Conway{
	private static JPanel panel_botonera;
	private static JPanel panel_grafico;
	private static int dimension=300;
	private static int iteraciones=3000;
	private static AtomicInteger contCeros;
	private static AtomicInteger contUnos;
	private static ThreadPoolExecutor pool;
	private static int seed=1;
	private static String option="Glider";
	private static Thread hilo_ejecucion;
	private static int fps=60;
	

	public static void initConway(JSplitPane pan_general){
		panel_botonera = new JPanel();
		panel_grafico=new JPanel();

		pan_general.setLeftComponent(panel_grafico);
		pan_general.setRightComponent(panel_botonera);
		pan_general.setDividerLocation(0.50);

		init_botonera();
		init_grafico_vacio();
	}

	private static void init_grafico_vacio(){
		panel_grafico.removeAll();
		panel_grafico.repaint();
		panel_grafico.setBackground(new Color(255,255,255));
		panel_grafico.revalidate();
	}

	private static void init_botonera(){
		panel_botonera.removeAll();
		panel_botonera.repaint();

		panel_botonera.setBackground(new Color(190,190,190));
		
		JLabel text2 = new JLabel("Conway's Game of Life");
		panel_botonera.add(text2);

		JLabel text = new JLabel("Parametros");
		panel_botonera.add(text);

		SpringLayout layout_inputs = new SpringLayout();
		panel_botonera.setLayout(layout_inputs);


		JLabel label1 = new JLabel("Dimension:");
		JSpinner input1 = new JSpinner();
		input1.setValue(300);
		panel_botonera.add(label1);
		panel_botonera.add(input1);

		JLabel label2 = new JLabel("Iteraciones:");
		JSpinner input2 = new JSpinner();
		input2.setValue(3000);
		panel_botonera.add(label2);
		panel_botonera.add(input2);

		JLabel label3 = new JLabel("Seed:");
		JSpinner input3 = new JSpinner();
		input3.setValue(1);
		panel_botonera.add(label3);
		panel_botonera.add(input3);

		JComboBox<String> box_option = new JComboBox<String>();
		box_option.setModel(new DefaultComboBoxModel<String>(new String[] {"Glider","Random","Center","Infinite"}));
		panel_botonera.add(box_option);

		JButton poblacion_button = new JButton("Poblacion");
		panel_botonera.add(poblacion_button);

		JLabel label4 = new JLabel("FPS:");
		JSpinner fps_spinner = new JSpinner();
		fps_spinner.setValue(60);
		panel_botonera.add(fps_spinner);
		panel_botonera.add(label4);

		JButton stop_button = new JButton("Stop");
		panel_botonera.add(stop_button);

		JButton run_button = new JButton("RUN!");
		run_button.setBackground(new Color(255,86,49));
		run_button.setForeground(new Color(255,255,255));
		panel_botonera.add(run_button);

		layout_inputs.putConstraint(SpringLayout.NORTH, text2, 5, SpringLayout.NORTH, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, text2, 5, SpringLayout.WEST, panel_botonera);

		layout_inputs.putConstraint(SpringLayout.NORTH, text, 5, SpringLayout.SOUTH, text2);
		layout_inputs.putConstraint(SpringLayout.WEST, text, 5, SpringLayout.WEST, panel_botonera);

		layout_inputs.putConstraint(SpringLayout.NORTH, label1, 5, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.NORTH, input1, 5, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.WEST, label1, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, input1, 5, SpringLayout.EAST, label1);

		layout_inputs.putConstraint(SpringLayout.NORTH, label2, 7, SpringLayout.SOUTH, label1);
		layout_inputs.putConstraint(SpringLayout.NORTH, input2, 7, SpringLayout.SOUTH, label1);
		layout_inputs.putConstraint(SpringLayout.WEST, label2, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, input2, 5, SpringLayout.EAST, label2);

		layout_inputs.putConstraint(SpringLayout.NORTH, label3, 7, SpringLayout.SOUTH, label2);
		layout_inputs.putConstraint(SpringLayout.NORTH, input3, 7, SpringLayout.SOUTH, label2);
		layout_inputs.putConstraint(SpringLayout.WEST, label3, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, input3, 5, SpringLayout.EAST, label3);

		layout_inputs.putConstraint(SpringLayout.WEST, box_option, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, box_option, 10, SpringLayout.SOUTH, label3);

		layout_inputs.putConstraint(SpringLayout.WEST, poblacion_button, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, poblacion_button, 10, SpringLayout.SOUTH, box_option);

		layout_inputs.putConstraint(SpringLayout.WEST, label4, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, label4, 10, SpringLayout.SOUTH, poblacion_button);
		layout_inputs.putConstraint(SpringLayout.WEST, fps_spinner, 5, SpringLayout.EAST, label4);
		layout_inputs.putConstraint(SpringLayout.NORTH, fps_spinner, 10, SpringLayout.SOUTH, poblacion_button);

		layout_inputs.putConstraint(SpringLayout.WEST, run_button, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, run_button, 10, SpringLayout.SOUTH, fps_spinner);

		layout_inputs.putConstraint(SpringLayout.WEST, stop_button, 5, SpringLayout.EAST, run_button);
		layout_inputs.putConstraint(SpringLayout.NORTH, stop_button, 10, SpringLayout.SOUTH, fps_spinner);


		input1.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
            	dimension = (Integer)input1.getValue();

            	if(dimension<200){
            		dimension=200;
            		input1.setValue(200);
            	}
         	}
      	});


		input2.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		iteraciones = (Integer)input2.getValue();
      		}
    	});


		input3.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		seed = (Integer)input3.getValue();
      		}
    	});


      	box_option.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent e) {
            	option = box_option.getSelectedItem().toString();
         	}
      	});


      	poblacion_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				inicia_poblacion();
			}
		});


		fps_spinner.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		fps = (Integer)fps_spinner.getValue();
        		ca2DSimulatorConway.setFPS(fps);
      		}
    	});


      	run_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				panel_grafico.revalidate();
				hilo_ejecucion = new Thread(Conway::iniciar_programa);
				hilo_ejecucion.start();
			}
		});


		stop_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				parar_programa();
			}
		});

		panel_botonera.revalidate();
	}

	private static void reiniciaContadores(){
		contCeros = new AtomicInteger(0);
		contUnos = new AtomicInteger(0);
	}

	private static void iniciar_programa(){

		int nCores = Runtime.getRuntime().availableProcessors();
		CyclicBarrier bar = new CyclicBarrier(nCores);
		pool = new ThreadPoolExecutor(nCores,nCores, 1000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		int ventana = dimension/nCores;
		int resto = dimension%nCores;
		int inf=0;
		int sup=ventana;

		panel_grafico.repaint();
		ca2DSimulatorConway.resetStop();
		ca2DSimulatorConway.setPanelGrafico(panel_grafico);
		ca2DSimulatorConway.setArraysValores(iteraciones);
		ca2DSimulatorConway.setIter_Dim(iteraciones,dimension);
		ca2DSimulatorConway.setSeed(seed);
		ca2DSimulatorConway.setOption(option);

		ca2DSimulatorConway.reiniciaBool();
		reiniciaContadores();
		ca2DSimulatorConway.setAtomics(contCeros, contUnos);

		ca2DSimulatorConway.generacionInicial();

		for(int i=0;i<nCores;i++){
			if(i==nCores-1){
				pool.execute(new ca2DSimulatorConway(inf,sup+resto,bar));
			}else{
				pool.execute(new ca2DSimulatorConway(inf,sup,bar));
				inf=sup;
				sup+=ventana;
			}
		}

		pool.shutdown();
		try{ pool.awaitTermination(1,TimeUnit.DAYS); }catch(Exception e){}	

		System.out.println("Finalizado");

	}

	private static void parar_programa(){
		ca2DSimulatorConway.stopNow();
	}

	private static void inicia_poblacion(){
		XYSeriesCollection datos = new XYSeriesCollection();
		JFreeChart grafica = ChartFactory.createXYLineChart("Poblacion","Generaciones","Cantidad",datos,PlotOrientation.VERTICAL,true,true,true);
		
		int[][] datos_valores = ca2DSimulatorConway.getValores();
		XYSeries aux;
		for(int i=0;i<2;i++){
			aux = new XYSeries(String.valueOf(i));

			for(int j=0;j<datos_valores[0].length;j++){
				aux.add(j,datos_valores[i][j]); 
			}
			
			datos.addSeries(aux);
		}
		

		JOptionPane.showMessageDialog(null,new ChartPanel(grafica));

	}
}