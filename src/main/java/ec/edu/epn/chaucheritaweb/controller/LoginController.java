package ec.edu.epn.chaucheritaweb.controller;

import ec.edu.epn.chaucheritaweb.model.dao.CuentaDAO;
import ec.edu.epn.chaucheritaweb.model.entities.Cuenta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.List;

import ec.edu.epn.chaucheritaweb.model.dao.UsuarioDAO;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;

@WebServlet("/loginController")
public class LoginController extends HttpServlet {

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
        String ruta = (req.getParameter("ruta") == null) ? "ingresar" : req.getParameter("ruta");

        switch (ruta) {
            case "ingresar":
                this.ingresar(req, resp);
                break;
            case "login":
            	this.login(req, resp);
                break;
        }
    }

    private void ingresar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("jsp/login.jsp");
    }
    
    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String usuario = req.getParameter("usuario");
        String clave = req.getParameter("clave");
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        Usuario u = usuarioDAO.autenticar(usuario, clave);

        CuentaDAO cuentaDAO = new CuentaDAO();

        List<Cuenta> cuentas = cuentaDAO.findByUsuario(u);

        req.setAttribute("cuentas", cuentas);
        
        if(u != null) {
        	req.getSession().setAttribute("usuario", u);
        	resp.sendRedirect("menuController?ruta=home");
        } else {
        	resp.sendRedirect("jsp/login.jsp");
        }
    }
}
