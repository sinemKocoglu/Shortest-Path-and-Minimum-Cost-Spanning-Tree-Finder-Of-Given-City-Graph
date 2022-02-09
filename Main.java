import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException{
		
		Locale.setDefault(new Locale("en","US"));
		int timeOfFather = 0;    
		int totNumOfCities;
		String cityOfMecnun = null;
		String cityOfLeyla = null;
		
		try {
			Scanner in = new Scanner(new File(args[0]));
			timeOfFather = Integer.parseInt(in.nextLine());    
			totNumOfCities = Integer.parseInt(in.nextLine());
			String[] temp = in.nextLine().split(" ");
			cityOfMecnun = temp[0];
			cityOfLeyla = temp[1];
			
			for(int j=0;j<totNumOfCities;j++) {
				String[] line = in.nextLine().split(" ");
				if(line[0].equals(cityOfLeyla)) {
					GraphOfCities.addLeftCity(line[0]);
					GraphOfCities.addRightCity(line[0]);
					if(line.length!=1) {
						for(int i=1;i<line.length-1;i=i+2) {
							GraphOfCities.addRightCity(line[i]);
							GraphOfCities.addEdgeToRight(line[0],line[i],Integer.parseInt(line[i+1]));
						}
					}
				}
				else if(line[0].startsWith("c")) {
					GraphOfCities.addLeftCity(line[0]);
					if(line.length!=1) {
						for(int i=1;i<line.length-1;i=i+2) {
							GraphOfCities.addLeftCity(line[i]);
							GraphOfCities.addEdgeToLeft(line[0],line[i],Integer.parseInt(line[i+1]));
						}
					}
				}
				else if(line[0].startsWith("d")) {
					GraphOfCities.addRightCity(line[0]);
					if(line.length!=1) {
						for(int i=1;i<line.length-1;i=i+2) {
							GraphOfCities.addRightCity(line[i]);
							GraphOfCities.addEdgeToRight(line[0],line[i],Integer.parseInt(line[i+1]));
						}
					}
				}	
			}

			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Catch - An error occurred.");
			e.printStackTrace();
		}
		
		File myOutputFile = new File(args[1]);
		try {
		      myOutputFile.createNewFile();
		      FileWriter writer = new FileWriter(args[1]);
		
		      writer.write(GraphOfCities.shortestDistanceToLeyla(cityOfMecnun, cityOfLeyla, timeOfFather));
		      writer.close();
		      
	    } catch (IOException e) {
	      System.out.println("Catch - An error occurred.");
	      e.printStackTrace();
	    }
	}
}
