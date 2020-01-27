package no.stelar7.api.r4j.basic;

import no.stelar7.api.r4j.basic.exceptions.APIUnsupportedActionException;
import no.stelar7.api.r4j.basic.utils.Base32;

import java.security.*;

public class APICredentials
{
    private String lolAPIKey;
    private String TFTAPIKey;
    private String LORAPIKey;
    private String tournamentApiKey;
    
    /**
     * Instantiates new API credentials.
     *
     * @param lol        the lol key
     * @param tft        the tft lol key
     * @param tournament the tournament key
     */
    public APICredentials(final String lol, final String tournament, final String tft, final String lor)
    {
        this.lolAPIKey = lol;
        this.tournamentApiKey = tournament;
        this.TFTAPIKey = tft;
        this.LORAPIKey = lor;
    }
    
    /**
     * Instantiates new API credentials.
     *
     * @param key        the lol key
     * @param tournament the tournament key
     */
    public APICredentials(final String key, final String tournament)
    {
        this(key, tournament, key, key);
    }
    
    /**
     * Instantiates new API credentials.
     *
     * @param api the api key
     */
    public APICredentials(final String api)
    {
        this(api, api, api, api);
    }
    
    /**
     * Gets the api key.
     *
     * @return the key
     */
    public String getLoLAPIKey()
    {
        if (this.lolAPIKey == null)
        {
            throw new APIUnsupportedActionException("API key not set!");
        }
        return this.lolAPIKey;
    }
    
    /**
     * Gets the tft key.
     *
     * @return the key
     */
    public String getTFTAPIKey()
    {
        if (this.TFTAPIKey == null)
        {
            throw new APIUnsupportedActionException("API key not set!");
        }
        return this.TFTAPIKey;
    }
    
    
    /**
     * Gets the lor key.
     *
     * @return the key
     */
    public String getLORAPIKey()
    {
        if (this.LORAPIKey == null)
        {
            throw new APIUnsupportedActionException("API key not set!");
        }
        return this.LORAPIKey;
    }
    
    /**
     * Gets the tournament key.
     *
     * @return the key
     */
    public String getTournamentAPIKey()
    {
        if (this.tournamentApiKey == null)
        {
            throw new APIUnsupportedActionException("TOURNAMENT key not set!");
        }
        return this.tournamentApiKey;
    }
    
    public String getUniqueKeyCombination()
    {
        String unique = lolAPIKey + "separator" + tournamentApiKey + "separator" + TFTAPIKey + "separator" + LORAPIKey;
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(unique.getBytes());
            return Base32.encode(messageDigest.digest());
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return unique;
        }
    }
}