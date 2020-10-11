import java.util.*;

public class Tester {
	static double range = .4;
	static int split = 3;
	public static void main(String args[]) throws Exception {
		CSVReader read = new CSVReader();
		//read.createStudents("/Users/Sam/eclipse-workspace/SimpleSchedule/lib/example.csv");
		read.createAeries("/Users/Sam/Desktop/PrintQueryToExcel_20201004_142033_31840ab.csv");
		System.out.println("total Students: " + read.allStudents.size());
		read.studentsAndClasses();
		read.allUniqueClasses();
		Set<Classes> allClasses = new HashSet<Classes>(read.allClasses);
		Set<Student> allStudents = new HashSet<Student>(read.allStudents);
		SchedulerV2 school = new SchedulerV2(range,allClasses,allStudents, split);
		school.run();
		//read.convertToCSV(school.allStudents);
	}
}