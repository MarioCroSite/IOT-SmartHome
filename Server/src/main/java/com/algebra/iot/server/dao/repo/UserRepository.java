/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algebra.iot.server.dao.repo;

import com.algebra.iot.server.dao.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author matij
 */
public interface UserRepository extends CrudRepository<User, String>, UserRepositoryCustom{
}
