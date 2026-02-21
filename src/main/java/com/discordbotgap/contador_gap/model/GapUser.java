package com.discordbotgap.contador_gap.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GapUser {
    @Id
    private String discordId;
    private String username;
    private long totalGaps;

    public GapUser() {}

    public GapUser(String discordId, String username, long totalGaps) {
        this.discordId = discordId;
        this.username = username;
        this.totalGaps = totalGaps;
    }

    public String getDiscordId() { return discordId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public long getTotalGaps() { return totalGaps; }
    public void setTotalGaps(long totalGaps) { this.totalGaps = totalGaps; }
}
