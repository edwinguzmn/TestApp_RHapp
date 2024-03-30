package com.apck.proyectfx.model;

public class ConfigModel {
    private String notifications;
    private String configId;

    public ConfigModel(String notifications, String configId) {
        this.notifications = notifications;
        this.configId = configId;
    }

    public ConfigModel(){

    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }
}
