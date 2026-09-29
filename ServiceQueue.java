package studentrecords;
public class ServiceQueue implements Queue {

    private String[] requests;
    private int front;
    private int rear;

    public ServiceQueue(int size) {
        requests = new String[size];
        front = 0;
        rear = -1;
    }

    @Override
    public void enqueue(String request) {
        if (rear == requests.length - 1) {
            System.out.println("Queue is full.");
            return;
        }

        rear++;
        requests[rear] = request;
    }

    @Override
    public String dequeue() {
        if (front > rear) {
            System.out.println("Queue is empty.");
            return null;
        }

        String request = requests[front];
        requests[front] = null;
        front++;

        return request;
    }

    @Override
    public String peek() {
        if (front > rear) {
            System.out.println("Queue is empty.");
            return null;
        }

        return requests[front];
    }

    @Override
    public void display() {
        if (front > rear) {
            System.out.println("No service requests.");
            return;
        }

        System.out.println("Service Requests:");

        for (int i = front; i <= rear; i++) {
            System.out.println("- " + requests[i]);
        }
    }
}