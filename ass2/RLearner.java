package ass2;

import java.util.ArrayList;
import java.util.Random;

public class RLearner {
	Road thisRoad;
	double[] action;
	int epochs = 1;
	double gamma = 0.9;
	double alpha = 0.1;
	double epsilon = 0.1;
	int rlParam = 0; // 0 for basic 1 for advanced

	// boolean endState;
	public void setRL(int param) {
		rlParam = param;
	}
	public double[] rlParams(){
		double[] params = new double[3];
		params[0] = alpha;
		params[1] = gamma;
		params[2] = epsilon;		
		return params;
		
	}
	public void runLearn(int newEpochs, double newAlpha,double newGamma, double newEpsilon) {
		epochs = newEpochs;
		gamma = newGamma;
		alpha = newAlpha;
		epsilon = newEpsilon;
		action = new double[200];
		System.out.println(alpha+" "+gamma+ " " +epsilon);
		for (int i = 0; i < epochs; i++) {
			runEpoch();
		}
	}

	private void runEpoch() {
		// While !endstate
		// for(int vCar = 9; vCar>= 0;vCar--){
		for (int vChain = 0; vChain < 5; vChain++) {
			// for(int hCar = 9;hCar >= 0;hCar--){
			for (int hChain = 0; hChain < 5; hChain++) {
				for (int lDelay = 3; lDelay >= 0; lDelay--) {
					for (int doSwitch = 1; doSwitch >= 0; doSwitch--)
						learnInstance(0, vChain, 0, hChain, lDelay, doSwitch);
				}
				// }
			}
			// }
		}

		return;
	}

	public void resetLearner() {
		action = new double[288];
	}

	private void learnInstance(int vCar, int vChain, int hCar, int hChain,
			int lDelay, int doSwitch) {
		// System.out.println("Learning new instance");
		double thisQVal = 0;
		double maxQVal = 0;
		double newQVal;
		double reward = 0;
		double reward2 = 0;
		int selectAction = (40 * vChain) + (8 * hChain) + 2 * (3 - lDelay)
				+ ((1 - doSwitch) % 2);
		thisQVal = action[selectAction];
		switch (rlParam) {
		case 0:
			if (vChain == 0 || hChain == 0) {
				reward = -1;
				if (lDelay == 0)
					lDelay = 4;
				maxQVal = getMaxQValue(vCar, vChain, hCar, hChain,
						(lDelay - 1) % 4);
			}
			break;
		case 1:
			if (vChain > 0 || hChain > 0) {
				if (doSwitch == 1) {
					reward = -vChain + hChain;
				} else {
					reward = -hChain + vChain;
				}
				if (lDelay == 0)
					lDelay = 4;
				maxQVal = getMaxQValue(vCar, vChain, hCar, hChain,
						(lDelay - 1) % 4);
			}
			break;
		}
		
		action[selectAction] = thisQVal + alpha
				* (reward + gamma * maxQVal - thisQVal);
		return;
	}

	private double getMaxQValue(int vCar, int vChain, int hCar, int hChain,
			int lDelay) {
		double maxQValue = -100;
		Random rnd = new Random();
		ArrayList<Double> tmpList;
		tmpList = new ArrayList<Double>();
		int selectAction = (40 * vChain) + (8 * hChain) + 2 * (3 - lDelay);
		// int selectAction =
		// (8000*(9-vCar))+(800*(vChain))+(80*(9-hCar))+(8*hChain)+2*(3-lDelay);
		tmpList.add(action[selectAction]);
		tmpList.add(action[selectAction + 1]);
		// Case for switching
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (vChain + x >= 0 && vChain + x < 5) {
					if (hChain + y >= 0 && hChain + y < 5) {
						selectAction = 40 * (vChain + x) + 8 * (hChain + y) + 2
								* (3 - lDelay);
						tmpList.add(action[selectAction]);
						tmpList.add(action[selectAction + 1]);
					}
				}
			}
		}
		for (double tmp : tmpList) {
			if (tmp > maxQValue)
				maxQValue = tmp;
		}
		if (Math.random() < epsilon) {
			int x = rnd.nextInt(tmpList.size());
			maxQValue = tmpList.get(x);
		}
		return maxQValue;

	}

	public int getBestAction(int vCar, int vChain, int hCar, int hChain,
			int lDelay,int cLight) {
		int selectAction = (40 * vChain) + (8 * hChain) + 2 * (3 - lDelay);
		// int selectAction =
		// (8000*(vCar))+(800*(vChain))+(80*(hCar))+(8*hChain)+2*(3-lDelay);
		if (selectAction >= 80000)
			System.out.println(vCar + " " + hCar + " " + lDelay);
		double tmp = action[selectAction];
		double tmp2 = action[selectAction + 1];
		// System.out.println(tmp + " " + tmp2);
		if (tmp2 > tmp)
			return 1;
		if (tmp == tmp2)
			return cLight;
		else
			return 0;

	}
}
