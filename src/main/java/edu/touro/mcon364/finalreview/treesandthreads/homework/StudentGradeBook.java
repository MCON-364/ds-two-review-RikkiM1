package edu.touro.mcon364.finalreview.treesandthreads.homework;

import java.util.*;
import java.util.stream.*;

/**
 * Homework 2 - Student GradeBook (TreeMap + Streams + DoubleSummaryStatistics)
 *
 * Scenario: a course has many students. Each student is identified by name and
 * has a numeric grade (0.0 to 100.0). The gradebook must support sorted lookup
 * and statistical analysis.
 *
 * Before coding, think about:
 * - Should the map key be the student name or the grade? Why does it matter?
 * - What does TreeMap.firstEntry() return? What does lastEntry() return?
 * - How do we turn a numeric score into a letter grade inside a stream?
 *
 * Requirements:
 * - The constructor receives a Map of student name to grade.
 * - buildSortedGradeBook() returns a TreeMap so students are iterated alphabetically.
 * - getStatistics() returns DoubleSummaryStatistics over all grades.
 * - getLetterGradeDistribution() returns a TreeMap counting how many students
 *   received each letter grade: A (90+), B (80-89), C (70-79), D (60-69), F (below 60).
 * - getTopStudents(n) returns the names of the n highest-scoring students, highest first.
 * - getStudentsInScoreRange(low, high) returns a sorted list of student names
 *   whose grade is in [low, high] inclusive.
 *
 * Do not use explicit loops. Use streams and collectors.
 */
public class StudentGradeBook {

    private final Map<String, Double> grades;

    public StudentGradeBook(Map<String, Double> grades) {
        // TODO: validate non-null; store a defensive copy
        if (grades == null)
            throw new NullPointerException();
//use hash map bec not require sorting
        this.grades = new HashMap<>(grades);

    }

    /**
     * Returns a TreeMap so iteration visits students alphabetically.
     *
     */
    public TreeMap<String, Double> buildSortedGradeBook() {
        // TODO
        return new TreeMap<>(grades);
    }

    /**
     * Returns summary statistics (count, min, max, average, sum) over all grades.
     *
     */
    public DoubleSummaryStatistics getStatistics() {
        // TODO
        // grades.values() returns a Collection<Double> of all the student grades
        return grades.values()//A Map does not have a .stream() method directly.
        //You can’t do grades.stream() because Map doesn’t implement Stream-related methods.
                //  convert the stream of Double objects to a primitive double stream
                // mapToDouble(Double::doubleValue) unboxes each Double to a primitive double
                .stream()
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
    }

    /**
     * Returns a TreeMap counting students per letter grade.
     *
     */
    public TreeMap<String, Long> getLetterGradeDistribution() {
        // TODO
        return grades.values()
                .stream()
                .map(g -> g >= 90 ? "A" :
                        g >= 80 ? "B" :
                                g >= 70 ? "C" :
                                        g >= 60 ? "D" : "F")
                // Group identical letter grades together,
                // store results in a TreeMap,
                // and count how many grades are in each group
                .collect(Collectors.groupingBy(
                        letter -> letter,
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

    /**
     * Returns the names of the n highest-scoring students, highest first.
     */
    public List<String> getTopStudents(int n) {
        return grades.entrySet()
                //the method converts the map into entries,
                // sorts them by grade (highest first),
                // keeps only the top n,
                // extracts their names,
                // and returns them as a list.
                              .stream()
               .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(n)
                .map(Map.Entry::getKey)
                 .toList();   }
    /**
     * Returns a sorted list of names whose grade falls in [low, high] inclusive.
     *
     */
    public List<String> getStudentsInScoreRange(double low, double high) {
        // TODO
        return buildSortedGradeBook()        // get students sorted alphabetically
                .entrySet()
                .stream()
                // filter by grade range
                .filter(e -> e.getValue() >= low && e.getValue() <= high)
                // extract names
                .map(Map.Entry::getKey)
                // collect into a list
                .toList();
    }
}
