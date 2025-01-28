package ec.edu.epn.chaucheritaweb.apirest;

import ec.edu.epn.chaucheritaweb.model.dao.CategoriaDAO;
import ec.edu.epn.chaucheritaweb.model.dao.UsuarioDAO;
import ec.edu.epn.chaucheritaweb.model.entities.Categoria;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/categoria")
public class CrudCategoriaRecurso {

    private CategoriaDAO categoriaDAO;

    public CrudCategoriaRecurso() {
        categoriaDAO = new CategoriaDAO();
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Categoria> listarCategorias() {
        return categoriaDAO.findAll();
    }

    @GET
    @Path("/encontrar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Categoria encontrarPorId(@PathParam("id") int id) {
        return categoriaDAO.findById(id);
    }


    @GET
    @Path("/listarPorUsuario/{usuarioId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Categoria> listarCategoriasPorUsuarioId(@PathParam("usuarioId") int usuarioId) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.findById(usuarioId);
        return categoriaDAO.listarPorUsuario(usuario);
    }

    @POST
    @Path("/crear")
    @Consumes(MediaType.APPLICATION_JSON)
    public void crear(Categoria categoria) {
        categoriaDAO.crear(categoria);
    }

    @PUT
    @Path("/actualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    public void actualizar(Categoria categoria) {

        Categoria categoriaExistente = categoriaDAO.findById(categoria.getId());
        if (categoriaExistente == null) {
            throw new WebApplicationException("Categoría no encontrada", 404);
        }

        categoriaExistente.setNombreCategoria(categoria.getNombreCategoria());

        if (categoria.getUsuario() != null && categoria.getUsuario().getId() != 0) {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.findById(categoria.getUsuario().getId());
            if (usuario == null) {
                throw new WebApplicationException("Usuario no encontrado", 404);
            }
            categoriaExistente.setUsuario(usuario);
        }

        categoriaDAO.actualizar(categoriaExistente);
    }


    @DELETE
    @Path("/eliminar/{id}")
    public void eliminar(@PathParam("id") int id) {
        categoriaDAO.eliminar(id);
    }
}
