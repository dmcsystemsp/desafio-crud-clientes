package com.dmcsystemsp.crudclientes.services;

import com.dmcsystemsp.crudclientes.dto.ClientDTO;
import com.dmcsystemsp.crudclientes.entities.Client;
import com.dmcsystemsp.crudclientes.repositories.ClientRepository;
import com.dmcsystemsp.crudclientes.services.exceptions.DataBaseException;
import com.dmcsystemsp.crudclientes.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.Column;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Client client = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado!"));
        return new ClientDTO(client);
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable){
        Page<Client> result = repository.findAll(pageable);
        return result.map(x -> new ClientDTO(x));

    }

    @Transactional
    public ClientDTO insert(ClientDTO dto){
        Client entity = new Client();

        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto){
        try {
            Client entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ClientDTO(entity);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id não emcontrado!");
        }
        catch (DataIntegrityViolationException e){
            throw new DataBaseException("Falha de Integridade Referencial");
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        try{
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id não emcontrado!");
        }

    }

    private void copyDtoToEntity(ClientDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());

    }


}
