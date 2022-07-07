package ru.cft.stepuha.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.cft.stepuha.repository.model.PersonEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class PersonEntityRowMapper  implements RowMapper<PersonEntity> {
    @Override
    public PersonEntity mapRow (ResultSet rs, int rowNum) throws SQLException {
        PersonEntity result = new PersonEntity(rs.getLong("id"),
                                               rs.getString("first_name"),
                                               rs.getString("last_name"),
                                               rs.getString("middle_name"),
                                               rs.getString("login"),
                                               rs.getBigDecimal("money"));
        return result;
    }
}
