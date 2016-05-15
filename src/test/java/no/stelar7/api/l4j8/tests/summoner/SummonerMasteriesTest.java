package no.stelar7.api.l4j8.tests.summoner;

import java.util.*;
import java.util.function.*;

import org.junit.*;

import no.stelar7.api.l4j8.basic.constants.api.*;
import no.stelar7.api.l4j8.pojo.summoner.masteries.*;
import no.stelar7.api.l4j8.tests.*;

public class SummonerMasteriesTest extends TestBase
{
    private final BiConsumer<String, MasteryPages> doAssertions = (final String key, final MasteryPages value) -> {

        value.getPages().forEach((final MasteryPage page) -> {
            Assert.assertNotNull("Mastery Page does not have an id", page.getId());
            // Assert.assertNotNull("Mastery Page does not have a name",
            // page.getName()); Names can be empty, so ignore this...
            Assert.assertNotNull("Mastery Page does not contain any masteries", page.getMasteries());
            Assert.assertNotNull("Unable to determine current Mastery page", page.isCurrent());

            page.getMasteries().forEach((final Mastery mastery) -> {
                Assert.assertNotNull("Mastery does not have an id", mastery.getId());
                Assert.assertNotNull("Mastery does not have a rank", mastery.getRank());

                Assert.assertNotEquals("Mastery does not have a valid id", mastery.getId(), (Integer) 0);
                Assert.assertNotEquals("Mastery does not have a valid rank", mastery.getRank(), (Integer) 0);
            });
        });

        Assert.assertTrue("There is not exactly ONE \"current\" page", value.getPages().stream().filter((final MasteryPage page) -> page.isCurrent()).count() == 1);
    };

    @Test
    public void doTest()
    {
        // Generate list of summoner IDs
        final List<String> keys = Arrays.asList("19613950", "22291359", "33540589");

        // Add them as a parameter to the URL
        keys.forEach((final String k) -> TestBase.builder.withURLData("{summonerId}", k));

        // Get the response
        final Map<String, MasteryPages> data = (Map<String, MasteryPages>) TestBase.builder.build();

        // Make sure all the data is returned as expected
        data.forEach(this.doAssertions);
    }

    @Before
    public void initBeforeTest()
    {
        TestBase.builder.withServer(Server.EUW);
        TestBase.builder.withEndpoint(URLEndpoint.SUMMONER_MASTERIES_BY_ID);
    }
}
