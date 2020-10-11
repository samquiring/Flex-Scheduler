import java.io.*;
import java.util.*;


//Created by Sam Quiring

//The class CSVReader is responsible for analyzing the csv data of the students and their respective classes
//It also has the ability to print to a new CSV file for the output
//TODO: find out the best way to implement classRooms into the total data
public class CSVReader {
	 
	List<Student> allStudents;
    List<Classes> allClasses;
    List<ClassRoom> allClassRooms;
    final String CSV_FILE_NAME = "/Users/Sam/Desktop/exampleFinal.csv";
	
    //Initializes the CSVReader with a Set that will contain all the students,
    //a list that will contain all the Classes
    //and a List that will contain all the ClassRooms
    public CSVReader() {
    	this.allStudents = new ArrayList<Student>();
    	this.allClasses = new ArrayList<Classes>(); //might be more efficient to use different list type
    	this.allClassRooms = new ArrayList<ClassRoom>();
    }
    
    //This method takes a String csvFile that is the path to a CSVfile that contains all the classrooms
    //and their sizes
    //TODO: test and implement
    public void createClassRooms(String csvFile) {
    	 BufferedReader br = null;
         String line = "";
         String cvsSplitBy = ",";

         try {

             br = new BufferedReader(new FileReader(csvFile));
             boolean firstLine = true;
             while ((line = br.readLine()) != null) {

                 // use comma as separator
                 String[] values = line.split(cvsSplitBy);
                 if(!firstLine) {
                	 ClassRoom holder = new ClassRoom(values[0],Integer.parseInt(values[1]));
                	 allClassRooms.add(holder);
                 }
                	 
             }
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             if (br != null) {
                 try {
                     br.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
    }
    
    //This method takes a String csvFile that is the path to a CSVfile that contains
    //all the students followed by what classes they are taking 
	public void createStudents(String csvFile) {
		
		//String csvFile = "/Users/Sam/eclipse-workspace/SimpleSchedule/lib/example.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] values = line.split(csvSplitBy);
                Student holder = new Student(values[0]);
                if(!firstLine) {
                	allStudents.add(holder);
                }
                int x = 0;
                for(String str: values) {
                	if(!firstLine) {
                		//System.out.print(str + " ");
                		if(x != 0) {
                			Classes clas = new Classes(str,x,"");
                			if(allClasses.contains(clas)) {
                				holder.addClasses(allClasses.get(allClasses.indexOf(clas)));
                				allClasses.get(allClasses.indexOf(clas)).addStudent(holder);
                			} else {
                				allClasses.add(clas);
                				holder.addClasses(clas);
                				clas.addStudent(holder);

                			}
                		}
                	}
                	x++;
                }
                firstLine = false;

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	//Used for the Aeries portal
	//Input: LIST STU SEC MST MST.PD MST.CN MST.TN MST.TS STU.ID IF MST.TS >= 5
	public void createAeries(String csvFile) {
		 BufferedReader br = null;
	        String line = "";
	        String csvSplitBy = ",";

	        try {

	            br = new BufferedReader(new FileReader(csvFile));
	            boolean firstLine = true;
	            while ((line = br.readLine()) != null) {
	            	if(!firstLine) {
	                // use comma as separator
	            		String[] values = line.split(csvSplitBy);
	            		Student holder = new Student(values[4]);
	            		if(this.allStudents.contains(holder)) {
	            			holder = this.allStudents.get(allStudents.indexOf(holder));
	            		} else {
	            			this.allStudents.add(holder);
	            		}
	            		Classes clas = new Classes(values[1],Integer.parseInt(values[0]),values[2]);
		                 if(allClasses.contains(clas)) {
		                	 holder.addClasses(allClasses.get(allClasses.indexOf(clas)));
	             			allClasses.get(allClasses.indexOf(clas)).addStudent(holder);
	             		} else {
	             			allClasses.add(clas);
	            			holder.addClasses(clas);
	            			clas.addStudent(holder);
	             		}
	            	}
	                firstLine = false;
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	}
	
	public void createClasses(String csvFile) {
		BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
            	if(!firstLine) {
            		String[] values = line.split(csvSplitBy);
            		String teacherName = values[0];
            		int x = 0;
            		for(String clas : values) {
            			if(x != 0) {
            				if(clas != null) {
            					allClasses.add(new Classes(clas,x-1,teacherName));
            				}
            			}
            			x++;
            		}
            	}
                firstLine = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	//This method takes a Set of Student objects and puts them into a CSV file with
	//their respective days in the class
	//Currently need to implement for 50% split
	public void convertToCSV(Set<Student> allStudents) {
		File csvOutputFile = new File(this.CSV_FILE_NAME);
		PrintWriter pw;
		StringBuffer csvHeader = new StringBuffer("");
		csvHeader.append("Name,Day\n");
		try {
			pw = new PrintWriter(csvOutputFile);
			pw.write(csvHeader.toString());
			for(Student stu : allStudents) {
				pw.print(stu.name + ",");
				if(stu.daysOn.getDay() == 0) {
					pw.println("Monday");
				} else if(stu.daysOn.getDay() == 1) {
					pw.println("Tuesday");
				} else if(stu.daysOn.getDay() == 2) {
					pw.println("Wednesday");
				} else if(stu.daysOn.getDay() == 3) {
					pw.println("Thursday");
				} else if(stu.daysOn.getDay() == 4) {
					pw.println("Friday");
				} else {
					pw.println("Error current day is :" + stu.daysOn.getDay());
				}
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//This method prints out all the students int the Set AllStudents
	//and prints out what classes they have
	public void studentsAndClasses() {
		for(Student stu: allStudents) {
            System.out.print(stu.name + " ");
            for(Classes clas: stu.allClasses) {
            	System.out.print(clas.className + " " + clas.classPeriod + " ");
            }
            System.out.println();
        }
	}
	
	//This method prints out all of the unique classes total
	//a unique class is one with either a different teacher period or className
    public void allUniqueClasses() {
    	for(Classes clas: allClasses) {
    		System.out.println(clas.className + " " + clas.classPeriod + " total Students: " + clas.allStudents.size());
    	}
    }
}
