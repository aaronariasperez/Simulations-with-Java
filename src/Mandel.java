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
import java.awt.image.BufferedImage;

public class Mandel{
	private static JPanel panel_botonera;
	private static JPanel panel_grafico;
	private static int dimension=600;
	private static int iteraciones=100;
	

	public static void initMandel(JSplitPane pan_general){
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

		JLabel label1 = new JLabel("Iteraciones:");
		JSpinner input1 = new JSpinner();
		input1.setValue(100);
		panel_botonera.add(label1);
		panel_botonera.add(input1);

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

		layout_inputs.putConstraint(SpringLayout.WEST, run_button, 5, SpringLayout.WEST, panel_botonera);
		layout_inputs.putConstraint(SpringLayout.NORTH, run_button, 10, SpringLayout.SOUTH, label1);


		input1.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		iteraciones = (Integer)input1.getValue();
      		}
    	});


      	run_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				panel_grafico.revalidate();
				Thread hilo_ejecucion = new Thread(Mandel::iniciar_programa);
				hilo_ejecucion.start();
			}
		});


		panel_botonera.revalidate();
	}

	private static void iniciar_programa(){

		int nCores = Runtime.getRuntime().availableProcessors();
		ThreadPoolExecutor pool = new ThreadPoolExecutor(nCores,nCores, 1000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		int ventana = dimension/nCores;
		int resto = dimension%nCores;
		int inf=0;
		int sup=ventana;

		panel_grafico.repaint();	
		ca2DSimulatorMandel.setIter_Dim(iteraciones,dimension);
		ca2DSimulatorMandel.generacionInicial();

		for(int i=0;i<nCores;i++){
			if(i==nCores-1){
				pool.execute(new ca2DSimulatorMandel(inf,sup+resto));
			}else{
				pool.execute(new ca2DSimulatorMandel(inf,sup));
				inf=sup;
				sup+=ventana;
			}
		}

		pool.shutdown();

		JLabel label1 = new JLabel("Calculando...");
		panel_grafico.add(label1);
		panel_grafico.revalidate();

		try{ pool.awaitTermination(1,TimeUnit.DAYS); }catch(Exception e){}	

		panel_grafico.removeAll();

		try{ pintaMandel(ca2DSimulatorMandel.getImagen()); }catch(Exception e){}
		panel_grafico.revalidate();

		System.out.println("Finalizado");

	}

	public static void pintaMandel(BufferedImage imagen) throws InterruptedException{
	    Graphics g = panel_grafico.getGraphics();
	    g.drawImage(imagen, 0, 0, panel_grafico.getWidth(),panel_grafico.getWidth(), panel_grafico);
  	}

}