package ec.edu.epn.chaucheritaweb.controller;

import ec.edu.epn.chaucheritaweb.model.dao.CuentaDAO;
import ec.edu.epn.chaucheritaweb.model.entities.Cuenta;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/menuController")
public class MenuController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    private void ruteador(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ruta = (req.getParameter("ruta") == null) ? "home" : req.getParameter("ruta");

        switch (ruta) {
            case "home":
                this.cargarHome(req, resp);
                break;
            case "logout":
                this.logout(req, resp);
                break;
            default:
                resp.sendRedirect("/jsp/login.jsp");
                break;
        }
    }

    private void cargarHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        
        if (usuario == null) {
            resp.sendRedirect("/jsp/login.jsp");
            return;
        }

        CuentaDAO cuentaDAO = new CuentaDAO();
        List<Cuenta> cuentas = cuentaDAO.findByUsuario(usuario);
        
        req.setAttribute("cuentas", cuentas);
        req.setAttribute("usuario", usuario);
        
        req.getRequestDispatcher("/jsp/home.jsp").forward(req, resp);
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        resp.sendRedirect("/jsp/login.jsp");
    }
}
