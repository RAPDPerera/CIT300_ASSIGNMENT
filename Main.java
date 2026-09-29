package studentrecords;
public class Main {
    public static void main(String[] args) {

        // Stack
        Stack stack = new Stack(5);

        stack.push("Added Student S001");
        stack.push("Updated Student S001");
        stack.push("Deleted Student S001");

        stack.display();

        System.out.println("\nLatest Action: " + stack.peek());


        // Queue
        ServiceQueue queue = new ServiceQueue(5);

        queue.enqueue("Service Request S001");
        queue.enqueue("Service Request S002");
        queue.enqueue("Service Request S003");

        queue.display();

        System.out.println("\nNext Request: " + queue.peek());
        System.out.println("Processed Request: " + queue.dequeue());
    }
}