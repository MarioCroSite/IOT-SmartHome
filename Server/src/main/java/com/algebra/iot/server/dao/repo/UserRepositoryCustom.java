/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algebra.iot.server.dao.repo;


import com.algebra.iot.server.dao.model.User;

/**
 *
 * @author matij
 */
public interface UserRepositoryCustom {
    void saveUserWithAuthorities(User user);
}
