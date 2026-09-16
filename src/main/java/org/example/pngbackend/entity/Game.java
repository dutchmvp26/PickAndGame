package org.example.pngbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String platform;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private int minimumPlayers;

    @Column(nullable = false)
    private int maximumPlayers;

    protected Game() {
    }

    public Game(
            String name,
            String platform,
            String genre,
            int minimumPlayers,
            int maximumPlayers
    ) {
        this.name = name;
        this.platform = platform;
        this.genre = genre;
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public void setMinimumPlayers(int minimumPlayers) {
        this.minimumPlayers = minimumPlayers;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }


    public void setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }
}