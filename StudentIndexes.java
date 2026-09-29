package studentrecords;

/** Integration adapter: the linked list is authoritative; both indexes mirror it. */
public final class StudentIndexes {
    private StudentBST tree = new StudentBST();
    private StudentHashTable hash = new StudentHashTable();
    public StudentBST tree() { return tree; }
    public StudentHashTable hash() { return hash; }

    /** Call only after linkedList.addStudent succeeds. */
    public void added(Student student) {
        if (student == null || hash.get(student.getStudentId()) != null)
            throw new IllegalStateException("duplicate or null index addition");
        tree.insert(student);
        hash.put(student);
    }
    /** Call after a successful list update, with list.findById(id), never the old object. */
    public void updated(Student replacement) {
        if (replacement == null || hash.get(replacement.getStudentId()) == null)
            throw new IllegalStateException("missing index update");
        tree.update(replacement);
        hash.update(replacement);
    }
    /** Call after a successful list deletion. */
    public void deleted(String id) {
        if (hash.get(id) == null) throw new IllegalStateException("missing index deletion");
        tree.remove(id);
        hash.remove(id);
    }
    /** Rebuild after bulk changes or when integrating with an existing populated list. */
    public void rebuild(StudentLinkedList list) {
        if (list == null) throw new IllegalArgumentException("list must not be null");
        StudentBST newTree = new StudentBST();
        StudentHashTable newHash = new StudentHashTable();
        for (Student student : list.traverse()) {
            newTree.insert(student);
            newHash.put(student);
        }
        tree = newTree;
        hash = newHash;
    }
}
