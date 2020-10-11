import java.util.*;
import java.lang.Math;

//Created by Sam Quiring

//This is the class that does all the heavy lifting
//in terms of computations
//it takes all the classes and tries to find the
//best way to sort all of them to fit the criteria
//the only public method in this class is run
public class SchedulerV2 {
	
	double max;
	double min;
	double maxAllowed;		//the max range of students allowed before turning off other preferences
	double minAllowed;
	double center;
	Set<Classes> allClasses;
	Set<Classes> smallClasses;
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
	public SchedulerV2(double range, Set<Classes> allClasses, Set<Student> allStudents, int split) {
		this.max = 1/(double)split;
		this.min = 1/(double)split;
		this.center = 1/(double)split;
		this.maxAllowed = max + range/2;
		this.minAllowed = min - range/2;
		this.allClasses = new HashSet<Classes>(allClasses);
		this.turnOffCounter = 0;
		this.allStudents = allStudents;
		this.totalDays = split;
		this.smallClasses = new HashSet<Classes>(allClasses);
	}
	
	//runs for any amount of days 1 - 7 total
	//It runs through all the classes and students and attempts to create
	//a schedule that fits all the constraints
	//If a schedule cannot be made in the constraints
	//It throws an Exception
	//TODO: Throw exception if any class is not in range
	public void run() throws Exception{
		if(this.totalDays == 2) {
			run50();
			return;
		}
		int day = 0;
		while(!this.isInRange() && day != this.totalDays) {
			while(!this.isInRangeDay(day)) {
				Classes worst = this.findWorstClass(day);
				if(worst == null) {
					worst = this.checkForRangeDay(day);
				}
				double worstPercent = worst.percentAllDays.get(day);
				this.updateClass(worst, day);
				if(worstPercent == worst.percentAllDays.get(day)) {
					break;
				}
				
			}
			for(int days = 0; days < totalDays; days++) {
				System.out.println("Current day is: " + days);
				for(Classes clas: allClasses) {
					System.out.println("class: " + clas.className + " period " + clas.classPeriod + " current range value: " + clas.getPercentHard(days));
				}
				System.out.println("Total Student Population: " + this.totalStudentsDay(days));
			}
			day++;
		}
		if(!this.isInRange()) {
			this.max += 0.01;
			this.min -= 0.01;
			if(this.max > this.maxAllowed || this.min < this.minAllowed) {
				throw new Exception("Range is not possible with data set");
			} else {
				run();
			}
		}
	}
	
	//a much simpler function that only works for a 50 percent split
	//It is called by the run function if split is = 2 or a 2 day split
	//use this if total Days = 2
	//It runs through all the classes and students and attempts to create
	//a schedule that fits all the constraints
	//If a schedule cannot be made in the constraints
	//It throws an Exception
	//TODO: Throw exception if not in range
	private void run50() throws Exception {
		while(!allInRange(allClasses)) {
			
			Classes startingClass = checkForRange(allClasses);
			if(startingClass == null) break;	//I hate this so much but its the best fix for the problem of classes affecting each other ugh.s
			updateIntoRangePref(startingClass);
			startingClass.run += 1;
			if(!startingClass.inRange(max, min)) {
				System.out.println();
				break;
			}
			System.out.print("class: " + startingClass.className + " period " + startingClass.classPeriod + " current range value: " + startingClass.getPercent());
			System.out.println();
		}
		if(!allInRange(allClasses)) {
			for(Classes clas : allClasses) {
				clas.run = 0;
			}
			this.max = this.max + 0.01;
			this.min = this.min - 0.01;
			if(max > maxAllowed || min < minAllowed) {
				this.turnOff();
				return;
			}
			run50();
		}
		System.out.println("All classes: ");
		System.out.println();
		for(Classes clas: allClasses) {
				System.out.println("class: " + clas.className + " period " + clas.classPeriod + " students in class " + clas.allStudents.size() + " current range value: " + clas.getPercent());
		}
		System.out.println("total students on monday: " + this.totalPercentStudents());
		//System.out.println("Everything is in range!");
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
		turnOffCounter++;
	}	
	
	//checks if all classes are in range on all days.
	//returns true if all classes in range and false if nots
	private boolean isInRange() {
		for(int i = 0; i < this.totalDays; i++) {
			if(!isInRangeDay(i)) {
				return false;
			}
		}
		return true;
	}
	
	//checks if all classes on a specific day
	//are in range. returns true if all in range
	//and false if not
	private boolean isInRangeDay(int day) {
		for(Classes clas: this.allClasses) {
			if(!clas.inRangeDay(this.max, this.min, day)) {
				return false;
			}
		}
		return true;
	}
	
	
	//finds and returns the class furthest away from the
	//center range on a given day and returns that Classes object
	private Classes findWorstClass(int day) {
		Classes worst = null;
		double worstPercent = 0;
		for(Classes clas : allClasses) {
			double percent = clas.getPercentHard(day);
			if(Math.abs(center - percent) > Math.abs(center - worstPercent)) {
				worst = clas;
				worstPercent = percent;
			}
		}
		return worst;
	}
	
	//finds and returns the first class on a given day
	//that is not in the specified range
	private Classes checkForRangeDay(int day) {
		for(Classes clas: allClasses) {
			if(!clas.inRangeDay(max, min, day)) {
				return clas;
			}
		}
		System.out.print("All Classes are within the range!!!");
		return null;
	}
	
	//must run class.getPercentAllDays() before so that the percents are updated
	//finds the day that has the lowest percentage for a given Classes object
	//and returns an int representing the day
	private int findLowestDay(Classes clas) {
		List<Double> holder = clas.percentAllDays;
		int day = 0;
		int worstDay = -1;
		double worst = 1;
		for(double percent : holder) {
			if(percent < worst) {
				worst = percent;
				worstDay = day;
			}
			day++;
		}
		return worstDay;
	}
	
	//attempts to update a given Classes object on a given day
	//into the range specified above
	private boolean updateClass(Classes clas, int day) {
		clas.getPercentAllDays(); //runs to update the percent values so they are recent
		if(clas.percentAllDays.get(day) < center) {
			for(Student stu : clas.allStudents) {	
				if(clas.inRangeDay(max, min, day)) {
					return true;
				}
				int range = stu.update(max,min, day);
				if(range < 0) {
					stu.update(max,min, day);
				} else {
					break;
				}
			}
		} else {
			int dayUpdate = findLowestDay(clas);
			for(Student stu : clas.allStudents) {	
				if(clas.inRangeDay(max, min, day)) {
					return true;
				}
				int range = stu.updateSet(max,min, dayUpdate);
				if(range < 0) {
					stu.update(max,min, day);
				}
			}
		}
		return false;
	}
	
	//finds the total students in a given day
	//returns a double that is the percent
	//of the student population that is in person
	//for that given day
	private double totalStudentsDay(int day) {
		int counter = 0;
		for(Student stu: this.allStudents) {
			if(stu.hasDayCheck(day)) {
				counter++;
			}
		}
		return((double)counter/(double)this.allStudents.size());
	}
	
	//only works for 50% capacity
	//checks to see if all the given Classes objects are
	//within the specified range and if so returns true
	//if not returns false
	private boolean allInRange(Set<Classes> allClass) {
		for(Classes clas: allClass) {
			if(!clas.inRange(max,min)) {
				return false;
			}
		}
		return true;
	}
	
	//only works for 50% capacity
	//finds and returns the first class
	//that is not in the specified range
	private Classes checkForRange(Set<Classes> allClass) {
		for(Classes clas: allClass) {
			if(!clas.inRange(max,min) && clas.run < 3) {
				return clas;
			}
		}
		System.out.print("All Classes are within the range!!!");
		return null;
	}
	
	
	//only works for 50% capacity
	//takes a Classes clas and changes students around until it is within range
	private void updateIntoRange(Classes clas) {
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
	private void updateIntoRangePref(Classes clas) {
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
	
	//only works for 50% capacity
	//sends back the percent of all students on monday
	private double totalPercentStudents() {
		int count = 0;
		for(Student stu : this.allStudents) {
			if(stu.hasDay) {
				count++;
			}
		}
		return((double)count/(double)this.allStudents.size());
		
	}
}
