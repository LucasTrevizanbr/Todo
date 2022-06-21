package br.com.todo.dominio.repositorios;

import br.com.todo.dominio.modelos.Meta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long > {

    Page<Meta> findAllByUsuarioId(Long idUsuario, Pageable paginacao);

    @Query("FROM Meta m WHERE m.historicoDatasMeta.dataFinalizacaoEstipulada BETWEEN :inicioDoDia AND :fimDoDia " +
            "AND m.status <> 'CONCLUIDA'" )
    List<Meta> listarMetasNoDiaFinal(LocalDateTime inicioDoDia, LocalDateTime fimDoDia);
}
