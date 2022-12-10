package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> idSubtasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status, 0L, LocalDateTime.now());
        endTime = getEndTime();
    }

    public Epic(int id, String name, String description, TaskStatus status, long duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
        endTime = super.getEndTime();
    }

    public Epic(String name, String description, TaskStatus status, long duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        endTime = super.getEndTime();
    }

    @Override
    public boolean isEpic() {
        return true;
    }

    public void addIdSubtasks(int id) {
        idSubtasks.add(id);
    }

    public List<Integer> getIdSubtasks() {
        return idSubtasks;
    }

    public void clearIdSubtasks() {
        idSubtasks.clear();
    }

    public void removeSubtask(int id) {
        idSubtasks.remove(Integer.valueOf(id));
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
        return "Epic{" +
                "idSubtasks=" + idSubtasks +
                ", endTime=" + endTime +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
