package studentrecords;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A hand-written singly linked list that stores {@link Student} records.
 *
 * This is Member 1's core data structure. It does NOT use java.util.LinkedList,
 * ArrayList, HashMap, or any other built-in collection as the underlying
 * store — nodes and links are managed manually.
 *
 * Invariants maintained by every method:
 *   - Empty list:      head == null, tail == null, size == 0.
 *   - Non-empty list:  head and tail reference reachable nodes.
 *   - tail.next == null.
 *   - size equals the number of reachable nodes.
 *   - No two nodes share the same (case-sensitive, trimmed) studentId.
 *   - No cycles; no nodes become unreachable except through deletion.
 *
 * Outcome policy (documented, since the assignment brief does not specify one):
 *   - Invalid arguments (null Student, null/blank id):        IllegalArgumentException
 *   - addStudent with a duplicate id:                          returns false
 *   - findById / deleteStudent for a missing id:               returns null
 *   - updateStudent for a missing id or invalid new values:    returns false
 *   - updateStudent success:                                   returns true
 *   - deleteStudent success:                                   returns the removed Student
 *
 * This class only manages student data in memory. It does not implement or
 * depend on the stack (Member 2), queue (Member 2), BST/AVL or hashing
 * (Member 3), or the graph/BFS/DFS (Member 4).
 */
public class StudentLinkedList {

    /** Node is private: callers never see Node objects, only Student data. */
    private static class Node {
        private Student data;
        private Node next;

        Node(Student data) {
            this.data = data;
        }

        void setData(Student data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public StudentLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // ---------------------------------------------------------------
    // A. Add
    // ---------------------------------------------------------------

    /**
     * Appends a new student to the end of the list, preserving insertion order.
     *
     * @param student the student to add; must not be null (Student's own
     *                constructor already rejects invalid field values, so by
     *                the time an object reaches here it is well-formed).
     * @return true if added; false if a student with the same studentId
     *         (case-sensitive, trimmed) already exists — the list is left
     *         unchanged in that case.
     * @throws IllegalArgumentException if student is null.
     */
    public boolean addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("student must not be null");
        }
        if (findNode(student.getStudentId()) != null) {
            return false; // duplicate id — list unchanged
        }

        Node newNode = new Node(student);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        return true;
    }

    // ---------------------------------------------------------------
    // D. Search
    // ---------------------------------------------------------------

    /**
     * Linear search by student ID, following the linked list from head.
     *
     * This is Member 1's own lookup for add/update/delete and for the
     * standalone demo. It is intentionally O(n) and does NOT represent the
     * assignment's hashing requirement — efficient ID search via hashing is
     * Member 3's responsibility.
     *
     * @param studentId the id to search for.
     * @return the matching Student, or null if not found or the list is empty.
     * @throws IllegalArgumentException if studentId is null or blank.
     */
    public Student findById(String studentId) {
        Node node = findNode(studentId);
        return node == null ? null : node.data;
    }

    /** Internal helper: finds the node whose Student has the given id, or null. */
    private Node findNode(String studentId) {
        String normalizedId = requireNonBlankId(studentId);
        Node current = head;
        while (current != null) {
            if (current.data.getStudentId().equals(normalizedId)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    // ---------------------------------------------------------------
    // B. Update
    // ---------------------------------------------------------------

    /**
     * Updates the name, programme, and marks of the student with the given id.
     * The studentId, the record's position in the list, and the list size are
     * all preserved. All replacement values are validated (via the Student
     * constructor) before the existing node's data is replaced, so a failed
     * validation leaves the original record completely unchanged.
     *
     * @return true if the student was found and updated; false if no student
     *         with that id exists.
     * @throws IllegalArgumentException if studentId is null/blank, or if the
     *         replacement name/programme/marks are invalid.
     */
    public boolean updateStudent(String studentId, String name, String programme, double marks) {
        Node node = findNode(studentId);
        if (node == null) {
            return false;
        }
        // Build the replacement first: if any value is invalid this throws
        // before anything in the list is touched, so a failed validation
        // leaves the original record completely unchanged.
        Student replacement = new Student(node.data.getStudentId(), name, programme, marks);
        node.setData(replacement);
        return true;
    }

    // ---------------------------------------------------------------
    // C. Delete
    // ---------------------------------------------------------------

    /**
     * Deletes the student with the given id, handling the empty list, the
     * only node, head, middle, tail, and missing-id cases.
     *
     * @return the removed Student (useful for Member 2's history/undo
     *         feature), or null if no student with that id exists.
     * @throws IllegalArgumentException if studentId is null or blank.
     */
    public Student deleteStudent(String studentId) {
        String normalizedId = requireNonBlankId(studentId);

        if (isEmpty()) {
            return null;
        }

        Node previous = null;
        Node current = head;
        while (current != null && !current.data.getStudentId().equals(normalizedId)) {
            previous = current;
            current = current.next;
        }

        if (current == null) {
            return null; // not found
        }

        if (previous == null) {
            // Deleting the head (covers the "only node" case too).
            head = current.next;
            if (head == null) {
                tail = null; // list is now empty
            }
        } else {
            previous.next = current.next;
            if (current == tail) {
                tail = previous; // deleted the tail
            }
        }

        size--;
        return current.data;
    }

    // ---------------------------------------------------------------
    // Size / empty
    // ---------------------------------------------------------------

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // ---------------------------------------------------------------
    // E. Display / safe traversal
    // ---------------------------------------------------------------

    /**
     * Returns a read-only, forward-only view over the students in insertion
     * order. Node objects are never exposed, and the returned iterator does
     * not support remove().
     */
    public Iterable<Student> traverse() {
        return () -> new Iterator<Student>() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Student next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                Student data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    private static String requireNonBlankId(String studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("studentId must not be null");
        }
        String trimmed = studentId.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("studentId must not be blank");
        }
        return trimmed;
    }
}
