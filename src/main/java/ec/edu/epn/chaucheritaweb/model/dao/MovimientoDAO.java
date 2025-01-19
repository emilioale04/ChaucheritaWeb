package ec.edu.epn.chaucheritaweb.model.dao;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ec.edu.epn.chaucheritaweb.model.entities.Egreso;
import ec.edu.epn.chaucheritaweb.model.entities.Ingreso;
import ec.edu.epn.chaucheritaweb.model.entities.Movimiento;
import ec.edu.epn.chaucheritaweb.model.entities.Transferencia;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;

import java.util.HashMap;

public class MovimientoDAO extends BaseDAO<Movimiento> {
    
    public MovimientoDAO() {
        super(Movimiento.class);
    }

    public List<Movimiento> findByUsuario(Usuario usuario) {
        return entityManager.createQuery(
            "SELECT m FROM Movimiento m WHERE m.cuenta.usuario = :usuario ORDER BY m.fecha DESC", 
            Movimiento.class)
            .setParameter("usuario", usuario)
            .getResultList();
    }

    public List<Movimiento> findWithFilters(Usuario usuario, Integer cuentaId, 
            Integer categoriaId, Date fechaInicio, Date fechaFin, String tipo) {
            
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT m FROM Movimiento m WHERE m.cuenta.usuario = :usuario ");
        
        Map<String, Object> params = new HashMap<>();
        params.put("usuario", usuario);

        if (cuentaId != null) {
            jpql.append("AND m.cuenta.id = :cuentaId ");
            params.put("cuentaId", cuentaId);
        }

        if (categoriaId != null) {
            jpql.append("AND m.categoria.id = :categoriaId ");
            params.put("categoriaId", categoriaId);
        }

        if (fechaInicio != null && fechaFin != null) {
            jpql.append("AND m.fecha BETWEEN :fechaInicio AND :fechaFin ");
            params.put("fechaInicio", fechaInicio);
            params.put("fechaFin", fechaFin);
        }

        if (tipo != null && !tipo.isEmpty()) {
            jpql.append("AND TYPE(m) = :tipo ");
            Class<?> tipoClase = switch(tipo.toUpperCase()) {
                case "INGRESO" -> Ingreso.class;
                case "EGRESO" -> Egreso.class;
                case "TRANSFERENCIA" -> Transferencia.class;
                default -> throw new IllegalArgumentException("Tipo de movimiento no válido");
            };
            params.put("tipo", tipoClase);
        }

        jpql.append("ORDER BY m.fecha DESC");

        TypedQuery<Movimiento> query = entityManager.createQuery(jpql.toString(), Movimiento.class);
        params.forEach(query::setParameter);

        return query.getResultList();
    }
}