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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.nio.charset.Charset;
import java.awt.FileDialog;
import java.awt.Frame;

public class Urm{
	private static JPanel panel_botonera, panel_info, panel_textos;
	private static JTextPane text_programa, text_input;
	private static JTextArea text_output;
	private static Frame f = new Frame();
	private static FileDialog fd;


	public static void initUrm(JSplitPane pan_gen){
		panel_textos = new JPanel();
		pan_gen.setLeftComponent(panel_textos);

		panel_botonera = new JPanel();
		panel_info = new JPanel();
		JSplitPane aux = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel_botonera, panel_info);
		//aux.setDividerLocation(0.5);

		pan_gen.setRightComponent(aux);

		pan_gen.setDividerLocation(0.70);

		init_botonera();
		init_info();
		init_textos_vacio();
	}

	private static void init_info(){
		panel_info.removeAll();
		panel_info.repaint();

		panel_info.setBackground(new Color(190,190,190));

		JLabel text0 = new JLabel("Instrucciones de URM:");
		panel_info.add(text0);

		JLabel text1 = new JLabel("-Z(n)");
		panel_info.add(text1);

		JLabel text2 = new JLabel("-S(n)");
		panel_info.add(text2);

		JLabel text3 = new JLabel("-T(m,n)");
		panel_info.add(text3);

		JLabel text4 = new JLabel("-J(m,n,i)");
		panel_info.add(text4);

		JLabel text5 = new JLabel("Mas informacion en help.");
		panel_info.add(text5);

		SpringLayout layout_inputs = new SpringLayout();
		panel_info.setLayout(layout_inputs);

		layout_inputs.putConstraint(SpringLayout.NORTH, text0, 5, SpringLayout.NORTH, panel_info);
		layout_inputs.putConstraint(SpringLayout.WEST, text0, 5, SpringLayout.WEST, panel_info);

		layout_inputs.putConstraint(SpringLayout.NORTH, text1, 5, SpringLayout.SOUTH, text0);
		layout_inputs.putConstraint(SpringLayout.WEST, text1, 5, SpringLayout.WEST, panel_info);

		layout_inputs.putConstraint(SpringLayout.NORTH, text2, 5, SpringLayout.SOUTH, text1);
		layout_inputs.putConstraint(SpringLayout.WEST, text2, 5, SpringLayout.WEST, panel_info);

		layout_inputs.putConstraint(SpringLayout.NORTH, text3, 5, SpringLayout.SOUTH, text2);
		layout_inputs.putConstraint(SpringLayout.WEST, text3, 5, SpringLayout.WEST, panel_info);

		layout_inputs.putConstraint(SpringLayout.NORTH, text4, 5, SpringLayout.SOUTH, text3);
		layout_inputs.putConstraint(SpringLayout.WEST, text4, 5, SpringLayout.WEST, panel_info);

		layout_inputs.putConstraint(SpringLayout.NORTH, text5, 5, SpringLayout.SOUTH, text4);
		layout_inputs.putConstraint(SpringLayout.WEST, text5, 5, SpringLayout.WEST, panel_info);

		panel_info.revalidate();
	}

	private static void init_botonera(){
		panel_info.removeAll();
		panel_info.repaint();

		panel_botonera.setBackground(new Color(190,190,190));

		JButton fichero_button = new JButton("Cargar fichero");
		panel_botonera.add(fichero_button);

		JButton run_button = new JButton("RUN!");
		run_button.setBackground(new Color(255,86,49));
		run_button.setForeground(new Color(255,255,255));
		panel_botonera.add(run_button);

		SpringLayout layout_inputs = new SpringLayout();
		panel_info.setLayout(layout_inputs);


		//TODO: Poner bien los botones.
		layout_inputs.putConstraint(SpringLayout.NORTH, fichero_button, 5, SpringLayout.NORTH, panel_info);
		layout_inputs.putConstraint(SpringLayout.WEST, fichero_button, 5, SpringLayout.WEST, panel_info);

		layout_inputs.putConstraint(SpringLayout.NORTH, run_button, 5, SpringLayout.SOUTH, fichero_button);
		layout_inputs.putConstraint(SpringLayout.WEST, run_button, 5, SpringLayout.WEST, panel_info);


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


		panel_info.revalidate();
	}

	private static void init_textos_vacio(){
		panel_textos.removeAll();
		panel_textos.repaint();

		panel_textos.setLayout(new BorderLayout());

		text_programa = new JTextPane(); //editable
		text_input = new JTextPane(); //editable

		text_output = new JTextArea(); //solo lectura
		text_output.setEditable(false);

		text_programa.replaceSelection("");
		text_input.replaceSelection("");
		text_output.replaceSelection("Output: ");

		JSplitPane textos_split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, text_input, text_output);
		//textos_split1.setResizeWeight(0.5);

		JSplitPane textos_split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, text_programa, textos_split1);
		//textos_split2.setResizeWeight(0.5);

		//textos_split1.setDividerLocation(0.5);
		textos_split2.setResizeWeight(1);

		panel_textos.add(textos_split2, BorderLayout.CENTER);

		panel_textos.revalidate();
	}

	private static void cargaFichero() throws Exception{
		text_programa.setText(null);
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
			text_programa.replaceSelection(all);
		}  
	}

	private static void iniciar_programa(){
		if(!text_programa.getText().equals("") && !text_input.getText().equals("")){
			interpreteUrm.setDatos(text_programa.getText(),text_input.getText());
			int res = interpreteUrm.computa();

			if(res>=0) text_output.setText("Output: "+res);	
			else text_output.setText("Error en el codigo");

		}else{
			JOptionPane.showMessageDialog(null,"Falta por introducir el programa o los datos de entrada");
		}
		

		System.out.println("Finalizado");
	}
}