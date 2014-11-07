package ass2;

import javax.media.opengl.GL2;

public class Car {
	int[] pos = new int[2];

	public Car(int x, int y) {
		pos[0] = x;
		pos[1] = y;
	}

	public int getX() {
		return pos[0];

	}

	public int getY() {
		return pos[1];

	}

	public void setPos(int x, int y) {
		pos[0] += x;
		pos[1] += y;
	}

	public void draw(GL2 gl) {
		gl.glColor3d(1.0, 1.0, 1.0);
		int x = (getX() - 49) * 10;
		int y = (getY() - 49) * 10;
		gl.glBegin(GL2.GL_POLYGON);
		{
			gl.glVertex2d(x - 3, y - 3);
			gl.glVertex2d(x + 3, y - 3);
			gl.glVertex2d(x + 3, y + 3);
			gl.glVertex2d(x - 3, y + 3);
		}
		gl.glEnd();
	}
}
