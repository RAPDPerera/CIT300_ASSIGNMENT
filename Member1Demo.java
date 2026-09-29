package studentrecords;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Standalone demo for Member 1's contribution only.
 *
 * This is NOT the group's final application — it exists so Member 1's
 * linked-list module can be demonstrated and marked independently before
 * the team integrates all data structures together. It intentionally has
 * no menu options for stacks, queues, trees, hashing, or graphs.
 *
 * Menu:
 *   1. Add Student Record
 *   2. Update Student Record
 *   3. Delete Student Record
 *   4. Search Student by ID - Linked List
 *   5. Display All Student Records
 *   6. Exit
 */
public class Member1Demo {

    public static void main(String[] args) {
        StudentLinkedList list = new StudentLinkedList();
        try (Scanner scanner = new Scanner(System.in)) {
            StudentConsole console = new StudentConsole(list, scanner);
            runMenu(scanner, console);
        }
    }

    private static void runMenu(Scanner scanner, StudentConsole console) {
        System.out.println("=================================================");
        System.out.println(" Member 1 Demo - Student Record (Linked List)");
        System.out.println("=================================================");

        boolean running = true;
        while (running) {
            printMenu();
            try {
                String choiceLine = scanner.nextLine();
                int choice;
                try {
                    choice = Integer.parseInt(choiceLine.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number between 1 and 6.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        console.handleAdd();
                        break;
                    case 2:
                        console.handleUpdate();
                        break;
                    case 3:
                        console.handleDelete();
                        break;
                    case 4:
                        console.handleSearch();
                        break;
                    case 5:
                        console.handleDisplay();
                        break;
                    case 6:
                        System.out.println("Exiting Member 1 demo. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            } catch (NoSuchElementException e) {
                // End of input (e.g. piped input or Ctrl+D) - exit cleanly.
                System.out.println("\nEnd of input detected. Exiting.");
                running = false;
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n1. Add Student Record");
        System.out.println("2. Update Student Record");
        System.out.println("3. Delete Student Record");
        System.out.println("4. Search Student by ID - Linked List");
        System.out.println("5. Display All Student Records");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }
}
