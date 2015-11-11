package no.stelar7.api.l4j8.pojo.summoner.masteries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import no.stelar7.api.l4j8.basic.APIObject;

public class MasteryPage implements APIObject
{
    public static Map<Object, List<Object>> createFromString(final String json) throws Exception
    {

        final JsonNode node = APIObject.getDefaultMapper().readTree(json);
        final Map<Object, List<Object>> data = new HashMap<>();
        node.fields().forEachRemaining(m -> {
            final List<Object> pages = new ArrayList<>();

            m.getValue().get("pages").iterator().forEachRemaining(n -> {
                try
                {
                    final Object page = APIObject.getDefaultMapper().readValue(n.toString(), MasteryPage.class);
                    pages.add(page);
                } catch (final Exception e)
                {
                    e.printStackTrace();
                }
            });

            data.put(m.getKey(), pages);
        });
        return data;
    }

    private Boolean       current;
    private Long          id;
    private List<Mastery> masteries;

    private String name;

    /**
     * The mastery page ID
     *
     * @return Long
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * Collection of masteries associated with the mastery page, if there are no masteries, it returns an empty list
     *
     * @return {@code List<Mastery>}
     */
    public List<Mastery> getMasteries()
    {
        return (this.masteries == null) ? Collections.EMPTY_LIST : Collections.unmodifiableList(this.masteries);
    }

    /**
     * The mastery page name
     *
     * @return String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Indicates if the mastery page is the current mastery page
     *
     * @return Boolean
     */
    public Boolean isCurrent()
    {
        return this.current;
    }

    @Override
    public String toString()
    {
        return "MasteryPage [current=" + this.current + ", id=" + this.id + ", masteries=" + this.masteries + ", name=" + this.name + "]";
    }

}