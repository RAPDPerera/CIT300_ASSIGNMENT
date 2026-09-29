package studentrecords;

public class Stack {

    private String[] actions;
    private int top;

    public Stack(int size) {
        actions = new String[size];
        top = -1;
    }

    // Add an action to the stack
    public void push(String action) {
        if (top == actions.length - 1) {
            System.out.println("Stack is full.");
            return;
        }

        top++;
        actions[top] = action;
    }

    // Remove the most recent action
    public String pop() {
        if (top == -1) {
            System.out.println("Stack is empty.");
            return null;
        }

        String action = actions[top];
        actions[top] = null;
        top--;

        return action;
    }

    // View the most recent action
    public String peek() {
        if (top == -1) {
            System.out.println("Stack is empty.");
            return null;
        }

        return actions[top];
    }

    // Display all recent actions
    public void display() {
        if (top == -1) {
            System.out.println("No recent actions.");
            return;
        }

        System.out.println("Recent Actions:");

        for (int i = top; i >= 0; i--) {
            System.out.println("- " + actions[i]);
        }
    }
}