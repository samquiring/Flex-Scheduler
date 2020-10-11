import java.util.*;


//Created by Sam Quiring

//This class creates a class Classes that implements the Comparable class
//to allow for creating lists and sets of Classes
//It is able to create a classroom class with a classname, teacher, and classPeriod
//It also holds a set of all the students in the class
//for only a 2 day split or 50% on each day it also holds a set of all students
//for one of the two days
public class Classes implements Comparable<Classes>{
	
	String className;
	Set<Student> allStudents;
	Set<Student> mwfStudents;
	int classPeriod;
	double percent;
	List<Double> percentAllDays;
	String teacher;
	ClassRoom room;
	int days = 5; //the amount of days that we are trying to work with
	int run; //The amount of times it has been run by the function
	
	//Primarly for testing purposes
	//Most likely will not be used in actual production
	//This initiziles Classes with a String className, and a set
	//of students in the class
	public Classes(String className, Set<Student> allStudents) {
		this.className = className;
		this.allStudents = new HashSet<Student>(allStudents);
		this.mwfStudents = new HashSet<Student>(allStudents);
		this.putInClasses();
		this.percent = getPercent();
		this.classPeriod = 0;
		this.teacher = "";
		this.percentAllDays = getPercentAllDays();
		this.run = 0;
	}
	
	//Used for Aeries
	public Classes(String className, int classPeriod) {
		this.className = className;
		this.allStudents = new HashSet<Student>();
		this.mwfStudents = new HashSet<Student>();
		this.percent = getPercent();
		this.classPeriod = classPeriod;
		this.teacher = "";
		this.percentAllDays = getPercentAllDays();
		this.run = 0;
	}
	
	//Current production implementation
	//Might be more optimized if teacher, classPeriod, and className
	//are all combined into a single string
	//TODO:Potential optimization
	
	//This initiziles Classes with a String className, an int ClassPeriod
	//and a String teacher name
	//it also initiziles percent to all days according to the int days above
	public Classes(String className, int classPeriod, String teacher) {
		this.className = className;
		this.allStudents = new HashSet<Student>();
		this.mwfStudents = new HashSet<Student>();
		this.classPeriod = classPeriod;
		this.teacher = teacher;
		this.percentAllDays = new ArrayList<Double>();
		for(int i = 0; i < 7; i++) {
			this.percentAllDays.add(0.0);
		}
		this.run = 0;
	}
	
	//This initiziles Classes with a String className, and integer classPeriod,
	//a String teacher, and a Classroom room
	//it also initiziles percent to all days according to the int days above
	//currently we are not using this in production but if ClassRooms need to become a factor
	//in the algorithm I already wrote an object for them and this would help initizile them
	public Classes(String className, int classPeriod, String teacher, ClassRoom room) {
		this.className = className;
		this.allStudents = new HashSet<Student>();
		this.mwfStudents = new HashSet<Student>();
		this.classPeriod = classPeriod;
		this.teacher = teacher;
		this.room = room;
		this.percentAllDays = new ArrayList<Double>();
		for(int i = 0; i < 7; i++) {
			this.percentAllDays.add(0.0);
		}
		this.run = 0;
	}
	

	//Adds a Student object to the classRoom set of Students
	public void addStudent(Student stu) {
		this.allStudents.add(stu);
		if(stu.hasDay) {
			this.mwfStudents.add(stu);
		}
	}
	
	//this method puts all the Student Objects in the Set allStudents
	//into this class in their object
	//This is useful if we are given a set of data with teachers and classes
	//and then all the students in the class so we can properly sort all the 
	//students into their classes
	private void putInClasses() {
		for(Student stu: this.allStudents) {
			stu.addClasses(this);
		}
	}
	
	//This method takes a Student and checks if he "hasDay"
	//and then sorts him accordingly 
	//This method is for 50% splits and is more effective
	//Then the multi day split so I have decided to keep it here
	public void updateSchedule(Student stu) {
		if(stu.hasDay) {
			mwfStudents.add(stu);
		} else {
			mwfStudents.remove(stu);
		}
	}
	
	//This method is only used for the 50% split and
	//Calculates the students on monday wednesday split
	//Returns double of the percent
	public double getPercent() {
		this.percent = (mwfStudents.size()/(double)allStudents.size());
		return this.percent;
	}
	
	
	//This method is only used for the 50% split and
	//Is used to evaluate if a student being changed
	//days is better or worse for all the classes they are in
	//returns 1 if change makes it closer to being in range
	//returns -1 if change makes it further from being in range
	//returns 0 if already in range and it doesn't knock it out
	//TODO: I can really hone in on proper class metrics with this method
	//if i figure out the proper way to give numbers back i can get really
	//close to 50% for all classes
	public int compareChange(double max, double min) {
		double newPercent  = ((double)mwfStudents.size()/(double)allStudents.size());
		if(this.percent > max) {
			if(newPercent < this.percent) {
				return 1;
			}
			return -1;
		} else if(this.percent < min) {
			if(newPercent > this.percent) {
				return 1;
			}
			return -1;
		} else if(newPercent > max || newPercent < min) {
			return -1;
		}
		return 0;
		
	}
	
	//This method takes a double maxPercent and a double minPercent
	//that represent the max and min that the classes percents should be
	//within. It also takes an int totalDays that is the amount of days
	//the student should be evaulated over. i.e if 25% capacity totalDays = 4
	//It returns an int that is positive or 0 if the class is closer in range
	//on all the days or negative if it is further from the range
	public int compareChangeAllDays(double max, double min, int totalDays) {
		int total = 0;
		double center = max+min/2;
		for(int i = 0; i < totalDays; i++) {
			double oldPercent = this.percentAllDays.get(i);
			double newPercent = this.getPercentHard(i);
			if(oldPercent > max) {
				if(newPercent < oldPercent) {
					 total++;
				} else if(newPercent > oldPercent){
					total--;
				}
			} else if(oldPercent < min) {
				if(newPercent > oldPercent) {
					total++;
				} else if(newPercent < oldPercent){
					total--;
				}
			} else if(newPercent > max || newPercent < min) {
				total -= 2;
			} else if(newPercent - center < oldPercent - center) {
				total ++;
			}
		}
		return total;
	}
	

	//This method takes a double maxPercent and a double minPercent
	//that represent the max and min that the classes percents should be
	//within. It also takes an int totalDays that is the amount of days
	//the student should be evaulated over. i.e if 25% capacity totalDays = 4
	//it is a more advanced version of the previous compareChanges and is
	//only used when compareChange is not sufficient
	//It works off the same principle as compare change except it weights
	//classes based off of how far from the center of max and min they are
	//so classes moving away from the center that are really close dont 
	//matter as much as classes moving away from the center that are 
	//already far away from it
	//It returns a double that is negative if the net weight is further away
	//or positive if it is closer
	public double compareChangeAdv(double max, double min, int totalDays) {
		double total = 0;
		double center = (max + min)/2.0;
		for(int i = 0; i < totalDays; i++) {
			double oldPercent = this.percentAllDays.get(i);
			double newPercent = this.getPercentHard(i);
			if(oldPercent > center && newPercent > center) {
				if(oldPercent > newPercent) {
					total += newPercent - center;
				} else if(oldPercent < newPercent) {
					total -= newPercent - center;
				}
			} else if(oldPercent <= center && newPercent <= center) {
				if(oldPercent < newPercent) {
					total+= center - newPercent;
				} else if(oldPercent > newPercent) {
					total -= center - newPercent;
				}
			}
		}
		return total;
	}
	
	//This method takes an integer day
	//and finds the percent of students
	//that are currently going to school
	//on that day and returns that value
	//as a double
	public double getPercentHard(int day) {
		int studentsTotal = this.allStudents.size();
		int studentIn = 0;
		for(Student stu: this.allStudents) {
			if(stu.daysOn.hasDay.get(day) == 1) {
				studentIn++;
			}
		}
		return((double)studentIn/(double)studentsTotal);
	}
	
	//This method finds the percent of students going to class on every day
	//and returns a List of Doubles of the percents on each day
	//with the index of each double denoting what day it is and the double
	//the percent of students in school on that day
	//TODO: add variable passed into to tell program how many days it is looking at
	public List<Double> getPercentAllDays(){
		List<Double> percents = new ArrayList<Double>();
		int studentsTotal = this.allStudents.size();
		for(int i = 0; i < days; i++) {
			int studentIn = 0;
			for(Student stu: this.allStudents) {
				if(stu.daysOn.hasDay.get(i) == 1) {
					studentIn++;
				}
			}
			percents.add((double)studentIn/(double)studentsTotal);
		}
		this.percentAllDays = percents;
		return(percents);
	}
	
	//Only works on 50% split
	//checks if the class percent is in between the range
	//of a given double max and a double min
	//returns true if in range false if not
	public boolean inRange(double max, double min) {
		double percent = this.getPercent();
		if(percent < min || percent > max) {
			return false;
		}
		return true;
	}
	
	//takes a double max, a double min and an int totalDays
	//Evaluates if every day from day = 0 to totalDays - 1
	//if all days are in range then returns true
	//else if any one day is not in range it returns false
	public boolean inRange2(double max, double min, int totalDays) {
		List<Double> percents = this.getPercentAllDays();
		for(int i = 0; i < totalDays; i++) {
			if(percents.get(i) < min || percents.get(i) > max) {
				return false;
			}
		}
		return true;
	}
	
	//takes a double max, a double min and an int day
	//checks if the percent of students in class on the day
	//is within the range
	//if not returns false
	//else returns true
	public boolean inRangeDay(double max, double min, int day) {
		List<Double> percents = this.getPercentAllDays();
		if(percents.get(day) < min || percents.get(day) > max) {
			return false;
		}
		return true;
	}
	
	//takes a set of Students representing all students
	//in the class and sets that to the set allStudents
	public void setAllStudents(Set<Student> students) {
		this.allStudents = students;
		
	}
	
	//prints out all student names in the class
	public void printStudents() {
		for(Student stu: this.allStudents) {
			System.out.print(stu.name + " ");
		}
		System.out.println();
	}
	
	//checks if a given object equals this current Classes object
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof Classes)) return false;
		Classes that = (Classes)obj;
		if(this.compareTo(that) == 0) {
			return true;
		}
		return false;
	}
	
	//creates a unique hashcode for this object Classes
	@Override
	//This isn't the most optimized version of hashing
	//If code is too slow find how to write this with .hashCode
	//TODO:Optimize
	public int hashCode() {
		return(Objects.hash(this.className,this.teacher,this.classPeriod));
	}
	
	//compares a Classes that to this Object classes
	@Override
	public int compareTo(Classes that) {
		int name = this.className.compareTo(that.className);
		if(name != 0) {
			return name;
		} else {
			int teacher = this.teacher.compareTo(that.teacher);
			if(teacher != 0) {
				return teacher;
			}
			return(this.classPeriod - that.classPeriod);
		}
	}
}
