package com.udacity.pricing.domain.price;

import com.udacity.pricing.service.PriceException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import javax.websocket.server.PathParam;
import java.util.Optional;

@RepositoryRestResource
public interface PriceRepository extends CrudRepository<Price, Long> {
    //this enables http://localhost:8082/prices/search/findByVehicleId?id=125
    Optional<Price> findByVehicleId(@Param("Id") Long id);
}
