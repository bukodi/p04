package com.bukodi.p04.repository;

import com.bukodi.p04.domain.Foo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Foo entity.
 */
@SuppressWarnings("unused")
public interface FooRepository extends JpaRepository<Foo,Long> {

}
