package momotoff.myjira.appsettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppSettings
{
    private static final Logger logger = LoggerFactory.getLogger(AppSettings.class);

    private String getVariable(String name, String defaultValue)
    {
        final String PREFIX = "MYJIRA";

        String varName = String.format("%s_%s", PREFIX, name);
        String value = System.getenv(varName);

        if (value == null)
        {
            logger.warn("{} not set, falling back to default: {}", varName, defaultValue);
            return defaultValue;
        }

        logger.info("{} set to: {}", varName, value);
        return value;
    }

    public String getDbServerAddress()
    {
        return getVariable("DB_SERVER_ADDRESS", "localhost");
    }

    public String getDbServerPort()
    {
        return getVariable("DB_SERVER_PORT", "5432");
    }

    public String getDbServerUser()
    {
        return getVariable("DB_SERVER_USER", "postgres");
    }

    public String getDbServerPassword()
    {
        return getVariable("DB_SERVER_PASSWORD", "postgres");
    }
}
