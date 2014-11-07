package ass2;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.text.NumberFormatter;

import com.jogamp.opengl.util.FPSAnimator;

public class View extends JFrame implements KeyListener {
	public View() throws IOException {

		super("Road Simulation");
		init();
	}

	public void init() throws IOException {
		// Initialise OpenGL
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		// Create a panel to draw on
		initPanels(caps);

		setSize(1200, 1000);

		setVisible(true);

		// Catch window closing events and quit
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// add a GL Event listener to handle rendering

	}

	private void initPanels(GLCapabilities caps) throws IOException {
		final GLJPanel myPanel = new GLJPanel(caps);
		final RoadGenerator thisGenerator = new RoadGenerator();
		thisGenerator.genRoad();
		final Road road = new Road();
		final JPanel panel = new JPanel();
		final JPanel configPanel = new JPanel();
		final int drawFPS = 60;
		final FPSAnimator animator = new FPSAnimator(drawFPS);
		animator.add(myPanel);
		final int epoch = 1;		
		final double alpha = 0.1;
		final double gamma = 0.9;		
		final double epsilon = 0.1;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 8;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.ipady = 50;
		c.ipadx = 50;
		add(myPanel, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx = 0;
		c.ipady = 0;
		c.ipadx = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		add(panel, c);
		final JButton startButton = new JButton("Start");
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 3;
		c.gridy = 1;
		c.weighty = 0;
		c.weightx = 0;
		c.ipady = 0;
		c.ipadx = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		add(startButton, c);
		JButton stopButton = new JButton("Stop");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 2;
		c.weighty = 0;
		c.weightx = 0;
		c.ipady = 0;
		c.ipadx = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		add(stopButton, c);
		JButton resetButton = new JButton("Reset");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 3;
		c.weighty = 0;
		c.weightx = 0;
		c.ipady = 0;
		c.ipadx = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		add(resetButton, c);
		JButton genRoad = new JButton("Generate Traffic");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 4;
		c.weighty = 0;
		c.weightx = 0;
		c.ipady = 0;
		c.ipadx = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		add(genRoad, c);

		myPanel.addGLEventListener(road);
		myPanel.addKeyListener(road);
		myPanel.setFocusable(true);
		panel.setLayout(new GridLayout(8, 2));
		final JRadioButton basicButton = new JRadioButton("Basic Learner");
		basicButton.setEnabled(false);
		basicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				road.setParam(0);
			}
		});
		final JRadioButton advButton = new JRadioButton("Advanced Learner");
		advButton.setEnabled(false);
		advButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				road.setParam(1);
			}
		});

		NumberFormat longFormat = NumberFormat.getIntegerInstance();
		NumberFormatter numberFormatter = new NumberFormatter(longFormat);
		numberFormatter.setValueClass(Long.class); // optional, ensures you will
													// always get a long value
		numberFormatter.setAllowsInvalid(true); // this is the key!!
		final JFormattedTextField fpsField = new JFormattedTextField(
				numberFormatter);
		fpsField.setValue(drawFPS);
		JLabel fpsLabel = new JLabel("fps");
		//numberFormatter.setMinimum(0l); // Optional
		final JFormattedTextField epochField = new JFormattedTextField(
				numberFormatter);
		epochField.setValue(epoch);
		JLabel epochLabel = new JLabel("Epochs");
		DecimalFormat doubleFormat = (DecimalFormat) DecimalFormat.getInstance();
		NumberFormatter doubleFormatter = new NumberFormatter(doubleFormat);
		doubleFormatter.setValueClass(Double.class); // optional, ensures you will
													// always get a long value
		doubleFormatter.setAllowsInvalid(true); // this is the key!!
		
		final JFormattedTextField alphaField = new JFormattedTextField(
				doubleFormatter);
		alphaField.setValue(alpha);
		JLabel alphaLabel = new JLabel("Alpha");
		final JFormattedTextField gammaField = new JFormattedTextField(
				doubleFormatter);
		gammaField.setValue(gamma);
		JLabel gammaLabel = new JLabel("Gamma");
		final JFormattedTextField epsilonField = new JFormattedTextField(
				doubleFormatter);
		epsilonField.setValue(epsilon);
		JLabel epsilonLabel = new JLabel("Epsilon");
		epochField.setEnabled(false);
		alphaField.setEnabled(false);
		gammaField.setEnabled(false);
		epsilonField.setEnabled(false);
		/*
		final JButton epochButton = new JButton("Set Epoch");
		epochButton.setEnabled(false);
		epochButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				epoch = Integer.parseInt(epochField.getValue().toString());
				road.setEpoch(epoch);
			}
		});

		JRadioButton alphaButton = new JRadioButton("Reinforced Learner");
		alphaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int alpha = Integer.parseInt(field.getValue().toString());
				road.setEpoch(alpha);
			}
		});
		**/		
		final JButton learnButton = new JButton("Learn!");
		learnButton.setEnabled(false);
		learnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myPanel.repaint();
				Integer.parseInt(fpsField.getValue().toString());
				road.learn(
						Integer.parseInt(epochField.getValue().toString()),
						Double.parseDouble(alphaField.getValue().toString()),
						Double.parseDouble(gammaField.getValue().toString()),
						Double.parseDouble(epsilonField.getValue().toString())
				);
				startButton.setEnabled(true);
				myPanel.repaint();
			}
		});
		JRadioButton timeButton = new JRadioButton("Time-Based");
		timeButton.setSelected(true);
		timeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				road.setMode(0);
				basicButton.setEnabled(false);
				startButton.setEnabled(true);
				advButton.setEnabled(false);
				learnButton.setEnabled(false);
				epochField.setEnabled(false);
				alphaField.setEnabled(false);
				gammaField.setEnabled(false);
				epsilonField.setEnabled(false);
			}
		});
		JRadioButton rlButton = new JRadioButton("Reinforced Learner");
		rlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				road.setMode(1);
				startButton.setEnabled(false);
				basicButton.setEnabled(true);
				advButton.setEnabled(true);
				basicButton.setSelected(true);
				learnButton.setEnabled(true);
				epochField.setEnabled(true);
				alphaField.setEnabled(true);
				gammaField.setEnabled(true);
				epsilonField.setEnabled(true);
				//epochButton.setEnabled(true);
			}
		});

		ButtonGroup group = new ButtonGroup();
		ButtonGroup rlGroup = new ButtonGroup();
		rlGroup.add(basicButton);
		rlGroup.add(advButton);
		group.add(timeButton);
		group.add(rlButton);
		panel.add(timeButton);
		panel.add(rlButton);
		panel.add(basicButton);
		panel.add(advButton);
		//panel.add(epochButton);	
		panel.add(fpsField);
		panel.add(fpsLabel);
		panel.add(epochField);
		panel.add(epochLabel);
		panel.add(alphaField);
		panel.add(alphaLabel);
		panel.add(gammaField);
		panel.add(gammaLabel);
		panel.add(epsilonField);
		panel.add(epsilonLabel);
		// add an Animator
		panel.add(learnButton);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pressed Start");				
				animator.setFPS(Integer.parseInt(fpsField.getValue().toString()));
				fpsField.setEnabled(false);
				epochField.setEnabled(false);
				alphaField.setEnabled(false);
				gammaField.setEnabled(false);
				epsilonField.setEnabled(false);
				road.setDraw(true);
				animator.start();
				
			}
		});
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				road.setDraw(false);
				fpsField.setEnabled(true);
				animator.stop();
			}
		});
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					road.setDraw(false);
					road.resetRoad();
					animator.stop();
					myPanel.revalidate();
					myPanel.repaint();
					((AbstractButton) panel.getComponent(0)).setSelected(true);	
					((AbstractButton) panel.getComponent(panel.getComponentCount()-1)).setEnabled(false);
					epochField.setValue(epoch);
					alphaField.setValue(alpha);
					epsilonField.setValue(epsilon);
					fpsField.setEnabled(true);
					gammaField.setValue(gamma);
					fpsField.setValue(drawFPS);
					panel.revalidate();
					panel.repaint();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				}
			}
		});

		genRoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					thisGenerator.genRoad();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		// configPanel.add(genRoad);
	}


	public static void main(String[] args) throws IOException {
		for(char k = 'z';k > 'a';k--){
			System.out.println(k);
		}
		View example = new View();
	}

	// }
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
