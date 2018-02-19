import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BorderLayout;
import java.util.concurrent.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Font;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;

public class Tumor{

	private static JPanel panel_botonera;
	private static JPanel panel_grafico;
	private static int dimension=600;
	private static ThreadPoolExecutor pool;
	private static Thread hilo_ejecucion;
	private static int fps=60;
	private static double p_survive = 0.98;
	private static double p_die = 1-p_survive;
	private static double p_migration = 0.1;
	private static double p_prolif = 0.1;
	private static double p_quies = 1 - p_migration - p_prolif;
	private static int np = 1;
	private static String option="Central";
	private static AtomicInteger contUnos;


	public static void initTumor(JSplitPane pan_general){
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

		JLabel text = new JLabel("Parametros");
		panel_botonera.add(text);

		SpringLayout layout_inputs = new SpringLayout();
		panel_botonera.setLayout(layout_inputs);


		JLabel label1 = new JLabel("Dimension:");
		JSpinner input1 = new JSpinner();
		input1.setValue(300);
		panel_botonera.add(label1);
		panel_botonera.add(input1);

		JLabel label2 = new JLabel("P. Survive:");
		JSpinner surv_spinner = new JSpinner();
		surv_spinner.setPreferredSize(new Dimension(52,20));
		surv_spinner.setModel(new SpinnerNumberModel(p_survive, 0, 1, 0.01));
		panel_botonera.add(label2);
		panel_botonera.add(surv_spinner);

		JLabel label3 = new JLabel("P. Migration:");
		JSpinner migr_spinner = new JSpinner();
		migr_spinner.setPreferredSize(new Dimension(52,20));
		migr_spinner.setModel(new SpinnerNumberModel(p_migration, 0, 1, 0.01));
		panel_botonera.add(label3);
		panel_botonera.add(migr_spinner);

		JLabel label4 = new JLabel("P. Proliferation:");
		JSpinner prol_spinner = new JSpinner();
		prol_spinner.setPreferredSize(new Dimension(52,20));
		prol_spinner.setModel(new SpinnerNumberModel(p_prolif, 0, 1, 0.01));
		panel_botonera.add(label4);
		panel_botonera.add(prol_spinner);

		JLabel label5 = new JLabel("NP:");
		JSpinner np_spinner = new JSpinner();
		np_spinner.setModel(new SpinnerNumberModel(np, 0, 30, 1));
		panel_botonera.add(label5);
		panel_botonera.add(np_spinner);

		JComboBox<String> box_option = new JComboBox<String>();
		box_option.setModel(new DefaultComboBoxModel<String>(new String[] {"Central","A","B","C","D"}));
		panel_botonera.add(box_option);

		JLabel label6 = new JLabel("FPS:");
		JSpinner fps_spinner = new JSpinner();
		fps_spinner.setValue(60);
		panel_botonera.add(fps_spinner);
		panel_botonera.add(label6);

		JButton poblacion_button = new JButton("Poblacion");
		panel_botonera.add(poblacion_button);

		JButton stop_button = new JButton("Stop");
		panel_botonera.add(stop_button);

		JButton run_button = new JButton("RUN!");
		run_button.setBackground(new Color(255,86,49));
		run_button.setForeground(new Color(255,255,255));
		panel_botonera.add(run_button);
		
		JLabel adv1 = new JLabel("*Si no aparece nada en pantalla es que no ha sobrevivido");
		panel_botonera.add(adv1);
		JLabel adv2 = new JLabel("el tumor, es necesario repetir la ejecucion.*");
		panel_botonera.add(adv2);


		layout_inputs.putConstraint(SpringLayout.NORTH, text, 5, SpringLayout.NORTH, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, text, 5, SpringLayout.WEST, panel_botonera);

		layout_inputs.putConstraint(SpringLayout.NORTH, label1, 7, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.NORTH, input1, 7, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.WEST, label1, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, input1, 5, SpringLayout.EAST, label1);

		layout_inputs.putConstraint(SpringLayout.NORTH, label2, 12, SpringLayout.SOUTH, label1);
		layout_inputs.putConstraint(SpringLayout.NORTH, surv_spinner,12, SpringLayout.SOUTH, label1);
		layout_inputs.putConstraint(SpringLayout.WEST, label2, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, surv_spinner, 5, SpringLayout.EAST, label2);

		layout_inputs.putConstraint(SpringLayout.NORTH, label3, 12, SpringLayout.SOUTH, label2);
		layout_inputs.putConstraint(SpringLayout.NORTH, migr_spinner, 12, SpringLayout.SOUTH, label2);
		layout_inputs.putConstraint(SpringLayout.WEST, label3, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, migr_spinner, 5, SpringLayout.EAST, label3);

		layout_inputs.putConstraint(SpringLayout.NORTH, label4, 12, SpringLayout.SOUTH, label3);
		layout_inputs.putConstraint(SpringLayout.NORTH, prol_spinner, 12, SpringLayout.SOUTH, label3);
		layout_inputs.putConstraint(SpringLayout.WEST, label4, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, prol_spinner, 5, SpringLayout.EAST, label4);

		layout_inputs.putConstraint(SpringLayout.NORTH, label5, 12, SpringLayout.SOUTH, label4);
		layout_inputs.putConstraint(SpringLayout.NORTH, np_spinner, 12, SpringLayout.SOUTH, label4);
		layout_inputs.putConstraint(SpringLayout.WEST, label5, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, np_spinner, 5, SpringLayout.EAST, label5);

		layout_inputs.putConstraint(SpringLayout.NORTH, box_option, 12, SpringLayout.SOUTH, label5);
		layout_inputs.putConstraint(SpringLayout.WEST, box_option, 5, SpringLayout.WEST, panel_botonera);

		layout_inputs.putConstraint(SpringLayout.WEST, label6, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, label6, 10, SpringLayout.SOUTH, box_option);
		layout_inputs.putConstraint(SpringLayout.WEST, fps_spinner, 5, SpringLayout.EAST, label6);
		layout_inputs.putConstraint(SpringLayout.NORTH, fps_spinner, 10, SpringLayout.SOUTH, box_option);

		layout_inputs.putConstraint(SpringLayout.WEST, poblacion_button, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, poblacion_button, 10, SpringLayout.SOUTH, fps_spinner);

		layout_inputs.putConstraint(SpringLayout.WEST, run_button, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, run_button, 10, SpringLayout.SOUTH, poblacion_button);

		layout_inputs.putConstraint(SpringLayout.WEST, stop_button, 5, SpringLayout.EAST, run_button);
		layout_inputs.putConstraint(SpringLayout.NORTH, stop_button, 10, SpringLayout.SOUTH, poblacion_button);

		layout_inputs.putConstraint(SpringLayout.WEST, adv1, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, adv1, 15, SpringLayout.SOUTH, run_button);
		layout_inputs.putConstraint(SpringLayout.WEST, adv2, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, adv2, 2, SpringLayout.SOUTH, adv1);
		
		
		input1.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
     			dimension = (Integer)input1.getValue();
     			
     			if(dimension<300){
            		dimension=300;
            		input1.setValue(300);
            	}
      		}
    	});


      	surv_spinner.addChangeListener(new ChangeListener() {
         	public void stateChanged(ChangeEvent e) {
            	p_survive = (double)surv_spinner.getValue();
            	if(p_survive<0){ p_survive=0.0; surv_spinner.setValue(0.0); }
            	if(p_survive>1){ p_survive=1.0; surv_spinner.setValue(1.0); }
            	p_die = 1-p_survive;
            	ca2DSimulatorTumor.setSurv(p_survive);
            	
         	}
      	});


      	migr_spinner.addChangeListener(new ChangeListener() {
         	public void stateChanged(ChangeEvent e) {
            	p_migration = (double)migr_spinner.getValue();
            	if(p_migration<0){ p_migration=0.0; migr_spinner.setValue(0.0); }
            	if(p_migration>1){ p_migration=1.0; migr_spinner.setValue(1.0); }
            	p_quies = 1 - p_migration - p_prolif;
            	ca2DSimulatorTumor.setMigr(p_migration);
         	}
      	});


      	prol_spinner.addChangeListener(new ChangeListener() {
         	public void stateChanged(ChangeEvent e) {
            	p_prolif = (double)prol_spinner.getValue();
            	if(p_prolif<0){ p_prolif=0.0; prol_spinner.setValue(0.0); }
            	if(p_prolif>1){ p_prolif=1.0; prol_spinner.setValue(1.0); }
            	p_quies = 1 - p_migration - p_prolif;
            	ca2DSimulatorTumor.setProl(p_prolif);
         	}
      	});


      	np_spinner.addChangeListener(new ChangeListener() {
         	public void stateChanged(ChangeEvent e) {
            	np = (Integer)np_spinner.getValue();
            	if(np<0){ np=0; np_spinner.setValue(0); }
            	ca2DSimulatorTumor.setNP(np);
         	}
      	});


      	box_option.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent e) {
            	option = box_option.getSelectedItem().toString();
            	configura_opcion(surv_spinner,migr_spinner,prol_spinner,np_spinner);
         	}
      	});


		fps_spinner.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		fps = (Integer)fps_spinner.getValue();
        		ca2DSimulatorTumor.setFPS(fps);
      		}
    	});


    	poblacion_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				inicia_poblacion();
			}
		});


      	run_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				panel_grafico.revalidate();
				hilo_ejecucion = new Thread(Tumor::iniciar_programa);
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

	private static void configura_opcion(JSpinner surv_spinner,JSpinner migr_spinner,JSpinner prol_spinner,JSpinner np_spinner){
		if(option.equals("Central")){
			p_survive=0.95;
			p_migration=0.1;
			p_prolif=0.1;
			np=1;

		}else{
			p_survive=1;
			p_prolif=0.25;

			switch(option){
				case "A": p_migration=0.2;
				np=1;
				break;

				case "B": p_migration=0.8;
				np=1;
				break;

				case "C": p_migration=0.2;
				np=2;
				break;

				case "D": p_migration=0.8;
				np=2;
				break;				
			}

		}

		surv_spinner.setValue(p_survive);
		migr_spinner.setValue(p_migration);
		prol_spinner.setValue(p_prolif);
		np_spinner.setValue(np);

		ca2DSimulatorTumor.setSurv(p_survive);
		ca2DSimulatorTumor.setMigr(p_migration);
		ca2DSimulatorTumor.setProl(p_prolif);
		ca2DSimulatorTumor.setNP(np);
	}

	private static void reiniciaContador(){
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
		ca2DSimulatorTumor.resetStop();
		ca2DSimulatorTumor.setPanelGrafico(panel_grafico);
		ca2DSimulatorTumor.setArraysValores();
		ca2DSimulatorTumor.setDim(dimension);
		ca2DSimulatorTumor.setOption(option);

		ca2DSimulatorTumor.reiniciaBool();
		reiniciaContador();
		ca2DSimulatorTumor.setAtomics(contUnos);

		ca2DSimulatorTumor.generacionInicial();

		for(int i=0;i<nCores;i++){
			if(i==nCores-1){
				pool.execute(new ca2DSimulatorTumor(inf,sup+resto,bar));
			}else{
				pool.execute(new ca2DSimulatorTumor(inf,sup,bar));
				inf=sup;
				sup+=ventana;
			}
		}

		pool.shutdown();
		try{ pool.awaitTermination(1,TimeUnit.DAYS); }catch(Exception e){}	

		System.out.println("Finalizado");
	}

	private static void parar_programa(){
		ca2DSimulatorTumor.stopNow();
	}

	private static void inicia_poblacion(){
		XYSeriesCollection datos = new XYSeriesCollection();
		JFreeChart grafica = ChartFactory.createXYLineChart("Poblacion","Generaciones","Cantidad",datos,PlotOrientation.VERTICAL,true,true,true);
		
		List<Integer> datos_valores = ca2DSimulatorTumor.getValores();
		XYSeries aux;

		aux = new XYSeries("Vivas");

		for(int j=0;j<datos_valores.size();j++){
			aux.add(j,datos_valores.get(j)); 
		}
			
		datos.addSeries(aux);

		JOptionPane.showMessageDialog(null,new ChartPanel(grafica));

	}


}