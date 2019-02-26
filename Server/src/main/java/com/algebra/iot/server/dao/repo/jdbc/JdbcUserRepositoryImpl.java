package com.algebra.iot.server.dao.repo.jdbc;

import com.algebra.iot.server.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcUserRepositoryImpl implements JdbcUserRepository{

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert userInserter;
    private final SimpleJdbcInsert authorityInserter;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.userInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("USERS");
        this.authorityInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("AUTHORITIES");
    }

    @Override
    public User findByUsername(String username) {
        return jdbc.queryForObject("SELECT * FROM USERS WHERE USERNAME = ?", this::mapRowToUser, username);
    }


    @Override
    public User create(User user) {
        Map<String, Object> userValues = new HashMap<>();

        userValues.put("USERNAME", user.getUsername());
        userValues.put("PASSWORD", user.getPassword());
        userValues.put("ENABLED", true);

        userInserter.execute(userValues);

        Map<String, Object> authorityValues = new HashMap<>();
        authorityValues.put("USERNAME", user.getUsername());
        authorityValues.put("AUTHORITY", "ROLE_USER");
        authorityInserter.execute(authorityValues);

        return user;
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getString("USERNAME"),
                rs.getString("PASSWORD")
        );
    }
}
