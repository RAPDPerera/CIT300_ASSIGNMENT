package studentrecords;

/** Separate chaining hash table; custom buckets, no HashMap. Resizes at load factor 0.75. */
public final class StudentHashTable {
    private static final class Entry {
        Student student;
        Entry next;
        Entry(Student student, Entry next) { this.student = student; this.next = next; }
    }
    private Entry[] buckets = new Entry[16];
    private int size;
    public int size() { return size; }
    private int index(String id) { return (id.hashCode() & 0x7fffffff) % buckets.length; }

    public Student get(String id) {
        String key = StudentBST.requireId(id);
        for (Entry entry = buckets[index(key)]; entry != null; entry = entry.next)
            if (entry.student.getStudentId().equals(key)) return entry.student;
        return null;
    }
    public boolean put(Student student) {
        if (student == null) throw new IllegalArgumentException("student must not be null");
        String key = student.getStudentId();
        if (get(key) != null) return false;
        if ((size + 1) * 4 > buckets.length * 3) resize();
        int position = index(key);
        buckets[position] = new Entry(student, buckets[position]);
        size++;
        return true;
    }
    public boolean update(Student student) {
        if (student == null) throw new IllegalArgumentException("student must not be null");
        String key = student.getStudentId();
        for (Entry entry = buckets[index(key)]; entry != null; entry = entry.next)
            if (entry.student.getStudentId().equals(key)) { entry.student = student; return true; }
        return false;
    }
    public Student remove(String id) {
        String key = StudentBST.requireId(id);
        int position = index(key);
        Entry previous = null;
        for (Entry entry = buckets[position]; entry != null; entry = entry.next) {
            if (entry.student.getStudentId().equals(key)) {
                if (previous == null) buckets[position] = entry.next;
                else previous.next = entry.next;
                size--;
                return entry.student;
            }
            previous = entry;
        }
        return null;
    }
    private void resize() {
        Entry[] old = buckets;
        buckets = new Entry[old.length * 2];
        for (Entry head : old) {
            while (head != null) {
                Entry next = head.next;
                int position = index(head.student.getStudentId());
                head.next = buckets[position];
                buckets[position] = head;
                head = next;
            }
        }
    }
}
