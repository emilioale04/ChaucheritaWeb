package ec.edu.epn.chaucheritaweb.controller;

import java.io.IOException;
import java.util.List;

import ec.edu.epn.chaucheritaweb.model.dao.CategoriaDAO;
import ec.edu.epn.chaucheritaweb.model.entities.Categoria;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GestionarCategoria")
public class GestionarCategoriaController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ruteador(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ruteador(request, response);
    }

    private void ruteador(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String ruta = (req.getParameter("ruta") == null) ? "listarCategorias" : req.getParameter("ruta");

        switch (ruta) {
            case "listarCategorias":
                listarCategorias(req, resp);
                break;
            case "cargarFormularioEdicion":
                cargarFormularioEdicion(req, resp);
                break;
            case "guardarCategoria":
                guardarCategoria(req, resp);
                break;
            case "eliminarCategoria":
                eliminarCategoria(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ruta no válida.");
                break;
        }
    }

    private void listarCategorias(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<Categoria> categorias = categoriaDAO.listarPorUsuario(usuario);
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("jsp/gestionarCategoria.jsp").forward(request, response);
    }

    private void cargarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Categoria categoria = categoriaDAO.findById(id);
        if (categoria == null) {
            request.setAttribute("error", "Categoría no encontrada.");
            listarCategorias(request, response);
            return;
        }
        request.setAttribute("categoria", categoria);
        listarCategorias(request, response); // Mantenerse en la misma vista
    }

    private void guardarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        String idStr = request.getParameter("id");
        String nombreCategoria = request.getParameter("nombre");

        if (nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            request.setAttribute("error", "El nombre de la categoría es obligatorio.");
            listarCategorias(request, response);
            return;
        }

        Categoria categoria;
        if (idStr == null || idStr.isEmpty()) {
            categoria = new Categoria();
        } else {
            int id = Integer.parseInt(idStr);
            categoria = categoriaDAO.findById(id);
            if (categoria == null) {
                request.setAttribute("error", "Categoría no encontrada.");
                listarCategorias(request, response);
                return;
            }
        }
        categoria.setNombreCategoria(nombreCategoria);
        categoria.setUsuario(usuario);

        if (categoria.getId() == null) {
            categoriaDAO.crear(categoria);
        } else {
            categoriaDAO.actualizar(categoria);
        }

        response.sendRedirect("GestionarCategoria");
    }

    private void eliminarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        categoriaDAO.eliminar(id);
        response.sendRedirect("GestionarCategoria");
    }
}
