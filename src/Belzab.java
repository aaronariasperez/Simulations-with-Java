import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BorderLayout;
import java.util.concurrent.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Dimension;

public class Belzab{

	private static JPanel panel_botonera;
	private static JPanel panel_grafico;
	private static int dimension=300;
	private static ThreadPoolExecutor pool;
	private static Thread hilo_ejecucion;
	private static int fps=60;
	private static float alfa=1.2f;
	private static float beta=1.0f;
	private static float gamma=1.0f;

	public static void initBelzab(JSplitPane pan_general){
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

		JLabel label2 = new JLabel("Alfa:");
		JSpinner alfa_spinner = new JSpinner();
		alfa_spinner.setPreferredSize(new Dimension(52,20));
		alfa_spinner.setModel(new SpinnerNumberModel(alfa, null, null, 0.1));
		panel_botonera.add(label2);
		panel_botonera.add(alfa_spinner);

		JLabel label3 = new JLabel("Beta:");
		JSpinner beta_spinner = new JSpinner();
		beta_spinner.setPreferredSize(new Dimension(52,20));
		beta_spinner.setModel(new SpinnerNumberModel(beta, null, null, 0.1));
		panel_botonera.add(label3);
		panel_botonera.add(beta_spinner);

		JLabel label4 = new JLabel("Gamma:");
		JSpinner gamma_spinner = new JSpinner();
		gamma_spinner.setPreferredSize(new Dimension(52,20));
		gamma_spinner.setModel(new SpinnerNumberModel(gamma, null, null, 0.1));
		panel_botonera.add(label4);
		panel_botonera.add(gamma_spinner);

		JLabel label5 = new JLabel("FPS:");
		JSpinner fps_spinner = new JSpinner();
		fps_spinner.setValue(60);
		panel_botonera.add(fps_spinner);
		panel_botonera.add(label5);

		JButton stop_button = new JButton("Stop");
		panel_botonera.add(stop_button);

		JButton run_button = new JButton("RUN!");
		run_button.setBackground(new Color(255,86,49));
		run_button.setForeground(new Color(255,255,255));
		panel_botonera.add(run_button);


		layout_inputs.putConstraint(SpringLayout.NORTH, text, 5, SpringLayout.NORTH, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, text, 5, SpringLayout.WEST, panel_botonera);

		layout_inputs.putConstraint(SpringLayout.NORTH, label1, 7, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.NORTH, input1, 7, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.WEST, label1, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, input1, 5, SpringLayout.EAST, label1);

		layout_inputs.putConstraint(SpringLayout.NORTH, label2, 12, SpringLayout.SOUTH, label1);
		layout_inputs.putConstraint(SpringLayout.NORTH, alfa_spinner,12, SpringLayout.SOUTH, label1);
		layout_inputs.putConstraint(SpringLayout.WEST, label2, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, alfa_spinner, 5, SpringLayout.EAST, label2);

		layout_inputs.putConstraint(SpringLayout.NORTH, label3, 12, SpringLayout.SOUTH, label2);
		layout_inputs.putConstraint(SpringLayout.NORTH, beta_spinner, 12, SpringLayout.SOUTH, label2);
		layout_inputs.putConstraint(SpringLayout.WEST, label3, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, beta_spinner, 5, SpringLayout.EAST, label3);

		layout_inputs.putConstraint(SpringLayout.NORTH, label4, 12, SpringLayout.SOUTH, label3);
		layout_inputs.putConstraint(SpringLayout.NORTH, gamma_spinner, 12, SpringLayout.SOUTH, label3);
		layout_inputs.putConstraint(SpringLayout.WEST, label4, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.WEST, gamma_spinner, 5, SpringLayout.EAST, label4);

		layout_inputs.putConstraint(SpringLayout.WEST, label5, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, label5, 10, SpringLayout.SOUTH, label4);
		layout_inputs.putConstraint(SpringLayout.WEST, fps_spinner, 5, SpringLayout.EAST, label5);
		layout_inputs.putConstraint(SpringLayout.NORTH, fps_spinner, 10, SpringLayout.SOUTH, label4);

		layout_inputs.putConstraint(SpringLayout.WEST, run_button, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, run_button, 10, SpringLayout.SOUTH, fps_spinner);

		layout_inputs.putConstraint(SpringLayout.WEST, stop_button, 5, SpringLayout.EAST, run_button);
		layout_inputs.putConstraint(SpringLayout.NORTH, stop_button, 10, SpringLayout.SOUTH, fps_spinner);


		input1.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
     			dimension = (Integer)input1.getValue();
     			
     			if(dimension<300){
            		dimension=300;
            		input1.setValue(300);
            	}
      		}
    	});


      	alfa_spinner.addChangeListener(new ChangeListener() {
         	public void stateChanged(ChangeEvent e) {
            	alfa = (float)alfa_spinner.getValue();
            	if(alfa<0){ alfa=0; alfa_spinner.setValue(0.0f); }
            	if(alfa>2){ alfa=2; alfa_spinner.setValue(2.0f); }
            	ca2DSimulatorBelzab.setAlfa(alfa);
            	
         	}
      	});


      	beta_spinner.addChangeListener(new ChangeListener() {
         	public void stateChanged(ChangeEvent e) {
            	beta = (float)beta_spinner.getValue();
            	if(beta<0){ beta=0; beta_spinner.setValue(0.0f); }
            	if(beta>2){ beta=2; beta_spinner.setValue(2.0f); }
            	ca2DSimulatorBelzab.setBeta(beta);
         	}
      	});


      	gamma_spinner.addChangeListener(new ChangeListener() {
         	public void stateChanged(ChangeEvent e) {
            	gamma = (float)gamma_spinner.getValue();
            	if(gamma<0){ gamma=0; gamma_spinner.setValue(0.0f); }
            	if(gamma>2){ gamma=2; gamma_spinner.setValue(2.0f); }
            	ca2DSimulatorBelzab.setGamma(gamma);
         	}
      	});


		fps_spinner.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		fps = (Integer)fps_spinner.getValue();
        		ca2DSimulatorBelzab.setFPS(fps);
      		}
    	});


      	run_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				panel_grafico.revalidate();
				hilo_ejecucion = new Thread(Belzab::iniciar_programa);
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

	private static void iniciar_programa(){
		int nCores = Runtime.getRuntime().availableProcessors();
		CyclicBarrier bar = new CyclicBarrier(nCores);
		pool = new ThreadPoolExecutor(nCores,nCores, 1000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		int ventana = dimension/nCores;
		int resto = dimension%nCores;
		int inf=0;
		int sup=ventana;

		panel_grafico.repaint();
		ca2DSimulatorBelzab.resetStop();
		ca2DSimulatorBelzab.setPanelGrafico(panel_grafico);
		ca2DSimulatorBelzab.setDim(dimension);

		ca2DSimulatorBelzab.reiniciaBool();

		ca2DSimulatorBelzab.generacionInicial();

		for(int i=0;i<nCores;i++){
			if(i==nCores-1){
				pool.execute(new ca2DSimulatorBelzab(inf,sup+resto,bar));
			}else{
				pool.execute(new ca2DSimulatorBelzab(inf,sup,bar));
				inf=sup;
				sup+=ventana;
			}
		}

		pool.shutdown();
		try{ pool.awaitTermination(1,TimeUnit.DAYS); }catch(Exception e){}	

		System.out.println("Finalizado");
	}

	private static void parar_programa(){
		ca2DSimulatorBelzab.stopNow();
	}

}
