package ec.edu.epn.chaucheritaweb.controller;

import ec.edu.epn.chaucheritaweb.model.dao.CategoriaDAO;
import ec.edu.epn.chaucheritaweb.model.dao.CuentaDAO;
import ec.edu.epn.chaucheritaweb.model.entities.Categoria;
import ec.edu.epn.chaucheritaweb.model.entities.Cuenta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import ec.edu.epn.chaucheritaweb.model.dao.UsuarioDAO;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;

@WebServlet("/realizarMovimientoController")
public class RealizarMovimientoController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    private void ruteador(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String ruta = (req.getParameter("ruta") == null) ? "realizarMovimiento" : req.getParameter("ruta");

        switch (ruta) {
            case "realizarMovimiento":
                this.realizarMovimiento(req, resp);
                break;
            case "realizarIngreso":
                this.realizarIngreso(req, resp);
                break;
            case "realizarEgreso":
                this.realizarEgreso(req, resp);
                break;
            case "realizarTransferencia":
                this.realizarTransferencia(req, resp);
                break;
        }
    }

    private void realizarMovimiento(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));

        CuentaDAO cuentaDAO = new CuentaDAO();
        CategoriaDAO categoriaDAO = new CategoriaDAO();

        Cuenta cuenta = cuentaDAO.findById(cuentaId);
        List<Categoria> categorias = categoriaDAO.listarPorUsuario(usuario);

        req.setAttribute("cuenta", cuenta);
        req.setAttribute("categorias", categorias);

        req.getRequestDispatcher("jsp/realizarMovimiento.jsp").forward(req, resp);
    }

    private void realizarIngreso(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("realizarMovimientoController?ruta=realizarMovimiento");
    }

    private void realizarEgreso(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("realizarMovimientoController?ruta=realizarMovimiento");
    }

    private void realizarTransferencia(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("realizarMovimientoController?ruta=realizarMovimiento");
    }
}
