package ass2;

import java.io.IOException;

public class RoadTester {
	public static void main(String[] args) throws IOException{
		RoadGenerator newRoadGen = new RoadGenerator();
		carRead newReader = new carRead();
		newRoadGen.genRoad();
		newReader.ReadRoad();
		Road thisRoad = new Road();
		timeTest(thisRoad);
		basicTest(thisRoad,1,1,1,0);
		basicTest(thisRoad,1,1,0,0);
		basicTest(thisRoad,1,0.1,0.9,0.1);
		basicTest(thisRoad,1,0.1,0.1,0.1);
		basicTest(thisRoad,1,0.5,0.5,0.5);
		basicTest(thisRoad,5,1,1,0);
		basicTest(thisRoad,5,1,0,0);
		basicTest(thisRoad,5,0.1,0.9,0.1);
		basicTest(thisRoad,5,0.1,0.1,0.1);
		basicTest(thisRoad,5,0.5,0.5,0.5);
		advTest(thisRoad,1,1,1,0);
		advTest(thisRoad,1,1,0,0);
		advTest(thisRoad,1,0.1,0.9,0.1);
		advTest(thisRoad,1,0.1,0.1,0.1);
		advTest(thisRoad,1,0.5,0.5,0.5);
		advTest(thisRoad,5,1,1,0);
		advTest(thisRoad,5,1,0,0);
		advTest(thisRoad,5,0.1,0.9,0.1);
		advTest(thisRoad,5,0.1,0.1,0.1);
		advTest(thisRoad,5,0.5,0.5,1);
		System.out.println("Complete");
		
		
	}
	public static void timeTest(Road thisRoad) throws IOException{
		int waitCars = 0;
		thisRoad.setMode(0);
		while(thisRoad.getTime() <1000){
			waitCars = thisRoad.updateRoad();
		}
		//System.out.println(waitCars);
		thisRoad.resetRoad();
	}
	public static void basicTest(Road thisRoad,int epoch,double alpha,double gamma, double epsilon) throws IOException{
		int waitCars = 0;
		thisRoad.setMode(1);
		thisRoad.learn(epoch,alpha,gamma,epsilon);
		while(thisRoad.getTime() <1000){
			waitCars = thisRoad.updateRoad();
		}
		//System.out.println(waitCars);
		thisRoad.resetRoad();
	}
	public static void advTest(Road thisRoad,int epoch,double alpha,double gamma, double epsilon) throws IOException{
		int waitCars = 0;
		thisRoad.setMode(1);
		thisRoad.setParam(1);
		thisRoad.learn(epoch,alpha,gamma,epsilon);
		while(thisRoad.getTime() <1000){
			waitCars = thisRoad.updateRoad();
		}
		//System.out.println(waitCars);
		thisRoad.resetRoad();
	}

}
