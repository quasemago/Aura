package dev.quasemago.aura.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aura")
public class AuraProperties {
    private final Bot bot = new Bot();
    private final Cache cache = new Cache();
    private final Jikan jikan = new Jikan();

    public Bot getBot() {
        return bot;
    }

    public Cache getCache() {
        return cache;
    }

    public Jikan getJikan() {
        return jikan;
    }

    public static class Bot {
        private String id;
        private String token;
        private String ownerId;
        private String presenceMessage = "Discord";
        private int presenceType = 0;
        private String defaultLanguage = "en";
        private String version = "1.0.0";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getPresenceMessage() {
            return presenceMessage;
        }

        public void setPresenceMessage(String presenceMessage) {
            if (presenceMessage != null && !presenceMessage.isBlank()) {
                this.presenceMessage = presenceMessage;
            }
        }

        public int getPresenceType() {
            return presenceType;
        }

        public void setPresenceType(int presenceType) {
            this.presenceType = presenceType;
        }

        public String getDefaultLanguage() {
            return defaultLanguage;
        }

        public void setDefaultLanguage(String defaultLanguage) {
            if (defaultLanguage != null && !defaultLanguage.isBlank()) {
                this.defaultLanguage = defaultLanguage;
            }
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            if (version != null && !version.isBlank()) {
                this.version = version;
            }
        }
    }

    public static class Cache {
        private String host;
        private int port;
        private long ttlSeconds = 3600;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public long getTtlSeconds() {
            return ttlSeconds;
        }

        public void setTtlSeconds(long ttlSeconds) {
            this.ttlSeconds = ttlSeconds;
        }
    }

    public static class Jikan {
        private String apiUrl = "https://api.jikan.moe/v4";
        private String userAgent = "AuraDiscordBot/1.0";

        public String getApiUrl() {
            return apiUrl;
        }

        public void setApiUrl(String apiUrl) {
            if (apiUrl != null && !apiUrl.isBlank()) {
                this.apiUrl = apiUrl;
            }
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            if (userAgent != null && !userAgent.isBlank()) {
                this.userAgent = userAgent;
            }
        }
    }
}
