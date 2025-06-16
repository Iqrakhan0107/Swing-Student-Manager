public class Student {
    String name;
    String age;
    String gender;
    String course;
    boolean enrollled;
    String notes;

    public Student(String name, String age, String gender, String course, boolean enrollled, String notes) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.course = course;
        this.enrollled = enrollled;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n"
             + "Age: " + age + "\n"
             + "Gender: " + gender + "\n"
             + "Course: " + course + "\n"
             + "Enrolled: " + (enrollled ? "Yes" : "No") + "\n"
             + "Notes: " + notes + "\n"
             + "---------------------------";
    }

    public String toFileFormat() {
        return "Name: " + name + "\n"
             + "Age: " + age + "\n"
             + "Gender: " + gender + "\n"
             + "Course: " + course + "\n"
             + "Enrolled: " + (enrollled ? "Yes" : "No") + "\n"
             + "Notes: " + notes + "\n"
             + "---------------------------\n";
    }
}
