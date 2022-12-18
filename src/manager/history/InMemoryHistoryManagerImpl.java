package manager.history;

import manager.history.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManagerImpl implements HistoryManager {

    private static class Node {
        private Task task;
        private Node prev;
        private Node next;

        private Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task +
                    ", prev=" + prev +
                    ", next=" + next +
                    '}';
        }
    }


    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node node = first;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }

    private void linkLast(Task task) {
        final Node oldLast = last;
        final Node node = new Node(task, last, null);
        last = node;
        if (oldLast == null) {
            first = node;
        } else {
            oldLast.next = node;
        }
        nodeMap.put(task.getId(), node);
    }

    private void removeNode(int id) {
        final Node node = nodeMap.remove(id);
        if (node != null) {
            if (node.prev != null && node.next != null) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            } else if (node.next == null && node.prev != null) {
                last = node.prev;
                node.prev.next = null;
            } else if (node.next != null) {
                first = node.next;
                node.next.prev = null;
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        removeNode(task.getId());
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(id);
        if (nodeMap.isEmpty()) {
            first = null;
            last = null;
        }
    }

    @Override
    public void clearHistory() {
        nodeMap.clear();
        first = null;
        last = null;
    }
}
