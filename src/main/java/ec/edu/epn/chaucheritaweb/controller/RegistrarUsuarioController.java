package ec.edu.epn.chaucheritaweb.controller;

import ec.edu.epn.chaucheritaweb.model.dao.UsuarioDAO;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registroController")
public class RegistrarUsuarioController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.registrar(req, resp);
    }

    private void registrar(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String nombre = req.getParameter("nombre");
        String usuario = req.getParameter("usuario");
        String clave = req.getParameter("clave");

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setUsuario(usuario);
        nuevoUsuario.setClave(clave);

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {
            usuarioDAO.registrar(nombre, usuario, clave);
            req.setAttribute("mensaje", "Usuario registrado exitosamente.");
            req.getRequestDispatcher("jsp/login.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Hubo un error al registrar el usuario.");
            req.getRequestDispatcher("jsp/registro.jsp").forward(req, resp);
        }
    }
}
