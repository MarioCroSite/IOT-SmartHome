package com.algebra.iot.server.dao.repo;

import com.algebra.iot.server.dao.model.Gateway;
import org.springframework.data.repository.CrudRepository;

public interface GatewayRepository extends CrudRepository<Gateway,Integer> {
    Gateway findFirstByOrderByIdDesc();


}
