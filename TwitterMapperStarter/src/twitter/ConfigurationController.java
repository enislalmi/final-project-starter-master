package twitter;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class ConfigurationController {

    public static Configuration  getDefaultConfiguration()
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("JeE2S8KXWwVp0yEYrKhsv7lUE")
                .setOAuthConsumerSecret("BhatTGhgfDcJSd4lJRV1Cw6CE1I9MFT8LtzwKkRSNXW042qCmk")
                .setOAuthAccessToken("1282629240396156929-XA4r8GY6eTSsX1UdfBVklpmwNkfLUr")
                .setOAuthAccessTokenSecret("IBFbR3d5eeOqXJchVV2o09fuNHLQisNYo2n3qC4PFEccQ");
        return cb.build();
    }



}
