package com.onarandombox.multiverseprofiles.config;

import com.onarandombox.multiverseprofiles.MultiverseProfiles;
import com.onarandombox.multiverseprofiles.util.MinecraftTools;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author dumptruckman
 */
public class ProfilesConfigImpl implements ProfilesConfig {

    public enum Path {
        LANGUAGE_FILE_NAME("settings.language_file", "english.yml", "# This is the language file you wish to use."),
        DEBUG_MODE("settings.debug_mode.enable", false, "# Enables debug mode."),
        DATA_SAVE_PERIOD("settings.data.save_every", 120, "# This is often plugin data is written to the disk.", "# This setting indicates the maximum amount of inventory rollback possible in the event of a server crash."),

        DEFAULT_SHARING_INV("defaults.sharing.inventory", true, "# "),
        DEFAULT_SHARING_ARMOR("defaults.sharing.armor", true, "# "),
        DEFAULT_SHARING_HEALTH("defaults.sharing.health", true, "# "),
        DEFAULT_SHARING_HUNGER("defaults.sharing.hunger", true, "# "),
        DEFAULT_SHARING_EXP("defaults.sharing.experience", true, "# "),
        DEFAULT_SHARING_EFFECTS("defaults.sharing.effects", true, "# "),;

        private String path;
        private Object def;
        private String[] comments;

        Path(String path, Object def, String... comments) {
            this.path = path;
            this.def = def;
            this.comments = comments;
        }

        public final Boolean getBoolean() {
            return MultiverseProfiles.getConfig().getConfig().getBoolean(path, (Boolean) def);
        }

        public final Integer getInt() {
            return MultiverseProfiles.getConfig().getConfig().getInt(path, (Integer) def);
        }

        public final String getString() {
            return MultiverseProfiles.getConfig().getConfig().getString(path, (String) def);
        }

        /**
         * Retrieves the path for a config option
         *
         * @return The path for a config option
         */
        private String getPath() {
            return path;
        }

        /**
         * Retrieves the default value for a config path
         *
         * @return The default value for a config path
         */
        private Object getDefault() {
            return def;
        }

        /**
         * Retrieves the comment for a config path
         *
         * @return The comments for a config path
         */
        private String[] getComments() {
            if (comments != null) {
                return comments;
            }

            String[] comments = new String[1];
            comments[0] = "";
            return comments;
        }
    }

    private CommentedConfiguration config;

    /**
     * Loads the configuration data into memory and sets defaults
     *
     * @throws IOException
     */
    public void load() throws Exception {
        // Make the data folders
        MultiverseProfiles.getPlugin().getDataFolder().mkdirs();

        // Check if the config file exists.  If not, create it.
        File configFile = new File(MultiverseProfiles.getPlugin().getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.createNewFile();
        }

        // Load the configuration file into memory
        config = new CommentedConfiguration(configFile);
        config.load();

        // Sets defaults config values
        this.setDefaults();

        // Saves the configuration from memory to file
        config.save();

        //this.setDefaultShares();
    }

    /**
     * Loads default settings for any missing config values
     */
    private void setDefaults() {
        for (ProfilesConfigImpl.Path path : ProfilesConfigImpl.Path.values()) {
            config.addComment(path.getPath(), path.getComments());
            if (config.getString(path.getPath()) == null) {
                config.set(path.getPath(), path.getDefault());
            }
        }
    }

    /*public void setDefaultShares() {
        MultiverseProfiles.setDefaultShares(this.getDefaultShares());
    }*/

    @Override
    public FileConfiguration getConfig() {
        return this.config;
    }

    @Override
    public boolean isDebugging() {
        return Path.DEBUG_MODE.getBoolean();
    }

    @Override
    public long getDataSaveInterval() {
        return MinecraftTools.convertSecondsToTicks(Path.DATA_SAVE_PERIOD.getInt());
    }

    @Override
    public String getLanguageFileName() {
        return Path.LANGUAGE_FILE_NAME.getString();
    }

    @Override
    public void loadWorldGroups() {

    }

    /*@Override
    public Shares getDefaultShares() {
        return new Shares(
                Path.DEFAULT_SHARING_INV.getBoolean(),
                Path.DEFAULT_SHARING_HEALTH.getBoolean(),
                Path.DEFAULT_SHARING_HUNGER.getBoolean(),
                Path.DEFAULT_SHARING_EXP.getBoolean(),
                Path.DEFAULT_SHARING_EFFECTS.getBoolean());
    }*/
}