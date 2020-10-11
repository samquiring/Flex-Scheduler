import java.util.*;

//Created by Sam Quiring

//This object day has the purpose of supplying the students with what day they are going to be in person
//The primary reason why I didn't just use a list of booleans is to keep track of the students previous
//set day when it is changed. This is so if the change is net negitive it can reverse the change
//This is all abstracted in this method so that all you need to call is setDay to set a day
//and reverseDay to go back to the old day
public class Day {
	//0 = monday, 1 = tuesday, 2 = wednesday, 3 = thursday, 4 = friday, 5 = saturday, 6 = sunday
	List<Integer> hasDay;
	boolean lockDay;
	
	//Initializes day with a given list of integers representing the days
	//and a boolean lockDay. This will be used for students that require certain days
	public Day(List<Integer> hasDay, boolean lockDay) {
		this.hasDay = hasDay;
		this.lockDay = lockDay;
	}
	
	//initializes day with Monday defaulted as in person or 1
	//and Tuesday defaulted as the previous valid input or -1
	//and lockDay defaulted to false
	public Day() {
		this.hasDay = new ArrayList<Integer>();
		for(int i = 0; i < 6; i++) {
			this.hasDay.add(0);
		}
		this.hasDay.set(0, 1);
		this.hasDay.set(1, -1);
		this.lockDay = false;
	}
	
	//if -1 is returned then student has no days in class currently
	//returns the first value of day that has class
	public int getDay() {
		if(this.hasDay.contains(1)) {
			return(this.hasDay.indexOf(1));
		}
		return -1;
	}
	
	//sets day to opposite what it currently is
	//if lockDay is true it doesn't set the day
	//TODO: should this return an error if called with lockDay?
	public void setDay(int day) {
		if(!this.lockDay) {
			this.changeOldDay();
			this.hasDay.set(day, 1);
		}
	}
	
	//changes the day back to what it was previously
	//if lockDay is true it doesn't do anything
	//TODO:Same as above
	private void changeOldDay() {
		if(!this.lockDay) {
			if(this.hasDay.contains(1)) {
				if(this.hasDay.contains(-1)) {
					this.hasDay.set(this.hasDay.indexOf(-1),0);
				}
				this.hasDay.set(this.hasDay.indexOf(1), -1);
			}
		}
	}
	
	//reverses the day back to the old day
	//and sets the current in person day to the old day
	//if lockDay is true it doesn't do anything
	public void reverseDay() {
		if(!this.lockDay) {
			if(this.hasDay.contains(-1)) {
				this.hasDay.set(this.hasDay.indexOf(1),0);
				if(this.hasDay.contains(-1)) {
					this.hasDay.set(this.hasDay.indexOf(-1), 1);
				}
			}
		}
	}
}
