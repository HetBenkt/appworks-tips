package nl.bos.entity.eisconnector.helloworld;

import com.opentext.cordys.entityCore.connectors.genericEISConnector.config.EISRepositoryExternalConfigurations;
import com.opentext.cordys.entityCore.connectors.genericEISConnector.config.IEISRepositoryExternalConfiguration;
import com.opentext.platform.configuration.api.Component;
import com.opentext.platform.configuration.api.ComponentDefinition;
import com.opentext.platform.configuration.api.InstanceSettingComponentBase;
import com.opentext.platform.configuration.api.SettingDefinition;
import com.opentext.platform.configuration.api.Settings;

import java.util.HashMap;
import java.util.Map;

@ComponentDefinition(name = EISRepositoryConfigurationExternal.COMPONENT_NAME, parent = EISRepositoryExternalConfigurations.class)
public class EISRepositoryConfigurationExternal extends InstanceSettingComponentBase implements IEISRepositoryExternalConfiguration {
    protected static final String COMPONENT_NAME = "eisRepositoryConfigurationExternal";


    public EISRepositoryConfigurationExternal(Component parent, String name) {
        super(parent, name);
    }

    @SettingDefinition(name = "username", defaultValue = "")
    public String getUsername() {
        return Settings.getSetting(this, null, null);
    }

    @SettingDefinition(name = "password", defaultValue = "")
    public String getPassword() {
        return Settings.getSetting(this, null, null);
    }

    @SettingDefinition(name = "domain", defaultValue = "")
    public String getDomain() {
        return Settings.getSetting(this, null, null);
    }

    @SettingDefinition(name = "repository", defaultValue = "")
    public String getRepository() {
        return Settings.getSetting(this, null, null);
    }

    @Override
    public Map<String, String> getPropertiesMap() {
        Map<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("username", this.getUsername());
        propertiesMap.put("password", this.getPassword());
        propertiesMap.put("domain", this.getDomain());
        propertiesMap.put("repository", this.getRepository());
        return propertiesMap;
    }
}
