package com.amadich.myvaadinproject.backend.model;

public class DeployRequest {

    private String projectName;
    private String service;
    private String username;
    private String password;
    private String database;

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }
}
