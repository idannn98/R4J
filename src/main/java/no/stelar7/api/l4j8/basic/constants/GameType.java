package no.stelar7.api.l4j8.basic.constants;

import java.util.*;
import java.util.stream.*;

public enum GameType
{
    /**
     * Custom games
     */
    CUSTOM_GAME,
    /**
     * Tutorial games
     */
    TUTORIAL_GAME,
    /**
     * All other games
     */
    MATCHED_GAME;
    
    /**
     * Returns a GameType from the provided value
     *
     * @return GameType
     */
    public static Optional<GameType> getFromCode(final String gameType)
    {
        return Stream.of(GameType.values()).filter(t -> t.name().equalsIgnoreCase(gameType)).findFirst();
    }
    
    /**
     * The value used to map strings to objects
     *
     * @return String
     */
    public String getCode()
    {
        return this.name();
    }
}
