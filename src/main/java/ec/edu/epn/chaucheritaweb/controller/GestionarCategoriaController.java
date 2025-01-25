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
        String action = request.getParameter("action");
        try {
            if (action == null || action.isEmpty()) {
                listarCategorias(request, response);
            } else if ("editar".equals(action)) {
                cargarFormularioEdicion(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("crear".equals(action)) {
                guardarCategoria(request, response);
            } else if ("editar".equals(action)) {
                guardarCategoria(request, response);
            } else if ("eliminar".equals(action)) {
                eliminarCategoria(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            listarCategorias(request, response);
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
        int id = Integer.parseInt(request.getParameter("id")); // Reemplazado por int
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
            int id = Integer.parseInt(idStr); // Reemplazado por int
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
        int id = Integer.parseInt(request.getParameter("id")); // Reemplazado por int
        categoriaDAO.eliminar(id);
        response.sendRedirect("GestionarCategoria");
    }
}
