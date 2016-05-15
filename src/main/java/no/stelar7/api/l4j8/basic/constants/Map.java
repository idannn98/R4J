package no.stelar7.api.l4j8.basic.constants;

import java.util.*;
import java.util.stream.*;

public enum Map
{

    /**
     * Summoner's Rift Original Summer Variant
     */
    SUMMONERS_RIFT_SUMMER_OLD(1),
    /**
     * Summoner's Rift Original Autumn Variant
     */
    SUMMONERS_RIFT_AUTUMN_OLD(2),
    /**
     * The Proving Grounds Tutorial Map
     */
    PROVING_GROUNDS(3),
    /**
     * Twisted Treeline Original Version
     */
    TWISTED_TREELINE_OLD(4),
    /**
     * The Crystal Scar Dominion Map
     */
    DOMINION(8),
    /**
     * Twisted Treeline Current Version
     */
    TWISTED_TREELINE(10),
    /**
     * Summoner's Rift Current Version
     */
    SUMMONERS_RIFT(11),
    /**
     * Howling Abyss ARAM Map
     */
    HOWLING_ABYSS(12),
    /**
     * Butcher's Bridge ARAM Map
     */
    BUTCHERS_BRIDGE(14);

    public static Optional<Map> getFromCode(final Integer mapId)
    {
        return Stream.of(Map.values()).filter(t -> t.code.equals(mapId)).findFirst();
    }

    Integer code;

    Map(final Integer code)
    {
        this.code = code;
    }

    public Integer getCode()
    {
        return this.code;
    }

}
