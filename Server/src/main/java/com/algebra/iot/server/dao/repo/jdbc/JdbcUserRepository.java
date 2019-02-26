package com.algebra.iot.server.dao.repo.jdbc;

import com.algebra.iot.server.dao.model.User;

public interface JdbcUserRepository {
    User create(User user);
    User findByUsername(String username);
}
