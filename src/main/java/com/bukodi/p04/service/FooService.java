package com.bukodi.p04.service;

import com.bukodi.p04.domain.Foo;
import com.bukodi.p04.repository.FooRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Foo.
 */
@Service
@Transactional
public class FooService {

    private final Logger log = LoggerFactory.getLogger(FooService.class);
    
    @Inject
    private FooRepository fooRepository;

    /**
     * Save a foo.
     *
     * @param foo the entity to save
     * @return the persisted entity
     */
    public Foo save(Foo foo) {
        log.debug("Request to save Foo : {}", foo);
        Foo result = fooRepository.save(foo);
        return result;
    }

    /**
     *  Get all the foos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Foo> findAll(Pageable pageable) {
        log.debug("Request to get all Foos");
        Page<Foo> result = fooRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one foo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Foo findOne(Long id) {
        log.debug("Request to get Foo : {}", id);
        Foo foo = fooRepository.findOne(id);
        return foo;
    }

    /**
     *  Delete the  foo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Foo : {}", id);
        fooRepository.delete(id);
    }
}
