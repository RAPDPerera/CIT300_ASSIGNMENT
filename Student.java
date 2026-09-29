package studentrecords;

/**
 * Immutable student record.
 *
 * Fields:
 *   studentId  - unique identifier (String, so alphanumeric IDs and leading
 *                zeros are preserved). Trimmed, non-empty, treated as
 *                case-sensitive.
 *   name       - student's name. Trimmed, non-empty. Spaces and ordinary
 *                punctuation are allowed (no letters-only restriction).
 *   programme  - programme of study. Trimmed, non-empty. Same rules as name.
 *   marks      - finite numeric mark in the inclusive range [0, 100].
 *
 * Design note: Student is immutable on purpose. An "update" does not mutate
 * an existing Student; StudentLinkedList replaces the old Student with a new
 * one that has the same studentId. This means any external index (e.g. a
 * hash map or tree built by another team member) that stores a reference to
 * the old Student object must be refreshed after an update, otherwise it
 * will keep pointing at stale data. See docs/MEMBER1.md, section
 * "Integration guidance".
 */
public final class Student {

    private final String studentId;
    private final String name;
    private final String programme;
    private final double marks;

    /**
     * Creates a validated, immutable Student.
     *
     * @throws IllegalArgumentException if studentId, name, or programme is
     *         null/blank, or if marks is not a finite number in [0, 100].
     */
    public Student(String studentId, String name, String programme, double marks) {
        this.studentId = requireNonBlank(studentId, "studentId");
        this.name = requireNonBlank(name, "name");
        this.programme = requireNonBlank(programme, "programme");
        this.marks = requireValidMarks(marks);
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return trimmed;
    }

    private static double requireValidMarks(double marks) {
        if (Double.isNaN(marks) || Double.isInfinite(marks)) {
            throw new IllegalArgumentException("marks must be a finite number");
        }
        if (marks < 0.0 || marks > 100.0) {
            throw new IllegalArgumentException("marks must be between 0 and 100 inclusive");
        }
        return marks;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getProgramme() {
        return programme;
    }

    public double getMarks() {
        return marks;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", programme='" + programme + '\'' +
                ", marks=" + marks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student other = (Student) o;
        return Double.compare(other.marks, marks) == 0 &&
                studentId.equals(other.studentId) &&
                name.equals(other.name) &&
                programme.equals(other.programme);
    }

    @Override
    public int hashCode() {
        int result = studentId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + programme.hashCode();
        long bits = Double.doubleToLongBits(marks);
        result = 31 * result + (int) (bits ^ (bits >>> 32));
        return result;
    }
}
