package ec.edu.epn.chaucheritaweb.apirest;

import ec.edu.epn.chaucheritaweb.model.dao.CategoriaDAO;
import ec.edu.epn.chaucheritaweb.model.dao.UsuarioDAO;
import ec.edu.epn.chaucheritaweb.model.entities.Categoria;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/categoria")
public class CrudCategoriaRecurso {

    private final CategoriaDAO categoriaDAO;
    private final UsuarioDAO usuarioDAO;

    public CrudCategoriaRecurso() {
        this.categoriaDAO = new CategoriaDAO();
        this.usuarioDAO = new UsuarioDAO();
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
        Usuario usuario = usuarioDAO.findById(usuarioId);
        if (usuario == null) {
            throw new WebApplicationException("Usuario no encontrado", Response.Status.NOT_FOUND);
        }
        return categoriaDAO.listarPorUsuario(usuario);
    }

    @POST
    @Path("/crear")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crear(Categoria categoria) {
        if (categoria.getUsuario() == null || categoria.getUsuario().getId() == 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El usuario es requerido para crear la categoría")
                    .build();
        }

        Usuario usuario = usuarioDAO.findById(categoria.getUsuario().getId());
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuario no encontrado")
                    .build();
        }

        categoria.setUsuario(usuario);
        categoriaDAO.crear(categoria);
        return Response.status(Response.Status.CREATED).entity(categoria).build();
    }

    @PUT
    @Path("/actualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(Categoria categoria) {
        Categoria categoriaExistente = categoriaDAO.findById(categoria.getId());
        if (categoriaExistente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Categoría no encontrada")
                    .build();
        }

        categoriaExistente.setNombreCategoria(categoria.getNombreCategoria());

        if (categoria.getUsuario() != null && categoria.getUsuario().getId() != 0) {
            Usuario usuario = usuarioDAO.findById(categoria.getUsuario().getId());
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Usuario no encontrado")
                        .build();
            }
            categoriaExistente.setUsuario(usuario);
        }

        categoriaDAO.actualizar(categoriaExistente);
        return Response.ok().entity("Categoría actualizada").build();
    }

    @DELETE
    @Path("/eliminar/{id}")
    public Response eliminar(@PathParam("id") int id) {
        Categoria categoria = categoriaDAO.findById(id);
        if (categoria == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Categoría no encontrada")
                    .build();
        }
        categoriaDAO.eliminar(id);
        return Response.ok().entity("Categoría eliminada exitosamente").build();
    }
}
