package com.dmcsystemsp.crudclientes.repositories;

import com.dmcsystemsp.crudclientes.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {

}
