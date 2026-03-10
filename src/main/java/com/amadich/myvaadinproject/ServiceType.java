package com.amadich.myvaadinproject;

public enum ServiceType {

    POSTGRES("PostgreSQL", "postgres.svg", "postgres:16", "Relational database"),
    MYSQL("MySQL", "mysql.svg", "mysql:8", "MySQL database"),
    REDIS("Redis", "redis.svg", "redis:7", "In-memory cache"),
    RABBITMQ("RabbitMQ", "rabbitmq.svg", "rabbitmq:3", "Message broker");

    private final String name;
    private final String image;
    private final String dockerImage;
    private final String description;

    ServiceType(String name, String image, String dockerImage, String description) {
        this.name = name;
        this.image = image;
        this.dockerImage = dockerImage;
        this.description = description;
    }

    public String getName() { return name; }
    public String getImage() { return image; }
    public String getDockerImage() { return dockerImage; }
    public String getDescription() { return description; }
}