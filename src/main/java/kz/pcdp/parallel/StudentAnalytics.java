package kz.pcdp.parallel;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;

public final class StudentAnalytics {

  public double averageAgeOfEnrolledStudentsImperative(final Student[] studentArray) {
    List<Student> activeStudents = new ArrayList<>();

    for (Student s : studentArray) {
      if (s.checkIsCurrent()) {
        activeStudents.add(s);
      }
    }

    double ageSum = 0.0;
    for (Student s : activeStudents) {
      ageSum += s.getAge();
    }

    return ageSum / (double) activeStudents.size();
  }

  public double averageAgeOfEnrolledStudentsParallelStream(final Student[] studentArray) {
    return Arrays.stream(studentArray)
      .parallel()
      .filter(Student::checkIsCurrent)
      .mapToDouble(Student::getAge)
      .average()
      .getAsDouble();
  }

  public String mostCommonFirstNameOfInactiveStudentsImperative(final Student[] studentArray) {
    List<Student> inactiveStudents = new ArrayList<Student>();

    for (Student s : studentArray) {
      if (!s.checkIsCurrent()) {
        inactiveStudents.add(s);
      }
    }

    Map<String, Integer> nameCounts = new HashMap<String, Integer>();

    for (Student s : inactiveStudents) {
      if (nameCounts.containsKey(s.getFirstName())) {
        nameCounts.put(s.getFirstName(),
          new Integer(nameCounts.get(s.getFirstName()) + 1));
      } else {
        nameCounts.put(s.getFirstName(), 1);
      }
    }

    String mostCommon = null;
    int mostCommonCount = -1;
    for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
      if (mostCommon == null || entry.getValue() > mostCommonCount) {
        mostCommon = entry.getKey();
        mostCommonCount = entry.getValue();
      }
    }

    return mostCommon;
  }

  public String mostCommonFirstNameOfInactiveStudentsParallelStream(final Student[] studentArray) {
    Map<String, Long> namesCountMap = Arrays.stream(studentArray)
      .parallel()
      .filter(s -> !s.checkIsCurrent())
      .map(Student::getFirstName)
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    return namesCountMap.entrySet()
      .stream()
      .max(Map.Entry.comparingByValue())
      .map(Map.Entry::getKey)
      .orElseGet(null);
  }

  public int countNumberOfFailedStudentsOlderThan20Imperative(final Student[] studentArray) {
    int count = 0;
    for (Student s : studentArray) {
      if (!s.checkIsCurrent() && s.getAge() > 20 && s.getGrade() < 65) {
        count++;
      }
    }
    return count;
  }

  public int countNumberOfFailedStudentsOlderThan20ParallelStream(final Student[] studentArray) {
    long count = Arrays.stream(studentArray)
      .parallel()
      .filter(s -> !s.checkIsCurrent() && s.getAge() > 20 && s.getGrade() < 65)
      .count();
    return toIntExact(count);
  }
}
