package studentrecords;

import java.util.ArrayList;
import java.util.List;

/** Unbalanced binary search tree keyed by trimmed, case-sensitive student ID. */
public final class StudentBST {
    private static final class Node {
        Student student;
        Node left, right;
        Node(Student student) { this.student = student; }
    }
    private Node root;
    private int size;

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    /** Insert a new ID; return false for an existing ID. */
    public boolean insert(Student student) {
        if (student == null) throw new IllegalArgumentException("student must not be null");
        if (root == null) { root = new Node(student); size++; return true; }
        Node current = root;
        while (true) {
            int comparison = student.getStudentId().compareTo(current.student.getStudentId());
            if (comparison == 0) return false;
            if (comparison < 0) {
                if (current.left == null) { current.left = new Node(student); size++; return true; }
                current = current.left;
            } else {
                if (current.right == null) { current.right = new Node(student); size++; return true; }
                current = current.right;
            }
        }
    }

    public Student search(String id) {
        String key = requireId(id);
        Node current = root;
        while (current != null) {
            int comparison = key.compareTo(current.student.getStudentId());
            if (comparison == 0) return current.student;
            current = comparison < 0 ? current.left : current.right;
        }
        return null;
    }

    /** Replace record with the same ID, preserving tree shape. */
    public boolean update(Student student) {
        if (student == null) throw new IllegalArgumentException("student must not be null");
        String key = student.getStudentId();
        Node current = root;
        while (current != null) {
            int comparison = key.compareTo(current.student.getStudentId());
            if (comparison == 0) { current.student = student; return true; }
            current = comparison < 0 ? current.left : current.right;
        }
        return false;
    }

    /** Remove ID and return old record, or null if absent. */
    public Student remove(String id) {
        String key = requireId(id);
        Student old = search(key);
        if (old == null) return null;
        root = removeNode(root, key);
        size--;
        return old;
    }

    private Node removeNode(Node node, String key) {
        if (node == null) return null;
        int comparison = key.compareTo(node.student.getStudentId());
        if (comparison < 0) node.left = removeNode(node.left, key);
        else if (comparison > 0) node.right = removeNode(node.right, key);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node successor = node.right;
            while (successor.left != null) successor = successor.left;
            node.student = successor.student;
            node.right = removeNode(node.right, successor.student.getStudentId());
        }
        return node;
    }

    /** Students in ascending lexicographic ID order. */
    public List<Student> inOrder() {
        List<Student> result = new ArrayList<>();
        visit(root, result);
        return result;
    }
    private void visit(Node node, List<Student> result) {
        if (node == null) return;
        visit(node.left, result);
        result.add(node.student);
        visit(node.right, result);
    }
    static String requireId(String id) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("student ID must not be blank");
        return id.trim();
    }
}
