package ass2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RoadGenerator {
	public void genRoad() throws IOException {
		Random rnd = new Random();
		BufferedWriter writer = new BufferedWriter(new FileWriter("carList"));
		for (int time = 0; time < 1000; time++) {
			if (time % (rnd.nextInt(10) + 5) < 1 ) {
				// if(time%8 >= 4){
				writer.write("1");
			} else {
				writer.write("0");
			}
			if (time % (rnd.nextInt(10) + 5) < 1) {
				 //if(time%8 >= 4){
				writer.write("1");
			} else {
				writer.write("0");
			}
		}
		writer.close();
	}
}
