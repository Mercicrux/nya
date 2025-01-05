package me.sun.unnamed.modules.notifications;

public enum NotificationType {
    INFO(0xFFFFFF),
    WARNING(0xFF7777),
    ERROR(0xFF2222);

    int color;
    NotificationType(int color) {
        this.color = color;
    }
}
