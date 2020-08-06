package kz.pcdp.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static kz.pcdp.util.Util.printResults;

public class StreamParallel {

  public static void main(String[] args) {

    Student[] studentArray = new Student[10000000];

    for (int i = 1; i < 10000001; i++) {
      Student student = new Student(i, Math.random() <= 0.5, Math.random() * i);
      studentArray[i - 1] = student;
    }

    for (int i = 1; i < 6; i++) {
      System.out.println("Run " + i);
      seqIteration(studentArray);
      parStream(studentArray);
    }
  }

  public static void seqIteration(Student[] studentArray) {
    long startTime = System.nanoTime();

    List<Student> students = new ArrayList<>();
    for (Student student : studentArray) {
      if (student.isCurrent) {
        students.add(student);
      }
    }

    long ageSum = 0;

    for (Student student : students) {
      ageSum += student.age;
    }
    double ret = (double) ageSum / (double) students.size();

    long timeInNanos = System.nanoTime() - startTime;
    printResults("seqIteration", timeInNanos, ret);
  }

  public static void parStream(Student[] studentArray) {
    long startTime = System.nanoTime();

    double ret = Stream.of(studentArray)
      .parallel()
      .filter(s -> s.isCurrent)
      .mapToDouble(a -> a.age)
      .average()
      .getAsDouble();

    long timeInNanos = System.nanoTime() - startTime;
    printResults("parStream", timeInNanos, ret);
  }
}

class Student {
  public int id;
  public boolean isCurrent;
  public double age;

  public Student(int id, boolean isCurrent, double age) {
    this.id = id;
    this.isCurrent = isCurrent;
    this.age = age;
  }
}
