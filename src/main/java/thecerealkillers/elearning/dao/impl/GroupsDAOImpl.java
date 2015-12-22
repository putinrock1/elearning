package thecerealkillers.elearning.dao.impl;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import thecerealkillers.elearning.dao.GroupsDAO;
import thecerealkillers.elearning.exceptions.DAOException;
import thecerealkillers.elearning.model.Course;
import thecerealkillers.elearning.model.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with love by Lucian and @Pi on 22.12.2015.
 */
@Repository
public class GroupsDAOImpl implements GroupsDAO {

    private static final String NO_SUBSCRIBED_GROUPS = "There is no subscribed group for this course";
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Group> getAll() throws DAOException {
        try {
            List<Group> groupList;
            String sqlCommand = "SELECT * FROM elearning_db.`group`;";

            groupList = namedParameterJdbcTemplate.query(sqlCommand, new RowMapper<Group>() {
                @Override
                public Group mapRow(ResultSet resultSet, int i) throws SQLException {
                    Group group = new Group();

                    group.setName(resultSet.getString("name"));
                    return group;
                }
            });
            return groupList;
        } catch (Exception ex) {
            throw new DAOException(ex.getMessage());
        }
    }

    @Override
    public void add(Group group) throws DAOException {
        try {
            String sqlCommand = "INSERT INTO `elearning_db`.`group` VALUE (:name);";
            Map<String, String> namedParameters = new HashMap<>();

            namedParameters.put("name", group.getName());
            namedParameterJdbcTemplate.update(sqlCommand, namedParameters);
        } catch (Exception ex) {
            throw new DAOException(ex.getMessage());
        }
    }

    @Override
    public void remove(String name) throws DAOException {
        try {
            String sql = "DELETE FROM `elearning_db`.`group` WHERE name = :name;";
            Map<String, String> namedParameters = Collections.singletonMap("name", name);

            namedParameterJdbcTemplate.update(sql, namedParameters);
        } catch (Exception exception) {
            throw new DAOException(exception.getMessage());
        }
    }

    @Override
    public void addCourseGroup(Course course, Group group) throws DAOException {
        try {
            String sql = "INSERT INTO course_group VALUES (:course, :group);";

            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("course", course.getTitle());
            namedParameters.put("group", group.getName());

            namedParameterJdbcTemplate.update(sql, namedParameters);
        } catch (Exception ex) {
            throw new DAOException(ex.getMessage());
        }
    }
}
