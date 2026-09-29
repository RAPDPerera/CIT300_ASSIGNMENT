package studentrecords;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Console-facing operations for Member 1's student-record menu options:
 *   1. Add Student Record
 *   2. Update Student Record
 *   3. Delete Student Record
 *   4. Display All Records using Linked List
 *
 * All console formatting lives here, kept separate from StudentLinkedList's
 * pure data-structure logic. Uses one shared Scanner (passed in) and only
 * line-based input (nextLine()) to avoid nextInt()/nextDouble() newline bugs.
 * This class never closes the Scanner/System.in, so it can be safely reused
 * inside a larger group menu.
 */
public class StudentConsole {

    private final StudentLinkedList list;
    private final Scanner scanner;

    public StudentConsole(StudentLinkedList list, Scanner scanner) {
        this.list = list;
        this.scanner = scanner;
    }

    /** Menu option 1: Add Student Record. */
    public void handleAdd() {
        System.out.println("\n--- Add Student Record ---");
        try {
            String id = readLine("Student ID: ");
            String name = readLine("Name: ");
            String programme = readLine("Programme: ");
            double marks = readMarks("Marks (0-100): ");

            Student student = new Student(id, name, programme, marks);
            boolean added = list.addStudent(student);
            if (added) {
                System.out.println("Student added successfully.");
            } else {
                System.out.println("A student with ID '" + id.trim() + "' already exists. Add cancelled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add student: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println("Input ended unexpectedly. Add cancelled.");
        }
    }

    /** Menu option 2: Update Student Record. */
    public void handleUpdate() {
        System.out.println("\n--- Update Student Record ---");
        try {
            String id = readLine("Student ID to update: ");
            Student existing = list.findById(id);
            if (existing == null) {
                System.out.println("No student found with ID '" + id.trim() + "'.");
                return;
            }

            System.out.println("Current record: " + describe(existing));
            String name = readLine("New Name: ");
            String programme = readLine("New Programme: ");
            double marks = readMarks("New Marks (0-100): ");

            boolean updated = list.updateStudent(id, name, programme, marks);
            System.out.println(updated ? "Student updated successfully."
                    : "Update failed: student not found.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not update student: " + e.getMessage());
            System.out.println("The original record was left unchanged.");
        } catch (NoSuchElementException e) {
            System.out.println("Input ended unexpectedly. Update cancelled.");
        }
    }

    /** Menu option 3: Delete Student Record. */
    public void handleDelete() {
        System.out.println("\n--- Delete Student Record ---");
        try {
            String id = readLine("Student ID to delete: ");
            Student removed = list.deleteStudent(id);
            if (removed != null) {
                System.out.println("Deleted: " + describe(removed));
            } else {
                System.out.println("No student found with ID '" + id.trim() + "'.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Could not delete student: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println("Input ended unexpectedly. Delete cancelled.");
        }
    }

    /** Menu option 4 (or 4/5 in the standalone demo): Search by ID (linked-list, linear search). */
    public void handleSearch() {
        System.out.println("\n--- Search Student by ID (Linked List) ---");
        try {
            String id = readLine("Student ID: ");
            Student found = list.findById(id);
            System.out.println(found != null ? "Found: " + describe(found)
                    : "No student found with ID '" + id.trim() + "'.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not search: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println("Input ended unexpectedly. Search cancelled.");
        }
    }

    /** Display all records via the linked list's traversal, in insertion order. */
    public void handleDisplay() {
        System.out.println("\n--- All Student Records (Linked List order) ---");
        if (list.isEmpty()) {
            System.out.println("No student records yet.");
            return;
        }
        System.out.printf("%-12s %-20s %-20s %-8s%n", "Student ID", "Name", "Programme", "Marks");
        System.out.println("--------------------------------------------------------------------");
        for (Student s : list.traverse()) {
            System.out.printf("%-12s %-20s %-20s %-8.2f%n",
                    s.getStudentId(), s.getName(), s.getProgramme(), s.getMarks());
        }
        System.out.println("Total records: " + list.size());
    }

    // ---------------------------------------------------------------
    // Input helpers
    // ---------------------------------------------------------------

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Reads a line and parses it as marks, re-prompting on non-numeric input
     * (but not on out-of-range values — those are surfaced by Student's own
     * validation so the failure message is consistent everywhere).
     */
    private double readMarks(String prompt) {
        while (true) {
            String raw = readLine(prompt);
            try {
                return Double.parseDouble(raw.trim());
            } catch (NumberFormatException e) {
                System.out.println("'" + raw + "' is not a valid number. Please enter a numeric mark.");
            }
        }
    }

    private static String describe(Student s) {
        return String.format("[%s] %s | %s | %.2f",
                s.getStudentId(), s.getName(), s.getProgramme(), s.getMarks());
    }
}
