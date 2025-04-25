
     import java.util.*;

class Trail {
    int to;
    int water;
    boolean day;
    boolean night;

    public Trail(int to, int water, boolean day, boolean night) {
        this.to = to;
        this.water = water;
        this.day = day;
        this.night = night;
    }
}

public class WaterToCity {

    static class State implements Comparable<State> {
        int waterUsed;
        int location;
        int time; // 0 for day, 1 for night

        public State(int waterUsed, int location, int time) {
            this.waterUsed = waterUsed;
            this.location = location;
            this.time = time;
        }

        public int compareTo(State other) {
            return Integer.compare(this.waterUsed, other.waterUsed);
        }
    }

    public static int minWaterToCity(int P, int C, Map<Integer, List<Trail>> trails) {
        PriorityQueue<State> heap = new PriorityQueue<>();
        heap.add(new State(0, P, 0)); // Start at pond during the day
        Map<String, Integer> visited = new HashMap<>();

        while (!heap.isEmpty()) {
            State current = heap.poll();
            String key = current.location + "," + current.time;

            if (visited.containsKey(key) && visited.get(key) <= current.waterUsed) {
                continue;
            }
            visited.put(key, current.waterUsed);

            if (current.location == C) {
                return current.waterUsed;
            }

            // Wait to switch time of day
            int nextTime = 1 - current.time;
            String waitKey = current.location + "," + nextTime;
            if (!visited.containsKey(waitKey) || current.waterUsed + 3 < visited.get(waitKey)) {
                heap.add(new State(current.waterUsed + 3, current.location, nextTime));
            }

            // Explore available trails
            List<Trail> neighbors = trails.getOrDefault(current.location, new ArrayList<>());
            for (Trail trail : neighbors) {
                if ((current.time == 0 && trail.day) || (current.time == 1 && trail.night)) {
                    int nextLocation = trail.to;
                    int nextWater = current.waterUsed + trail.water;
                    String trailKey = nextLocation + "," + nextTime;
                    if (!visited.containsKey(trailKey) || nextWater < visited.get(trailKey)) {
                        heap.add(new State(nextWater, nextLocation, nextTime));
                    }
                }
            }
        }

        return -1; // City not reachable
    }
}

