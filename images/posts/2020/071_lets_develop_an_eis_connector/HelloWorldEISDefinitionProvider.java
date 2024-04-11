package nl.bos.entity.eisconnector.helloworld;

import com.opentext.cordys.entityCore.connectors.genericEISConnector.definition.EISEntity;
import com.opentext.cordys.entityCore.connectors.genericEISConnector.definition.EISEntityProperty;
import com.opentext.cordys.entityCore.connectors.genericEISConnector.definition.IEISDefinitionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HelloWorldEISDefinitionProvider implements IEISDefinitionProvider {

    private static final String PROJECT = "project";
    private static final String TASK = "task";

    @Override
    public void init(Map<String, String> map) {
        //Do nothing
    }

    @Override
    public EISEntity getEntity(String entityName) {
        switch (entityName) {
            case PROJECT:
                return HelloWorldEISDefinitionProvider.build(PROJECT);
            case TASK:
                return HelloWorldEISDefinitionProvider.build(TASK);
            default:
                return new EISEntity();
        }
    }

    @Override
    public List<EISEntity> getEntities() {
        List<EISEntity> entities = new ArrayList<>();
        entities.add(build(PROJECT));
        entities.add(build(TASK));
        return entities;
    }

    private static EISEntity build(String entityName) {
        EISEntity eisEntity = new EISEntity();
        eisEntity.setName(entityName);

        //ExternalId Property
        EISEntityProperty id = new EISEntityProperty();
        id.setId("id");
        id.setName("id");
        id.setChangeability(EISEntityProperty.Changeability.NEVER);
        id.setType(EISEntityProperty.Type.INTEGER);
        id.setDescription("Entity External ID");
        id.setPrimaryKey(true);

        //Subject property
        EISEntityProperty subject = new EISEntityProperty();
        subject.setId("subject");
        subject.setName("subject");
        subject.setDisplayName("Subject");
        subject.setChangeability(EISEntityProperty.Changeability.ANY_TIME);
        subject.setType(EISEntityProperty.Type.TEXT);
        subject.setDescription("Entity Subject");
        subject.setLength(64);
        subject.setPrimaryKey(false);

        eisEntity.addProperty(id);
        eisEntity.addProperty(subject);

        return eisEntity;
    }
}
