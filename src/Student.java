import java.util.HashSet;
import java.util.Set;

//Created by Sam Quiring

//Creates and object Student that implements the Comparable interface
//to allow for sets and lists of Student
//Is used to generate a profile on a student that consists of
//if they must be on a certain day or if they have a proffered day
//a Set of all the classes they are in
//and a Day object consisting of what day(s) they have in person class on
public class Student implements Comparable<Student>{
	
	String name;
	boolean hasDay;
	Day daysOn;
	int updateCounter;
	Set<Classes> allClasses;
	boolean pref;				//if a student has a preference on day they need to come in on
	boolean requiredDay;		//if a student has a day that they need to come in on
	double percentIn;
	
	//Initializes the object student
	//requires a string name
	public Student(String name) {
		//this.percentIn = percentIn;
		this.name = name;
		this.hasDay = true;
		this.allClasses = new HashSet<Classes>();
		this.pref = false;
		this.requiredDay = false;
		this.daysOn = new Day();
	}
	
	//if the student has a day they prefer
	//requires string day
	//TODO: update for all days so if the student needs to come in every day
	//TODO: make this work with a multi day schedule
	public void hasPreference(String day) {
		this.pref = true;
		if(day.equalsIgnoreCase("t") || day.equalsIgnoreCase("tuesday")) {
			this.hasDay = false;
		}
	}
	
	//if the student is required to come in on a certain day
	//TODO: update for all days so if the student needs to come in every day
	//TODO: make this work with a multi day schedule
	public void hasRequired(String day) {
		this.pref = true;
		this.requiredDay = true;
		if(day.equalsIgnoreCase("t") || day.equalsIgnoreCase("tuesday")) {
			this.hasDay = false;
		}
	}
	
	//takes a Classes class and adds it to the students set of classes
	public void addClasses(Classes cur) {
		this.allClasses.add(cur);
	}
	
	//Works only for 50% capacity
	//updates the schedule ignoring preferences
	//but does take into account required days
	//returns an int that is if the update
	//was better or worse for balancing the schedule
	public int update(double max, double min) {
		if(!this.requiredDay) {
			updateCounter++;
			hasDay = !hasDay;
			int rangeCounter = updateClasses(max,min);
			return rangeCounter;
		}
		return 0;
	}
	
	//updates the student Schedule day on to a student given day
	//and returns an int that is if the update
	//was better or worse for balancing the schedule
	public int update(double max, double min, int day) {
		if(daysOn.hasDay.get(day) == 0) {
			daysOn.setDay(day);
			int rangeCounter = updateClassesAll(max,min);
			return rangeCounter;
		}
		daysOn.reverseDay();
		return 0;
	}
	
	//you set the day you want it to update to
	//takes an int day, a double max, and a double min
	public int updateSet(double max, double min, int day) {
		daysOn.setDay(day);
		return updateClassesAll(max,min);
	}
	
	//special update function that uses the compareChangeAdv
	//to evaluate if the update was better or worse for balancing the
	//schedule
	public double updateOutlier(double max, double min, int day) {
		if(daysOn.hasDay.get(day) == 0) {
			daysOn.setDay(day);
			double rangeCounter = 0.0;
			for(Classes clas: allClasses) {
				rangeCounter += clas.compareChangeAdv(max,min, 5);
				
			}
			return rangeCounter;
		}
		daysOn.reverseDay();
		return 0;
	}
	
	//only works for 50% capacity
	//updates the schedule taking in account if the student has a preference
	public int prefUpdate(double max, double min) {
		if(!pref) {
			updateCounter++;
			hasDay = !hasDay;
			int rangeCounter = updateClasses(max,min);
			return rangeCounter;
		}
		else {
			return 0;
		}
	}
	
	//only works for 50% capacity
	//accepts a max and min value for the change
	//adds up the compare change for every class the student is in 
	//and returns that value as an int
	public int updateClasses(double max, double min) {
		int rangeCounter = 0;
		for(Classes clas: allClasses) {
			clas.updateSchedule(this);
			rangeCounter += clas.compareChange(max,min);
			
		}
		return rangeCounter;
	}
	
	//for when more then two days
	//accepts a max and min value for the change
	//adds up the compare change for every class the student is in
	//on every day they could be in
	//and returns that value as an int
	public int updateClassesAll(double max, double min) {
		int rangeCounter = 0;
		for(Classes clas: allClasses) {
			rangeCounter += clas.compareChangeAllDays(max,min, 5);
			
		}
		return rangeCounter;
	}
	
	//prints out all the classes the student is in
	//prints the className and the ClassPeriod
	public void printClasses() {
		for(Classes clas: allClasses) {
			System.out.print(clas.className + " " + clas.classPeriod + "  ");
		}
		System.out.println();
	}
	
	//accepts an int day and checks if the student is in person on that day
	//returns a boolean of if they are in person that day
	public boolean hasDayCheck(int day) {
		return (this.daysOn.hasDay.get(day) == 1);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof Student)) return false;
		Student that = (Student)obj;
		if(this.compareTo(that) == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return(this.name.hashCode());
	}
	
	@Override
	public int compareTo(Student that) {
		return(this.name.compareTo(that.name));
	}

}
