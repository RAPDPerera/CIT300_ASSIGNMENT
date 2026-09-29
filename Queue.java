package studentrecords;

public interface Queue {
    
    void enqueue(String request);

    String dequeue();

    String peek();

    void display();
}