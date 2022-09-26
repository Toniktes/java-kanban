package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    ArrayList<Integer> idSubtasks = new ArrayList<>();

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
    }

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    @Override
    public boolean isEpic() {
        return true;
    }

    public void addIdSubtasks(int id) {
        idSubtasks.add(id);
    }

    public ArrayList<Integer> getIdSubtasks() {
        return idSubtasks;
    }

    public void clearIdSubtasks() {
        idSubtasks.clear();
    }

    public void removeSubtask(int id) {
        idSubtasks.remove(Integer.valueOf(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idSubtasks, epic.idSubtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubtasks);
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", idSubtasks=" + idSubtasks.toString() +
                '}';
    }
}
