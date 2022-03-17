package br.com.todo.todo.repository;

import br.com.todo.todo.model.Meta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long > {

    Page<Meta> findAllByUsuarioId(Long idUsuario, Pageable paginacao);
}
