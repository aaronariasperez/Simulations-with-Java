import javax.swing.*;
import java.awt.EventQueue;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Canvas;

public class Esqueleto extends JFrame{
	
	private JSplitPane panel_general, panel_drcho;
	private JPanel panel_grafico, panel_botonera, panel_configuracion;
	private JMenuBar barra_menu;
	private JMenu menu_help, menu_about, menu_simulaciones;
	private JMenuItem item_help_aleatorios, item_help_automata, item_help_cifrado;
	private JMenuItem item_help_conway, item_help_belzab, item_help_tumor, item_help_mandel;
	private JMenuItem item_help_urm;
	private JMenuItem item_about, item_aleatorios, item_automata, item_cifrado;
	private JMenuItem item_conway, item_belzab, item_tumor, item_mandel, item_urm;

	
	public Esqueleto(){
		inicializarUI();
	}
	
	public void inicializarUI(){
		setTitle("MC-Simulaciones");
		setSize(900,480);
		setLocationRelativeTo(null); //centrada
		setDefaultCloseOperation(EXIT_ON_CLOSE); //al pulsar x
	
		initBarraMenu();
		initProgramacionMenu();
		initPanels();
		
		Conway.initConway(panel_general);
	}

	//******************* BARRA MENU *******************
	
	private void initBarraMenu(){
		//barra de herramientas
		this.barra_menu = new JMenuBar();

		//cada menu de la barra
		this.menu_simulaciones = new JMenu("Simulaciones");
		this.menu_help = new JMenu("Help");
		this.menu_about = new JMenu("About");
		
		//cada opcion de cada menu
		this.item_help_aleatorios = new JMenuItem("Aleatorios");
		this.item_help_automata = new JMenuItem("Automata");
		this.item_help_cifrado = new JMenuItem("Automata Cifrado");
		this.item_help_conway = new JMenuItem("Conway");
		this.item_help_belzab = new JMenuItem("Belzab");
		this.item_help_tumor = new JMenuItem("Tumor");
		this.item_help_mandel = new JMenuItem("Mandel");
		this.item_help_urm = new JMenuItem("URM");

		this.item_about = new JMenuItem("About");
		this.item_aleatorios = new JMenuItem("Aleatorios");
		this.item_automata = new JMenuItem("Automata");
		this.item_cifrado = new JMenuItem("Automata Cifrado");
		this.item_conway = new JMenuItem("Conway");
		this.item_belzab = new JMenuItem("Belzab");
		this.item_tumor = new JMenuItem("Tumor");
		this.item_mandel = new JMenuItem("Mandel");
		this.item_urm = new JMenuItem("URM");
		
		//metemos todas las opciones en los menus y los menus en la barra
		this.barra_menu.add(this.menu_simulaciones);
		this.menu_simulaciones.add(this.item_aleatorios);
		this.menu_simulaciones.add(this.item_automata);
		this.menu_simulaciones.add(this.item_cifrado);
		this.menu_simulaciones.add(this.item_conway);
		this.menu_simulaciones.add(this.item_belzab);
		this.menu_simulaciones.add(this.item_tumor);
		this.menu_simulaciones.add(this.item_mandel);
		this.menu_simulaciones.add(this.item_urm);
		
		this.barra_menu.add(this.menu_help);
		this.menu_help.add(this.item_help_aleatorios);
		this.menu_help.add(this.item_help_automata);
		this.menu_help.add(this.item_help_cifrado);
		this.menu_help.add(this.item_help_conway);
		this.menu_help.add(this.item_help_belzab);
		this.menu_help.add(this.item_help_tumor);
		this.menu_help.add(this.item_help_mandel);
		this.menu_help.add(this.item_help_urm);
		
		this.barra_menu.add(this.menu_about);
		this.menu_about.add(this.item_about);
		
		//metemos la barra en la ventana
		setJMenuBar(this.barra_menu);
	}
	
	private void initProgramacionMenu(){
		//se le añade funcionalidad a cada item
		item_help_aleatorios.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				help_aleatorios();
			}
		});

		item_help_automata.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				help_automata();
			}
		});

		item_help_cifrado.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				help_cifrado();
			}
		});

		item_help_conway.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				help_conway();
			}
		});

		item_help_belzab.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				help_belzab();
			}
		});

		item_help_tumor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				help_tumor();
			}
		});

		item_help_mandel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				help_mandel();
			}
		});

		item_help_urm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				help_urm();
			}
		});
		
		item_about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				about();
			}
		});
		
		item_aleatorios.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				reiniciaPaneles();
				panel_general.setDividerLocation(0.65);
				Aleatorios.initAleatorios(panel_botonera, panel_grafico, panel_configuracion);
			}
		});

		item_automata.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				reiniciaPaneles();
				Automata.initAutomata(panel_general, panel_botonera, panel_configuracion);
			}
		});

		item_cifrado.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				reiniciaPaneles();
				AutomataCifrado.initCifrado(panel_grafico, panel_botonera, panel_configuracion);
			} 
		});

		item_conway.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				reiniciaPaneles();
				Conway.initConway(panel_general);
			} 
		});

		item_belzab.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				reiniciaPaneles();
				Belzab.initBelzab(panel_general);
			} 
		});

		item_tumor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				reiniciaPaneles();
				Tumor.initTumor(panel_general);
			} 
		});

		item_mandel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				reiniciaPaneles();
				Mandel.initMandel(panel_general);
			} 
		});

		item_urm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				reiniciaPaneles();
				Urm.initUrm(panel_general);
			} 
		});
	}

	private void reiniciaPaneles(){
		panel_general.remove(panel_general.getLeftComponent());
		panel_general.setLeftComponent(panel_grafico);
		panel_general.setRightComponent(panel_drcho);
	}

	private void about(){
		String texto = "Software desarrollado para la asignatura\n"+
						"Modelos de Computacion, que nos permite probar\n"+
						"distintas simulaciones representadas graficamente.\n"+
						"\n\nAutor: Aaron Arias Perez";
		JOptionPane.showMessageDialog(null,texto);
	}

	private void help_aleatorios(){
		String texto = "Simulador de numeros aleatorios.\n"+
						"El simulador de numeros aleatorios nos ofrece\n"+
						"la posibilidad de generar numeros aleatorios\n"+
						"de distintas formas, representados graficamente.\n\n"+
						"*Indicaciones*\n"+
						"-Se deben introducir los parametros indicados\n"+
						"para cada generador y pulsar intro.\n"+
						"Luego click en Run para iniciar.\n"+
						"-Opcion personalizado: se puede crear un generador\n"+
						"personalizado de la forma (a*x + b) mod m\n";

		JOptionPane.showMessageDialog(null,texto);
	}

	private void help_automata(){
		String texto = "Simulador de automatas celulares.\n"+
						"El simulador de automatas celulares nos permite\n"+
						"ver representaciones graficas de su evolucion y\n"+
						"el crecimiento de la poblacion de las celulas\n"+
						"*Indicaciones*\n"+
						"-Se deben introducir los parametros indicados\n"+
						"y pulsar intro para las cajas de texto.\n"+
						"-Se seleccionara la opcion del generador que se desee.\n"+
						"-Se seleccionara la opcion de la condicion de frontera\n"+
						"deseada.\n"+
						"*Indicaciones2*\n"+
						"Despues de ejecutar el run, se podra ver la grafica de\n"+
						"poblacion, hamming y entropia, con solo pulsar 'Poblacion'\n"+
						"'Hamming G.' y 'Entropia G.'\n"+
						"Para el hamming medio y entropia media basta con pulsar\n"+
						"los botones 'Hamming M.' y 'Entropia M.'.\n"+
						"Para la entropia temporal de una sola celula, se debera\n"+
						"introducir la celula indicada antes de ejecutar el run,\n"+
						"luego pulsar en 'Entropia Temporal'.";

		JOptionPane.showMessageDialog(null, texto);
	}

	private void help_cifrado(){
		String texto = "Cifrador y descifrador de texto.\n"+
						"Con esta herramienta podemos cifrar y descifrar\n"+
						"el texto que queramos.\n"+
						"Para ello se utiliza un automata celular que genera\n"+
						"una secuencia cifrante lo mas caotica posible y junto\n"+
						"a la operacion XOR, se encripta el mensaje\n"+
						"*Indicaciones*\n"+
						"-Tanto para cifrar como para descifrar, se introducira\n"+
						"la clave.\n"+
						"-Se escribira el texto en el panel superior o se abrira\n"+
						"un archivo de texto con la opcion 'Cargar fichero'\n"+
						"-Se pulsara en 'RUN' para iniciar el cifrado o descifrado.\n";

		JOptionPane.showMessageDialog(null, texto);
	}

	private void help_conway(){
		String texto = "Juego de la vida de Conway.\n"+
						"Con esta herramienta podemos hacer simulaciones\n"+
						"del juego de la vida de Conway.\n"+
						"*Indicaciones*\n"+
						"-Se ajustara una dimension para la reticula, que sera\n"+
						"como minimo de 200.\n"+
						"-Se ajustara un numero de iteraciones y la semilla de aleatoriedad.\n"+
						"-Se seleccionara un estado inicial entre las opciones disponibles.\n"+
						"-Se pulsara en RUN! para iniciar el simulador, pudiendose alterar\n"+
						"los fotogramas por segundo, ademas de poder parar la simulacion.\n"+
						"-Tenemos la opcion para ver la grafica de poblacion en cualquier\n"+
						"punto de la simulacion.";

		JOptionPane.showMessageDialog(null, texto);
	}

	private void help_belzab(){
		String texto = "Simulador Reaccion Quimica de Belousov-Zhabotinsky.\n"+
						"Este simulador nos permite ver graficamente las reacciones\n"+
						"quimicas de Belousov-Zhabotinsky.\n"+
						"*Indicaciones*\n"+
						"-Se ajustara una dimension para la reticula, que sera\n"+
						"como minimo de 300.\n"+
						"-Se ajustara los parametros alfa, beta y gamma. Estos\n"+
						"parametros estan en el intervalo [0-2]\n"+
						"-Se pulsara en RUN! para iniciar el simulador,pudiendose alterar\n"+
						"los fotogramas por segundo, ademas de poder parar la simulacion.";

		JOptionPane.showMessageDialog(null, texto);
	}

	private void help_tumor(){
		String texto = "Simulador de crecimiento tumoral.\n"+
						"Este simulador nos permite ver graficamente la evolucion\n"+
						"de un tumor.\n"+
						"*Indicaciones*\n"+
						"-Se ajustara una dimension para la reticula, que sera\n"+
						"como minimo de 300.\n"+
						"-Se ajustaran los parametros, pudiendose elegir una\n"+
						"configuracion inicial de las disponibles.\n"+
						"-Se pulsara en RUN! para iniciar el simulador, pudiendose alterar\n"+
						"los fotogramas por segundo, ademas de poder parar la simulacion.\n"+
						"-Tenemos la opcion para ver la grafica de poblacion en cualquier\n"+
						"punto de la simulacion.";

		JOptionPane.showMessageDialog(null, texto);
	}

	private void help_mandel(){
		String texto = "Simulador de conjunto de Mandelbrot.\n"+
						"Este simulador nos permite ver graficamente el conjunto\n"+
						"de Mandelbrot.\n"+
						"*Indicaciones*\n"+
						"-Se ajustara el numero de iteraciones a computar y se pulsara\n"+
						"el boton RUN! para iniciar.";

		JOptionPane.showMessageDialog(null, texto);
	}

	private void help_urm(){
		String texto = "Interprete de URM.\n"+
						"Este interprete nos permite ejecutar programas de URM.\n"+
						"*Indicaciones*\n"+
						"-Se escribira el codigo en el panel de arriba o se cargara\n"+
						"un fichero en el boton Cargar Fichero.\n"+
						"-El programa se ejecutara con el boton RUN!\n"+
						"+Info de las instrucciones de URM+\n"+	
						"-Z(n): Reemplaza el valor del registro Rn por 0.\n"+
						"-S(n): Incrementa el valor del registro Rn en la unidad.\n"+
						"-T(m,n): Copia el valor del registro Rm en Rn.\n"+
						"-J(m,n,i): Si los registros Rm y Rn tienen el mismo valor,\n"+
						"entonces se salta a la i-esima instruccion.\n"+
						"Si su valor es distinto, se continua con la siguiente\n"+
						"instruccion. Si no existe la i-esima instruccion, el programa termina.";

		JOptionPane.showMessageDialog(null, texto);
	}

	//************************ PANELES ************************

	private void initPanels(){
		//creamos los tres subpaneles para distribuir las herramientas
		this.panel_grafico = new JPanel();
		panel_grafico.setLayout(new FlowLayout());
		
		this.panel_configuracion = new JPanel();
		
		this.panel_botonera = new JPanel();
		//para que los botones se pongan uno detras de otro y ocupen otras filas si es necesario
		this.panel_botonera.setLayout(new FlowLayout(FlowLayout.CENTER, 3,3));
		
		//este es el panel derecho q se divide en dos paneles, arriba y abajo
		this.panel_drcho = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel_configuracion, panel_botonera);
		panel_drcho.setResizeWeight(0.5);
		
		//este es el panel general q se divide en dos paneles, izquierda y derecha
		this.panel_general = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel_grafico, panel_drcho);
		panel_general.setResizeWeight(0.5);
		
		//add al jframe (metemos el panel en la ventana)
		add(panel_general, BorderLayout.CENTER);
	}


	//************************ MAIN ************************
	
    public static void main(String[] args) {
       
        EventQueue.invokeLater(() -> {
            Esqueleto esq = new Esqueleto();
			//esq.pack();
            esq.setVisible(true);
        });
    }
}