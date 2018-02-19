

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

public class Aleatorios{
	private static JPanel panel_botonera, panel_configuracion, panel_grafico;
	private static boolean iniciable =false;
	private static int tam_puntos = 4;
	/*
		*MODO ACTUAL*
		Indica que generador se esta usando en este momento.
		-1 -> ninguno
		0 -> 26a
		1 -> 26b
		2 -> 26_2
		3 -> 26_3
		4 -> combinada
		5 -> fishman
		6 -> moore
		7 -> randu
		8 -> personalizado
	*/
	private static int modo_actual=-1;

	private static int n=100;
	private static double a=0;
	private static double b=0;
	private static double m=0;
	private static double seed=1;

	public static void initAleatorios(JPanel pan_bot, JPanel pan_gra, JPanel pan_con){
		panel_grafico=pan_gra;
		panel_botonera=pan_bot;
		panel_configuracion=pan_con;

		init_botonera();
		init_configuracion_vacio();
		init_grafico_vacio();
	}

	private static void init_botonera(){
		panel_botonera.removeAll();
		panel_botonera.repaint();

		panel_botonera.setBackground(new Color(190,190,190));

		//para ordenar los botones dentro del panel
		GridLayout layout_inputs = new GridLayout(3,0);
		panel_botonera.setLayout(layout_inputs);

		JButton bgeneral = new JButton("Personalizado");
		panel_botonera.add(bgeneral);
		
		JButton b26a = new JButton("26a");
		panel_botonera.add(b26a);

		JButton b26b = new JButton("26b");
		panel_botonera.add(b26b);

		JButton b26_2 = new JButton("26_2");
		panel_botonera.add(b26_2);

		JButton b26_3 = new JButton("26_3");
		panel_botonera.add(b26_3);

		JButton bcombinada = new JButton("Combinada");
		panel_botonera.add(bcombinada);

		JButton bfishman = new JButton("Fishman");
		panel_botonera.add(bfishman);

		JButton bmoore = new JButton("Moore");
		panel_botonera.add(bmoore);

		JButton brandu = new JButton("Randu");
		panel_botonera.add(brandu);

		JButton brun = new JButton("RUN!");
		brun.setBackground(new Color(255,86,49));
		brun.setForeground(new Color(255,255,255));
		panel_botonera.add(brun);

		//****Programacion botones****

		bgeneral.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				//lo que quiera hacer el boton 
				init_configuracion_personalizado();
				iniciable=true;
				modo_actual=8;
			}
		});

		b26a.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				//lo que quiera hacer el boton 
				init_configuracion_comun("26a");
				iniciable=true;
				modo_actual=0;
			}
		});

		b26b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				//lo que quiera hacer el boton 
				init_configuracion_comun("26b");
				iniciable=true;
				modo_actual=1;
			}
		});

		b26_2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				//lo que quiera hacer el boton 
				init_configuracion_comun("26_2");
				iniciable=true;
				modo_actual=2;
			}
		});

		b26_3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				//lo que quiera hacer el boton 
				init_configuracion_comun("26_3");
				iniciable=true;
				modo_actual=3;
			}
		});

		bcombinada.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				//lo que quiera hacer el boton 
				init_configuracion_comun("Combinada");
				iniciable=true;
				modo_actual=4;
			}
		});

		bfishman.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				//lo que quiera hacer el boton 
				init_configuracion_comun("Fishman");
				iniciable=true;
				modo_actual=5;
			}
		});

		bmoore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				//lo que quiera hacer el boton 
				init_configuracion_comun("Moore");
				iniciable=true;
				modo_actual=6;
			}
		});

		brandu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				//lo que quiera hacer el boton 
				init_configuracion_comun("Randu");
				iniciable=true;
				modo_actual=7;
			}
		});

		brun.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				if (iniciable) iniciar_programa();
			}
		});
		
		//Para que se reinicie los layout despues de añadir/eliminar botones
		panel_botonera.revalidate();
	}

	private static void iniciar_programa(){
		double[] puntos = new double[n];

		double seed_saved = seed;

		if(modo_actual==4){
			puntos=randomGenerator.fcombinada(seed, n);
			for(int i=0;i<puntos.length;i++){
				puntos[i] = puntos[i]/32362;
			}
		}else{

			for(int i=0;i<n;i++){
				switch(modo_actual){
					case 0: puntos[i]=randomGenerator.f26a(seed);
					seed=puntos[i];
					puntos[i] = puntos[i]/Math.pow(2,5);
					break;

					case 1: puntos[i]=randomGenerator.f26b(seed);
					seed=puntos[i];
					puntos[i] = puntos[i]/Math.pow(2,5);
					break;

					case 2: puntos[i]=randomGenerator.f26_2(seed);
					seed=puntos[i];
					puntos[i] = puntos[i]/31;
					break;

					case 3: puntos[i]=randomGenerator.f26_3(seed);
					seed=puntos[i];
					puntos[i] = puntos[i]/Math.pow(2,31)-1;
					break;

					case 4: break;

					case 5: puntos[i]=randomGenerator.fFishman(seed);
					seed=puntos[i];
					puntos[i] = puntos[i]/Math.pow(2,31)-1;
					break;

					case 6: puntos[i]=randomGenerator.fMoore(seed);
					seed=puntos[i];
					puntos[i] = puntos[i]/Math.pow(2,31)-1;
					break;

					case 7: puntos[i]=randomGenerator.fRandu(seed);
					seed=puntos[i];
					puntos[i] = puntos[i]/Math.pow(2,31);
					break;

					case 8: puntos[i]=randomGenerator.funcionBase(seed, a, b, m);
					seed=puntos[i];
					puntos[i] = puntos[i]/m;
					break;

					default: break;
				}

				
				//System.out.println(puntos[i]+" ");
			}
		}


		seed = seed_saved; //reinicio de seed
		dibujar_grafica(puntos);
	}

	private static void init_configuracion_vacio(){
		panel_configuracion.removeAll();
		panel_configuracion.repaint();

		panel_configuracion.setBackground(new Color(190,190,190));

		panel_configuracion.revalidate();
	}

	private static void init_configuracion_personalizado(){
		panel_configuracion.removeAll();
		panel_configuracion.repaint();

		panel_configuracion.setBackground(new Color(190,190,190));

		JLabel text = new JLabel("Parametros de configuracion - Personalizado");
		
		SpringLayout layout_inputs = new SpringLayout();
		panel_configuracion.setLayout(layout_inputs);
		
		//por defecto es 0, maximo tamaño 2
		JLabel label1 = new JLabel("Valor a: ");
		JSpinner input1 = new JSpinner();
		input1.setValue(0);
		
		JLabel label2 = new JLabel("Valor b: ");
		JSpinner input2 = new JSpinner();
		input2.setValue(0);

		JLabel label3 = new JLabel("Valor m: ");
		JSpinner input3 = new JSpinner();
		input3.setValue(0);
		
		JLabel label4 = new JLabel("Valor seed: ");
		JSpinner input4 = new JSpinner();
		input4.setValue(1);
		
		JLabel label5 = new JLabel("Numero de aleatorios: ");
		JSpinner input5 = new JSpinner();
		input5.setValue(100);
		
		panel_configuracion.add(text);

		panel_configuracion.add(label1);
		panel_configuracion.add(input1);

		panel_configuracion.add(label2);
		panel_configuracion.add(input2);

		panel_configuracion.add(label3);
		panel_configuracion.add(input3);

		panel_configuracion.add(label4);
		panel_configuracion.add(input4);

		panel_configuracion.add(label5);
		panel_configuracion.add(input5);
		
		layout_inputs.putConstraint(SpringLayout.NORTH, text, 5, SpringLayout.NORTH, panel_configuracion);
		layout_inputs.putConstraint(SpringLayout.WEST, text, 5, SpringLayout.WEST, panel_configuracion);

		layout_inputs.putConstraint(SpringLayout.NORTH, label1, 5, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.NORTH, input1, 5, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.WEST, label1, 5, SpringLayout.WEST, panel_configuracion);
		layout_inputs.putConstraint(SpringLayout.WEST, input1, 5, SpringLayout.EAST, label1);
		
		layout_inputs.putConstraint(SpringLayout.WEST, label2, 5, SpringLayout.WEST, panel_configuracion);
		layout_inputs.putConstraint(SpringLayout.WEST, input2, 5, SpringLayout.EAST, label2);
		layout_inputs.putConstraint(SpringLayout.NORTH, label2, 9, SpringLayout.SOUTH, label1);
		layout_inputs.putConstraint(SpringLayout.NORTH, input2, 5, SpringLayout.SOUTH, input1);

		layout_inputs.putConstraint(SpringLayout.WEST, label3, 5, SpringLayout.WEST, panel_configuracion);
		layout_inputs.putConstraint(SpringLayout.WEST, input3, 5, SpringLayout.EAST, label3);
		layout_inputs.putConstraint(SpringLayout.NORTH, label3, 9, SpringLayout.SOUTH, label2);
		layout_inputs.putConstraint(SpringLayout.NORTH, input3, 5, SpringLayout.SOUTH, input2);

		layout_inputs.putConstraint(SpringLayout.WEST, label4, 5, SpringLayout.WEST, panel_configuracion);
		layout_inputs.putConstraint(SpringLayout.WEST, input4, 5, SpringLayout.EAST, label4);
		layout_inputs.putConstraint(SpringLayout.NORTH, label4, 9, SpringLayout.SOUTH, label3);
		layout_inputs.putConstraint(SpringLayout.NORTH, input4, 5, SpringLayout.SOUTH, input3);

		layout_inputs.putConstraint(SpringLayout.WEST, label5, 5, SpringLayout.WEST, panel_configuracion);
		layout_inputs.putConstraint(SpringLayout.WEST, input5, 5, SpringLayout.EAST, label5);
		layout_inputs.putConstraint(SpringLayout.NORTH, label5, 9, SpringLayout.SOUTH, label4);
		layout_inputs.putConstraint(SpringLayout.NORTH, input5, 5, SpringLayout.SOUTH, input4);

		
		input1.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		a = (Integer)input1.getValue();
      		}
    	});
		
		input2.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		b = (Integer)input2.getValue();
      		}
    	});
		
		input3.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		m = (Integer)input3.getValue();
      		}
    	});
		
		input4.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		seed = (Integer)input4.getValue();
      		}
    	});
		
		input5.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		n = (Integer)input5.getValue();
      		}
    	});


		panel_configuracion.revalidate();
	}

	private static void init_configuracion_comun(String name){
		panel_configuracion.removeAll();
		panel_configuracion.repaint();

		JLabel text = new JLabel("Parametros de configuracion - "+name);
		
		SpringLayout layout_inputs = new SpringLayout();
		panel_configuracion.setLayout(layout_inputs);
		
		//por defecto es 0, maximo tamaño 2
		JLabel label1 = new JLabel("Numero de aleatorios: ");
		JSpinner input1 = new JSpinner();
		input1.setValue(100);
		
		JLabel label2 = new JLabel("Valor seed: ");
		JSpinner input2 = new JSpinner();
		input2.setValue(1);

		
		panel_configuracion.add(text);

		panel_configuracion.add(label1);
		panel_configuracion.add(input1);

		panel_configuracion.add(label2);
		panel_configuracion.add(input2);
		
		layout_inputs.putConstraint(SpringLayout.NORTH, text, 5, SpringLayout.NORTH, panel_configuracion);
		layout_inputs.putConstraint(SpringLayout.WEST, text, 5, SpringLayout.WEST, panel_configuracion);

		layout_inputs.putConstraint(SpringLayout.NORTH, label1, 5, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.NORTH, input1, 5, SpringLayout.SOUTH, text);
		layout_inputs.putConstraint(SpringLayout.WEST, label1, 5, SpringLayout.WEST, panel_configuracion);
		layout_inputs.putConstraint(SpringLayout.WEST, input1, 5, SpringLayout.EAST, label1);
		
		layout_inputs.putConstraint(SpringLayout.WEST, label2, 5, SpringLayout.WEST, panel_configuracion);
		layout_inputs.putConstraint(SpringLayout.WEST, input2, 5, SpringLayout.EAST, label2);
		layout_inputs.putConstraint(SpringLayout.NORTH, label2, 9, SpringLayout.SOUTH, label1);
		layout_inputs.putConstraint(SpringLayout.NORTH, input2, 5, SpringLayout.SOUTH, input1);

	
		input1.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
        		n = (Integer)input1.getValue();
      		}
    	});
		
		input2.addChangeListener(new ChangeListener(){
     		public void stateChanged(ChangeEvent e){
     			seed = (Integer)input2.getValue();
      		}
    	});


		panel_configuracion.revalidate();
	}

	private static void init_grafico_vacio(){
		panel_grafico.removeAll();
		panel_grafico.repaint();
		panel_grafico.revalidate();
	}

	private static void dibujar_grafica(double[] puntos){
		panel_grafico.removeAll();
		panel_grafico.repaint();

		//System.out.println("valor"+puntos[0]+" , "+puntos[1]);

		panel_grafico.setLayout(new BorderLayout());
         
        DibujoAleatorios paintPanel = new DibujoAleatorios(puntos, tam_puntos);
        panel_grafico.add(paintPanel, BorderLayout.CENTER);

		panel_grafico.revalidate();
	}
}

