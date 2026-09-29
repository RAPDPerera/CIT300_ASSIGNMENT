package campus;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Undirected campus graph, represented by an adjacency list. */
public final class CampusGraph {
    private final Map<String, Set<String>> adjacency = new LinkedHashMap<>();
    private int roads;

    private static String location(String value) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException("Location must not be blank");
        return value.trim();
    }
    public int locationCount() { return adjacency.size(); }
    public int roadCount() { return roads; }
    public boolean hasLocation(String name) { return adjacency.containsKey(location(name)); }
    public boolean addLocation(String name) {
        String key = location(name);
        if (adjacency.containsKey(key)) return false;
        adjacency.put(key, new LinkedHashSet<>());
        return true;
    }
    public boolean removeLocation(String name) {
        String key = location(name);
        Set<String> neighbours = adjacency.get(key);
        if (neighbours == null) return false;
        for (String neighbour : neighbours) {
            adjacency.get(neighbour).remove(key);
            roads--;
        }
        adjacency.remove(key);
        return true;
    }
    /** Add an undirected road; both endpoints must exist. */
    public boolean addRoad(String from, String to) {
        String a = location(from), b = location(to);
        requireEndpoints(a, b);
        if (a.equals(b)) throw new IllegalArgumentException("A location cannot connect to itself");
        if (!adjacency.get(a).add(b)) return false;
        adjacency.get(b).add(a);
        roads++;
        return true;
    }
    public boolean removeRoad(String from, String to) {
        String a = location(from), b = location(to);
        requireEndpoints(a, b);
        if (!adjacency.get(a).remove(b)) return false;
        adjacency.get(b).remove(a);
        roads--;
        return true;
    }
    public List<String> neighbours(String name) {
        String key = location(name);
        if (!adjacency.containsKey(key)) throw new IllegalArgumentException("Unknown location: " + key);
        return Collections.unmodifiableList(new ArrayList<>(adjacency.get(key)));
    }
    public Map<String, List<String>> network() {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (String key : adjacency.keySet()) result.put(key, neighbours(key));
        return Collections.unmodifiableMap(result);
    }
    /** Breadth-first traversal of the component reachable from start. */
    public List<String> bfs(String start) {
        String key = location(start);
        if (!adjacency.containsKey(key)) throw new IllegalArgumentException("Unknown location: " + key);
        List<String> order = new ArrayList<>();
        Set<String> visited = new LinkedHashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        visited.add(key);
        queue.add(key);
        while (!queue.isEmpty()) {
            String current = queue.remove();
            order.add(current);
            for (String neighbour : adjacency.get(current)) {
                if (visited.add(neighbour)) queue.add(neighbour);
            }
        }
        return order;
    }
    private void requireEndpoints(String a, String b) {
        if (!adjacency.containsKey(a) || !adjacency.containsKey(b))
            throw new IllegalArgumentException("Both campus locations must exist");
    }
}
