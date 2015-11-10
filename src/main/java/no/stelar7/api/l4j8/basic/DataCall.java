package no.stelar7.api.l4j8.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import com.google.common.util.concurrent.RateLimiter;

import javafx.util.Pair;

public class DataCall
{

    public static class DataCallBuilder
    {
        private final DataCall dc = new DataCall();

        private final BiFunction<String, String, String> merge = ((o, n) -> o + "," + n);

        public DataCallBuilder asVerbose(final boolean verbose)
        {
            this.dc.verbose = verbose;
            return this;
        }

        public Pair<ResponseType, Object> build()
        {

            try
            {

                if (this.dc.useCache)
                {
                    if (DataCall.cache.containsKey(this.getURL()))
                    {
                        return new Pair<>(ResponseType.OK, DataCall.cache.get(this.getURL()));
                    }
                }

                if (this.dc.server.isLimited() || !this.dc.endpoint.getValue().startsWith(DataCall.HTTP))
                {
                    DataCall.limiter.get(this.dc.server).acquire();
                }

                final String url = this.getURL();
                final HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

                con.setUseCaches(false);
                con.setDefaultUseCaches(false);
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
                con.setRequestProperty("Cache-Control", "no-store,max-age=0,no-cache");
                con.setRequestProperty("Expires", "0");
                con.setRequestProperty("Pragma", "no-cache");
                con.setRequestProperty("Connection", "keep-alive");
                con.connect();

                if (this.dc.verbose)
                {
                    System.out.println(url);
                    con.getHeaderFields().forEach((key, value) -> {
                        System.out.println(key + ":\t" + value);
                    });
                }

                final int response = con.getResponseCode();

                if (response == 200)
                {
                    final StringBuilder data = new StringBuilder();

                    try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)))
                    {
                        br.lines().forEach(s -> data.append(s));
                    }

                    con.disconnect();

                    final Class<? extends APIObject> returnType = this.dc.endpoint.getType();
                    final Method creator = returnType.getMethod("createFromString", String.class);

                    final Object dtoobj = creator.invoke(returnType.newInstance(), data.toString());

                    if (this.dc.useCache)
                    {
                        DataCall.cache.put(this.getURL(), dtoobj);
                    }

                    return new Pair<>(ResponseType.OK, dtoobj);
                }

                if (response == 429)
                {
                    if (this.dc.retry)
                    {
                        final String limitType = con.getHeaderField("X-Rate-Limit-Type");

                        if ((limitType != null) && (limitType.equals(DataCall.LIMIT_USER) || limitType.equals(DataCall.LIMIT_SERVICE)))
                        {
                            final String timeoutString = con.getHeaderField("Retry-After");

                            final long timeout = timeoutString == null ? this.dc.retryTime : Integer.parseInt(timeoutString);

                            TimeUnit.SECONDS.sleep(timeout);

                            con.disconnect();

                            return this.build();
                        }

                        TimeUnit.SECONDS.sleep(this.dc.retryTime);

                        con.disconnect();

                        return this.build();
                    }
                }
                return new Pair<>(ResponseType.ERROR, response);

            } catch (IOException | IllegalAccessException | InterruptedException | InstantiationException | InvocationTargetException | NoSuchMethodException e)
            {
                e.printStackTrace();
                return new Pair<>(ResponseType.ERROR, e.getMessage());
            }
        }

        public DataCallBuilder clearURLData()
        {
            this.dc.urlData.clear();
            return this;
        }

        public DataCallBuilder clearURLParameter()
        {
            this.dc.urlParams.clear();
            return this;
        }

        public String getURL()
        {
            if (urlBuilder.length() != 0)
            {
                return urlBuilder.toString();
            }

            if (!this.dc.endpoint.getValue().startsWith(DataCall.HTTP))
            {
                urlBuilder.append(DataCall.HTTPS);
            } else
            {
                this.dc.server = Server.GLOBAL;
            }

            urlBuilder.append(this.dc.server.getURL()).append(this.dc.endpoint.getValue());

            this.withURLData("{region}", this.dc.server.asURLFormat());
            this.withURLData("{version}", this.dc.endpoint.getVersion());

            this.dc.urlData.forEach((k, v) -> {
                String temp = urlBuilder.toString();
                temp = temp.toString().replace(k, v);
                urlBuilder.setLength(0);
                urlBuilder.append(temp);
            });

            urlBuilder.append("?api_key=").append(this.dc.key);
            this.dc.urlParams.forEach((k, v) -> urlBuilder.append("&").append(k).append("=").append(v));

            return urlBuilder.toString();
        }

        public DataCallBuilder withAPIKey(final String key)
        {
            this.dc.key = key;
            return this;
        }

        public DataCallBuilder withEndpoint(final URLEndpoint endpoint)
        {
            this.dc.endpoint = endpoint;
            return this;
        }

        public DataCallBuilder withRetryOn429(final boolean retry)
        {
            this.dc.retry = retry;
            return this;
        }

        public DataCallBuilder withRetryTime(final int retryTime)
        {
            this.dc.retryTime = retryTime;
            return this;
        }

        public DataCallBuilder withServer(final Server server)
        {
            this.dc.server = server;
            return this;
        }

        public DataCallBuilder withURLData(final String key, final String value)
        {
            this.dc.urlData.merge(key, value, this.merge);
            return this;
        }

        public DataCallBuilder withURLParameter(final String key, final String value)
        {
            this.dc.urlParams.merge(key, value, this.merge);
            return this;
        }

        public DataCallBuilder useCache(final boolean flag)
        {
            this.dc.useCache = flag;
            return this;
        }

        final StringBuilder urlBuilder = new StringBuilder();

    }

    public enum ResponseType
    {
        OK,
        ERROR;
    }

    private static final Map<Server, RateLimiter> limiter = new HashMap<Server, RateLimiter>()
    {
        {
            Arrays.stream(Server.values()).filter(s -> s.isLimited()).forEach(s -> this.put(s, RateLimiter.create(500.0 / (60.0 * 10.0))));
        }
    };

    private static final String HTTP = "http://";

    private static final String HTTPS         = "https://";
    private static final String LIMIT_USER    = "service";
    private static final String LIMIT_SERVICE = "user";

    public static DataCallBuilder builder()
    {
        return new DataCallBuilder();
    }

    private String key;

    private final Map<String, String> urlData   = new HashMap<>();
    private final Map<String, String> urlParams = new HashMap<>();

    private boolean retry    = true;
    private boolean verbose  = false;
    public boolean  useCache = true;

    private int retryTime = 5;

    private Server server;

    private URLEndpoint endpoint;

    /**
     * URL , Object
     */
    private static final Map<String, Object> cache = new HashMap<String, Object>();

}
