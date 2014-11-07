package ass2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;


import com.jogamp.opengl.util.gl2.GLUT;

public class Road implements KeyListener, GLEventListener {
	static Random rnd;
	static char[][] roadView;
	static int time = 0;
	private static ArrayList<Car> vertCars;
	private static ArrayList<Car> horizCars;
	private double[] myPosition;
	private double myRotation;
	private double myScale;
	public RLearner newLearn = new RLearner();
	File f = new File("carList");
	private char[] carFile = new char[(int) f.length()];
	// Create a StateArray
	public int hCar;
	public int vCar;
	public int cLight;
	public int lDelay;
	public int epoch;
	private int lightMode;
	int waitCars;
	private boolean beginDraw = false;

	/**
	 * public static void main(String[] args) {
	 * 
	 * vertCars = new ArrayList<Car>(); horizCars = new ArrayList<Car>();
	 * //initRoad();
	 * 
	 * int ch = 'F';
	 * 
	 * try{ while(ch != -1){ ch = System.in.read(); if(ch == 'h') updateRoad();
	 * //printRoad(); } }catch (IOException e) { System.out.println ("IO error:"
	 * + e ); }
	 * 
	 * 
	 * do{ updateRoad(); } while(true);
	 * 
	 * }
	 * 
	 * @throws IOException
	 **/
	public Road() throws IOException {
		FileReader fr = new FileReader(f);
		fr.read(carFile);
		fr.close();
		vertCars = new ArrayList<Car>();
		rnd = new Random();
		horizCars = new ArrayList<Car>();
		myPosition = new double[2];
		myPosition[0] = 0;
		myPosition[1] = 0;
		vCar = 9;
		hCar = 9;
		cLight = 0;
		lDelay = 3;
		myRotation = 0;
		myScale = 1;
		epoch = 1;
		waitCars = 0;
		beginDraw = false;
		lightMode = 0; // 0 = time, 1 = RL

	}

	public void resetRoad() throws IOException {
		FileReader fr = new FileReader(f);
		fr.read(carFile);
		fr.close();
		time = 0;
		vertCars = new ArrayList<Car>();
		rnd = new Random();
		horizCars = new ArrayList<Car>();
		myPosition = new double[2];
		myPosition[0] = 0;
		myPosition[1] = 0;
		vCar = 9;
		hCar = 9;
		cLight = 0;
		lDelay = 3;
		myRotation = 0;
		myScale = 1;
		waitCars = 0;
		beginDraw = false;
		lightMode = 0; // 0 = time, 1 = RL
	}

	public int updateRoad() {
		if (time < 1000) {
			int vChain = 0;
			int hChain = 0;
			int newLight = cLight;
			boolean vCont = true;
			boolean hCont = true;
			int[][] roadGrid = new int[100][100];
			for (int i = 0; i < vertCars.size(); i++) {
				if (vertCars.get(i).getY() >= 99)
					vertCars.remove(i);
				vertCars.get(i).setPos(0, 1);

				if ((cLight == 0) && (vertCars.get(i).getY() == 49)) {
					vertCars.get(i).setPos(0, -1);
					if (vCont && vChain < 4)
						vChain++;
					waitCars++;
				} if((cLight == 0) && (vertCars.get(i).getY() == 48) && vChain == 0){
					vChain++;
				}else if (i > 0 && vertCars.get(i).getY() <= 48) {
					if (vertCars.get(i).getY() == vertCars.get(i - 1).getY()) {
						vertCars.get(i).setPos(0, -1);
						waitCars++;
						if (vCont && vChain < 4 && vChain != 0)
							vChain++;
					} else if (vertCars.get(i).getY() + 1 == vertCars
							.get(i - 1).getY()) {
						if (vCont && vChain < 4 && vChain != 0)
							vChain++;
					} else {
						if (vertCars.get(i).getY() < 50)
							vCont = false;
					}
				}

			}
			for (int i = 0; i < horizCars.size(); i++) {
				if (horizCars.get(i).getX() >= 99)
					horizCars.remove(i);
				horizCars.get(i).setPos(1, 0);
				if ((cLight == 1) && (horizCars.get(i).getX() == 49)) {
					horizCars.get(i).setPos(-1, 0);
					waitCars++;
					if (hCont && hChain < 4)
						hChain++;
				} if((cLight == 1) && (horizCars.get(i).getX() == 48) && hChain == 0){
					hChain++;
				}else if (i > 0 && horizCars.get(i).getX() <= 48) {
			
					if (horizCars.get(i).getX() == horizCars.get(i - 1).getX()) {
						horizCars.get(i).setPos(-1, 0);
						waitCars++;
						if (hCont && hChain < 4 && hChain != 0)
							hChain++;
					} else if (horizCars.get(i).getX() + 1 == horizCars.get(
							i - 1).getX()) {
						if (hCont && hChain < 4 && hChain != 0)
							hChain++;
					} else {
						hCont = false;
						// hChain = 0;
					}
				}

			}
			for (Car c : vertCars) {
				int x = c.getX();
				int y = c.getY();
				if (roadGrid[x][y] == 1)
					System.out.println("vert collision at " + x + " " + y);
				roadGrid[x][y] = 1;
				if (49 > y && y > 40 && 48 - y < vCar)
					vCar = 48 - y;
			}
			for (Car c : horizCars) {
				int x = c.getX();
				int y = c.getY();
				if (roadGrid[x][y] == 1)
					System.out.println("horiz collision at " + x + " " + y);
				roadGrid[x][y] = 1;
				if (49 > x && x > 40 && 48 - x < hCar)
					hCar = 48 - x;
			}
			if (lightMode == 0) {
				if (time % 20 < 10)
					cLight = 0;
				else
					cLight = 1;
				newLight = cLight;
			} else if (lDelay == 0) {
				// System.out.println(vCar + " " + vChain + "  " + hCar + "  " +
				// hChain);
				newLight = newLearn.getBestAction(0, vChain, 0, hChain, lDelay, cLight);
			}
			if (newLight != cLight) {
				cLight = newLight;
				lDelay = 3;
			} else if (lDelay > 0)
				lDelay--;
			// System.out.println(carFile[2*time] + " "+ carFile[2*time+1] + " "
			// + time);
			// if(time%(rnd.nextInt(10)+5)==0){
			if (carFile[2 * time] == '1') {
				// System.out.println("Car Added");
				Car newCar = new Car(0, 49);
				int index = horizCars.size();
				if (index == 0) {
					horizCars.add(newCar);
				} else {
					if (horizCars.get(index - 1).getX() != 0)
						horizCars.add(newCar);

				}
			}
			// if(time%(rnd.nextInt(10)+5)==0){
			if (carFile[2 * time + 1] == '1') {
				// System.out.println("Car Added");
				Car newCar = new Car(49, 0);
				int index = vertCars.size();
				if (index == 0) {
					vertCars.add(newCar);
				} else {
					if (vertCars.get(index - 1).getY() != 0)
						vertCars.add(newCar);
				}
			}
			time++;
			// System.out.println(waitCars);
			// printRoad(roadGrid);
			// horizontal car

			// vertical car
		}
		return waitCars;
	}

	public static void printRoad(int[][] roadGrid) {
		for (int y = 0; y < 100; y++) {
			for (int x = 0; x < 100; x++) {
				System.out.print(roadGrid[x][y]);
			}
			System.out.println();
		}
		System.out.println("=================================");
	}

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

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {

		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		// coordinate system (left, right, bottom, top)
		GLU glu = new GLU();
		glu.gluOrtho2D(-500, 500, 500, -500);

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		if (beginDraw == true) {
			updateRoad();
		}
		String waitText = Integer.toString(waitCars);
		GLUT glut = new GLUT();

		gl.glPushMatrix();
		{
			gl.glRotated(myRotation, 0, 0, 1.0f);
			gl.glScaled(myScale, myScale, 1.0f);
			gl.glColor3d(0.7, 0.7, 0.7);
			gl.glBegin(GL2.GL_POLYGON);
			{
				gl.glVertex2d(-500, -500);
				gl.glVertex2d(500, -500);
				gl.glVertex2d(500, 500);
				gl.glVertex2d(-500, 500);
			}
			gl.glEnd();
			gl.glTranslated(-500, 0, 0);
			gl.glColor3d(0.0, 0.0, 0.0);
			gl.glBegin(GL2.GL_POLYGON);
			{
				gl.glVertex2d(0, -7.5);
				gl.glVertex2d(1000, -7.5);
				gl.glVertex2d(1000, 22.5);
				gl.glVertex2d(0, 22.5);
			}
			gl.glEnd();
			gl.glTranslated(500, -500, 0);
			gl.glBegin(GL2.GL_POLYGON);
			{
				gl.glVertex2d(-12.5, 0);
				gl.glVertex2d(17.5, 0);
				gl.glVertex2d(17.5, 1000);
				gl.glVertex2d(-12.5, 1000);
			}
			gl.glEnd();
			gl.glTranslated(0, 510, 0);
			if (cLight == 1) {
				gl.glColor3d(0.0, 1.0, 0.0);
			} else {
				gl.glColor3d(1.0, 0.0, 0.0);
			}
			gl.glBegin(GL2.GL_POLYGON);
			{
				gl.glVertex2d(2.5, -32);
				gl.glVertex2d(17.5, -32);
				gl.glVertex2d(17.5, -17.5);
				gl.glVertex2d(2.5, -17.5);
			}
			gl.glEnd();
			gl.glTranslated(0, -20, 0);
			if (cLight == 1) {
				gl.glColor3d(1.0, 0.0, 0.0);
			} else {
				gl.glColor3d(0.0, 1.0, 0.0);
			}
			gl.glBegin(GL2.GL_POLYGON);
			{
				gl.glVertex2d(-27.5, 17.5);
				gl.glVertex2d(-12.5, 17.5);
				gl.glVertex2d(-12.5, 2.5);
				gl.glVertex2d(-27.5, 2.5);
			}
			gl.glEnd();
			gl.glTranslated(-10, 10, 0);
			for (Car c : horizCars)
				c.draw(gl);
			gl.glTranslated(20, -5, 0);
			for (Car c : vertCars)
				c.draw(gl);
			gl.glColor3f(1.0f, 0.0f, 0f);				
			gl.glRasterPos2f(250f, 400f); // <-- position of text
			glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "Cars Waiting: "
					+ waitText);
			gl.glRasterPos2f(250f, 430f); // <-- position of text
			glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "Frame: " + time);
		}
		gl.glPopMatrix();

	}

	private boolean drawRoad() {
		return beginDraw;
	}

	public void setDraw(boolean doDraw) {
		beginDraw = doDraw;
	}

	public void setMode(int i) {
		//System.out.print("Mode is now ");
		switch (i) {
		case 0:
			//System.out.println("time based.");
			break;
		case 1:
			//System.out.println("Reinforced Learner.");
			break;
		}
		lightMode = i;

	}

	public void setParam(int i) {
		//System.out.print("RLearner is now ");
		switch (i) {
		case 0:
			//System.out.println("basic.");
			break;
		case 1:
			//System.out.println("advanced.");
			break;
		}
		newLearn.setRL(i);

	}

	public int getMode() {
		return lightMode;
	}

	public void setEpoch(int thisEpoch) {
		epoch = thisEpoch;
	}
	public int getTime(){
		return time;
	}
	public void learn(int epoch,double alpha,double gamma,double epsilon) {
		// newLearn.resetLearner();
		newLearn.runLearn(epoch,alpha,gamma,epsilon);
		// for(int vCar = 9; vCar>= 0;vCar--){
		for (int vChain = 0; vChain <= 5; vChain++) {
			// for(int hCar = 9;hCar >= 0;hCar--){
			for (int hChain = 0; hChain <= 5; hChain++) {
				for (int lDelay = 3; lDelay >= 0; lDelay--) {
					for (int doSwitch = 1; doSwitch >= 0; doSwitch--) {
					}
					// System.out.println(vChain +" " +hChain+" " + lDelay + " "
					// + doSwitch + " " + newLearn.action[i] + " " + i++);
				}
				// }
			}
			// }
		}
		// for(double i: newLearn.action)
		// System.out.println(i);

	}

}
