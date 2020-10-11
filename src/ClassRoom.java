import java.util.*;

public class ClassRoom{
	List<Classes> classesIn;
	String className;
	int classSize;
	
	public ClassRoom(String className,int classSize ,List<Classes> classesIn) {
		this.className = className;
		this.classesIn = classesIn;
		this.classSize = classSize;
	}
	
	public ClassRoom(String className, int classSize) {
		this.className = className;
		this.classesIn = new ArrayList<Classes>();
		this.classSize = classSize;
		for(int x = 0; x < 10; x++) {
			this.classesIn.add(null);
		}
	}
	
	public void setClassesIn(List<Classes> classesIn) {
		this.classesIn = classesIn;
	}
	
	public void addClasses(Classes clas) {
		this.classesIn.set(clas.classPeriod, clas);
	}

}
