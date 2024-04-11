package nl.bos.entity.eisconnector.helloworld;

import com.eibus.util.logger.CordysLogger;
import com.opentext.cordys.entityCore.connectors.genericEISConnector.repository.*;
import nl.bos.entity.eisconnector.helloworld.dao.IDataAccessObject;
import nl.bos.entity.eisconnector.helloworld.dao.Project;
import nl.bos.entity.eisconnector.helloworld.dao.ProjectDAO;

import java.util.*;

public class HelloWorldEISRepositoryProvider implements IEISRepositoryProvider {
    private static final CordysLogger LOGGER = CordysLogger.getCordysLogger(HelloWorldEISRepositoryProvider.class);
    private final IDataAccessObject projectDAO = new ProjectDAO();

    @Override
    public void init(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Key = %s, Value = %s", entry.getKey(), entry.getValue()));
            }
        }

        String username = map.get("username");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("username = %s", username));
        }
    }

    @Override
    public boolean testConnection() {
        return true;
    }

    @Override
    public EISEntityRow create(EISContext eisContext, EISRepositoryEntity eisRepositoryEntity, EISEntityRow eisEntityRow) {
        int id = generateUniqueId();
        projectDAO.save(new Project(id, String.valueOf(eisEntityRow.getColumns().get(1).getColumnValue())));

        eisEntityRow.setId(String.valueOf(id));
        eisEntityRow.getColumns().get(0).setColumnValue(id);
        return eisEntityRow;
    }

    @Override
    public EISEntityRow get(EISContext eisContext, EISRepositoryEntity eisRepositoryEntity, EISEntityRow eisEntityRow) {
        Project project = projectDAO.get((Integer) eisEntityRow.getColumns().get(0).getColumnValue());
        return buildEntityRow(project);
    }

    @Override
    public boolean update(EISContext eisContext, EISRepositoryEntity eisRepositoryEntity, EISEntityRow eisEntityRow) {
        Project project = projectDAO.get((Integer) eisEntityRow.getColumns().get(0).getColumnValue());
        project.setSubject(String.valueOf(eisEntityRow.getColumns().get(1).getColumnValue()));
        projectDAO.update(project.getId(), project);
        return true;
    }

    @Override
    public boolean delete(EISContext eisContext, EISRepositoryEntity eisRepositoryEntity, List<EISEntityRow> list) {
        for (EISEntityRow eisEntityRow : list) {
            Project project = projectDAO.get((Integer) eisEntityRow.getColumns().get(0).getColumnValue());
            if (project != null) {
                projectDAO.delete(project.getId());
            }
        }
        return true;
    }

    @Override
    public Long getTotalRecordCount(EISContext eisContext, EISRepositoryEntity eisRepositoryEntity, QueryWrapper queryWrapper) {
        return (long) projectDAO.getAll().size();
    }

    @Override
    public List<EISEntityRow> doQuery(EISContext eisContext, EISRepositoryEntity eisRepositoryEntity, QueryWrapper queryWrapper) {
        List<EISEntityRow> result = new ArrayList<>();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(queryWrapper.getJsonQuery());
        }

        List<Project> projects = projectDAO.getAll();
        for (Project project : projects) {
            result.add(buildEntityRow(project));
        }

        return result;
    }

    private EISEntityRow buildEntityRow(Project project) {
        EISEntityRow entityRow = new EISEntityRow();
        List<EISEntityRowColumn> columns = new ArrayList<>();

        EISEntityRowColumn id = new EISEntityRowColumn();
        id.setColumnName("id");
        id.setColumnValue(project.getId());
        columns.add(id);

        EISEntityRowColumn subject = new EISEntityRowColumn();
        subject.setColumnName("subject");
        subject.setColumnValue(project.getSubject());
        columns.add(subject);

        entityRow.setColumns(columns);
        return entityRow;
    }

    private int generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        int uid = String.valueOf(idOne).hashCode();
        return Integer.parseInt(Integer.toString(uid).replaceAll("-", ""));
    }
}
