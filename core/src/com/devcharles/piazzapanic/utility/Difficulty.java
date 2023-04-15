package com.devcharles.piazzapanic.utility;

public enum Difficulty {
    SCENARIO("Scenario",30000),
    ENDLESS_EASY("Endless - Easy",120000),
    ENDLESS_NORMAL("Endless - Normal",60000),
    ENDLESS_HARD("Endless - Hard",30000);

    private String displayName;
    private int spawnFrequency;
    Difficulty(String displayName, int spawnFrequency){
        this.displayName=displayName;
        this.spawnFrequency=spawnFrequency;
    }
    public String getDisplayName(){
        return this.displayName;
    }
    public int getSpawnFrequency(){ return this.spawnFrequency;}
}
