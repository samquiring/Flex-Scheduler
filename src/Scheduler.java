import java.util.*;
import java.lang.Math;


//Created by Sam Quiring

//Is an Object Scheduler that does all the heavy lifting in terms of algorithms
//This class uses the given classes and students and trys to find an ideal schedule
//within the constraints

public class Scheduler {
	
	double max;
	double min;
	double maxAllowed;		//the max range of students allowed before turning off other preferences
	double minAllowed;
	double center;
	Set<Classes> allClasses;
	Set<Student> allStudents;
	boolean prefKeep;
	int turnOffCounter;		//keeps track of how many times turn off has been called
	int totalDays;
	
	
	//Initializes the class Scheduler
	//Takes a double range that represents the max range the
	//school wants for their classrooms capacity, takes a set allClasses
	//that is all the classes in the school that this program is to work on,
	//takes a Set of Students that is all the students
	//takes an int split that represents the day split
	//I.E a 2 day split or split = 2 would be 50% capacity
	//a 4 day split or split = 4 would be 25% capacity
	public Scheduler(double range, Set<Classes> allClasses, Set<Student> allStudents, int split) {
		this.max = 1/(double)split;
		this.min = 1/(double)split;
		//this.max = 0.3;
		//this.min = 0.1;
		this.center = 1/(double)split;
		this.maxAllowed = max + range/2;
		this.minAllowed = min - range/2;
		this.allClasses = new HashSet<Classes>(allClasses);
		this.turnOffCounter = 0;
		this.allStudents = allStudents;
		this.totalDays = split;
	}
	
	//a much simpler function that only works for a 50 percent split
	//use this if total Days = 2
	//It runs through all the classes and students and attempts to create
	//a schedule that fits all the constraints
	//If a schedule cannot be made in the constraints
	//It throws an Exception
	public void run() throws Exception {
		while(!allInRange(allClasses)) {
			
			Classes startingClass = checkForRange(allClasses);
			for(Classes clas: allClasses) {
				System.out.println("class: " + clas.className + " period " + clas.classPeriod + " current range value: " + clas.getPercent());
			}
			updateIntoRangePref(startingClass);
			if(!startingClass.inRange(max, min)) {
				System.out.println();
				break;
			}
			System.out.println();
		}
		if(!allInRange(allClasses)) {
			this.max = this.max + 0.01;
			this.min = this.min - 0.01;
			if(max > maxAllowed || min < minAllowed) {
				this.turnOff();
			}
			run();
		}
		for(Classes clas: allClasses) {
			System.out.println("class: " + clas.className + " period " + clas.classPeriod + " current range value: " + clas.getPercent());
		}
		System.out.println("Everything is in range!");
		
	}
	
	//runs for any amount of days 1 - 7 total
	//It runs through all the classes and students and attempts to create
	//a schedule that fits all the constraints
	//If a schedule cannot be made in the constraints
	//It throws an Exception
	public void run2() throws Exception {
		//TODO: make a more perm fix
		int day = 0;
		while(!allInRange2(allClasses,totalDays) && day < totalDays) {
			while(!allInRangeDay(allClasses, day)) {
				//Classes startingClass = checkForRangeDay(allClasses, day);
				Classes startingClass = this.findWorstRangeDay(allClasses, day); //slower but closer range
				double holder = startingClass.getPercentHard(day);
				updateIntoRangePref2(startingClass, day);
				if(Math.abs(center - holder) <= Math.abs(center - startingClass.getPercentHard(day))) {
					System.out.println();
					break;
				}
			}
			day++;
			for(int days = 0; days < totalDays; days++) {
				System.out.println("Current day is: " + days);
				for(Classes clas: allClasses) {
					System.out.println("class: " + clas.className + " period " + clas.classPeriod + " current range value: " + clas.getPercentHard(days));
				}
				System.out.println("Total Student Population: " + this.totalStudentsDay(days));
			}
			System.out.println("all in range: " + allInRange2(allClasses,totalDays));
			System.out.println();
				//this.max = this.max + 0.01;
				//this.min = this.min - 0.01;
				if(max > maxAllowed || min < minAllowed) {
					//this.removeOutliers();
					//throw new Exception("could not fit constraints");
					//this.turnOff();
				} else {
					run2();
				}
			}
	}
	
	//finds the worse cases in every day and attempts to fix them
	private void removeOutliers() {
		for(int day = 0; day < this.totalDays; day++) {
			while(!allInRangeDay(allClasses, day)) {
				//Classes startingClass = checkForRangeDay(allClasses, day);
				Classes startingClass = this.findWorstRangeDay(allClasses, day); //slower but closer range
				double percentStart = startingClass.getPercentHard(day);
				fixOutlier(startingClass, day);
				if(percentStart == startingClass.getPercentHard(day)) {
					System.out.println();
					break;
				}
			}
		}
		for(int days = 0; days < totalDays; days++) {
			System.out.println("Current day is: " + days);
			for(Classes clas: allClasses) {
				System.out.println("class: " + clas.className + " period " + clas.classPeriod + " current range value: " + clas.getPercentHard(days));
			}
			System.out.println("Total Student Population: " + this.totalStudentsDay(days));
		}
	}
	
	//the purpose of this method is to one by one turn off preferences until we get
	//a model that is within the allowed range
	//as more preferences are added more else ifs can be put in
	private void turnOff() throws Exception {
		this.min = 0.5;		//resets the min and max to try again
		this.max = 0.5;
		if(this.turnOffCounter == 0) {
			this.prefKeep = false;
		} else {
			throw new Exception("All preferences have been turned off. Range is not possible with data set");
		}
	}
	
	//only works for a 50% split
	//takes a set of Classes and returns true if all the classes are within range
	public boolean allInRange(Set<Classes> allClass) {
		for(Classes clas: allClass) {
			if(!clas.inRange(max,min)) {
				return false;
			}
		}
		return true;
	}
	
	//takes a set of Classes and an int of the totalDays
	//and returns true if all the classes are within range for the given days
	public boolean allInRange2(Set<Classes> allClass, int totalDays) {
		for(Classes clas: allClass) {
			if(!clas.inRange2(max,min, totalDays)) {
				return false;
			}
		}
		return true;
	}
	
	//takes a set of Classes and an int day
	//returns true if all the classes are within range for the given day
	public boolean allInRangeDay(Set<Classes> allClass, int day) {
		for(Classes clas: allClass) {
			if(!clas.inRangeDay(max,min, day)) {
				return false;
			}
		}
		return true;
	}
	
	//only works for 50% capacity
	//returns the first instance of a class that is not within range
	public Classes checkForRange(Set<Classes> allClass) {
		for(Classes clas: allClass) {
			if(!clas.inRange(max,min)) {
				return clas;
			}
		}
		System.out.print("All Classes are within the range!!!");
		return null;
	}
	
	
	//returns the first instance of a class that is not within range for a given day
	public Classes checkForRangeDay(Set<Classes> allClass, int day) {
		for(Classes clas: allClass) {
			if(!clas.inRangeDay(max, min, day)) {
				return clas;
			}
		}
		System.out.print("All Classes are within the range!!!");
		return null;
	}
	
	//finds the biggest outlier class for a given day and returns it
	public Classes findWorstRangeDay(Set<Classes> allClass, int day) {
		double worstHolder = this.center;
		Classes worstClass = null;
		for(Classes clas: allClass) {
			if(Math.abs(clas.getPercentHard(day) - this.center) > Math.abs(worstHolder - this.center)) {
				worstClass = clas;
				worstHolder = clas.getPercentHard(day);
			}
		}
		return worstClass;
	}
	
	//only works for 50% capacity
	//takes a Classes clas and changes students around until it is within range
	public void updateIntoRange(Classes clas) {
		Set<Student> allStudent = clas.allStudents;
		for(Student stu: allStudent) {
			if(!clas.inRange(max,min)) {
				int range = stu.update(max,min);
				if(range < 0) {
					stu.update(max,min);
				}
			} else {
				break;
			}
		}
	}
	
	
	//only works for 50% capacity
	//takes a Classes clas and changes students around until it is within range
	//accounts for students preferences and works around it
	public void updateIntoRangePref(Classes clas) {
		Set<Student> allStudent = clas.allStudents;
		for(Student stu: allStudent) {
			if(!clas.inRange(max,min)) {
				int range = stu.prefUpdate(max,min);
				if(range <= 0) {
					stu.prefUpdate(max,min);
				}
			} else {
				break;
			}
		}
	}
	
	//TODO: Try and get it to go as close to the center as possible
	//instead of being happy with just being in range
	public void updateIntoRangePref2(Classes clas, int day) {
		Set<Student> allStudent = clas.allStudents;
		for(Student stu: allStudent) {
			if(!clas.inRangeDay(max,min,day)) {
				int range = stu.update(max,min, day);
				if(range < 0) {
					stu.update(max,min, day);
				}
			} else {
				break;
			}
		}
	}
	
	//takes a Classes clas and an int day
	//attempts to push the clas closer
	//to the center of the range on that given day
	public void fixOutlier(Classes clas, int day) {
		Set<Student> allStudent = clas.allStudents;
		for(Student stu: allStudent) {
			if(!clas.inRangeDay(max,min,day)) {
				double range = stu.updateOutlier(max,min, day);
				if(range < 0.0) {
					stu.updateOutlier(max,min, day);
				}
			} else {
				break;
			}
		}
	}
	
	//takes an int day returns a double
	//that is the total students at the school
	//in person on the given day
	public double totalStudentsDay(int day) {
		int counter = 0;
		for(Student stu: this.allStudents) {
			if(stu.hasDayCheck(day)) {
				counter++;
			}
		}
		return((double)counter/(double)this.allStudents.size());
	}
}
