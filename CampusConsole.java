package campus;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/** Member 4 menu. Reuse the group's Scanner; this class never closes it. */
public final class CampusConsole {
    private final CampusGraph graph;
    private final Scanner input;
    public CampusConsole(CampusGraph graph, Scanner input) {
        if (graph == null || input == null) throw new IllegalArgumentException("Graph and input are required");
        this.graph = graph;
        this.input = input;
    }
    private String ask(String prompt) {
        System.out.print(prompt);
        if (!input.hasNextLine()) throw new IllegalStateException("Input ended");
        return input.nextLine();
    }
    public void run() {
        while (true) {
            System.out.println("\n1. Add location\n2. Remove location\n3. Add road\n4. Remove road\n5. Display campus network\n6. BFS traversal\n7. Exit");
            String choice;
            try { choice = ask("Choose: ").trim(); }
            catch (IllegalStateException e) { return; }
            try {
                switch (choice) {
                    case "1": System.out.println(graph.addLocation(ask("Location: ")) ? "Location added." : "Location already exists."); break;
                    case "2": System.out.println(graph.removeLocation(ask("Location: ")) ? "Location removed." : "Location not found."); break;
                    case "3": System.out.println(graph.addRoad(ask("From: "), ask("To: ")) ? "Road added." : "Road already exists."); break;
                    case "4": System.out.println(graph.removeRoad(ask("From: "), ask("To: ")) ? "Road removed." : "Road not found."); break;
                    case "5": display(); break;
                    case "6": System.out.println("BFS: " + String.join(" -> ", graph.bfs(ask("Start location: ")))); break;
                    case "7": return;
                    default: System.out.println("Invalid option. Enter 1 to 7.");
                }
            } catch (IllegalArgumentException e) { System.out.println("Invalid operation: " + e.getMessage()); }
            catch (IllegalStateException e) { return; }
        }
    }
    private void display() {
        if (graph.locationCount() == 0) { System.out.println("Campus network is empty."); return; }
        for (Map.Entry<String, List<String>> entry : graph.network().entrySet())
            System.out.println(entry.getKey() + " -> " + (entry.getValue().isEmpty() ? "(no roads)" : String.join(", ", entry.getValue())));
        System.out.println("Locations: " + graph.locationCount() + ", roads: " + graph.roadCount());
    }
}
