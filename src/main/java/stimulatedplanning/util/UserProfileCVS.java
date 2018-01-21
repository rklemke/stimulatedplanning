package stimulatedplanning.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import stimulatedplanning.StimulatedPlanningFactory;



public class UserProfileCVS {

	private static final Logger log = Logger.getLogger(UserProfileCVS.class.getName());   

	
	public UserProfileCVS() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static final String[][] userProfiles = {
			{ "id", "username", "name", "email", "language", "location", "year_of_birth", "gender", "level_of_education", "mailing_address", "goals", "enrollment_mode", "verification_status", "city", "country" },
	};
	
	
    public static List<String[]> getUserProfiles(String filename) {
        File file= new File(filename);

        // this gives you a 2-dimensional array of strings
        List<String[]> lines = new ArrayList<>();
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);

            while(inputStream.hasNextLine()){
                String line= inputStream.nextLine();
    			log.info("UserProfileCVS: read line: "+line);
            	String[] values = line.split(",");
                // this adds the currently parsed line to the 2-dimensional string array
                lines.add(values);
            }

            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            //return userProfiles;
        }
        
        return lines;

    }


}
