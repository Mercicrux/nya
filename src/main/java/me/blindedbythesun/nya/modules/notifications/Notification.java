package me.blindedbythesun.nya.modules.notifications;

public class Notification {

    private String name,description;
    private long creationTime,durationMS;
    private NotificationType type;

    public Notification(String name, String description, long durationMS, NotificationType type) {
        this.name = name;
        this.description = description;
        this.durationMS = durationMS;
        this.type = type;
        creationTime = System.currentTimeMillis();
    }

    public Notification(String description, long durationMS, NotificationType type) {
        this.name = "";
        this.description = description;
        this.durationMS = durationMS;
        this.type = type;
        creationTime = System.currentTimeMillis();
    }

    public void resetCreationTime() {
        creationTime = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDurationMS() {
        return durationMS;
    }

    public void setDurationMS(long durationMS) {
        this.durationMS = durationMS;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public NotificationType getType() {
        return type;
    }

}
