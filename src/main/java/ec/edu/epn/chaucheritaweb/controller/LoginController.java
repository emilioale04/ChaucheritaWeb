package ec.edu.epn.chaucheritaweb.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

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

    private void ruteador(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String ruta = (req.getParameter("ruta") == null) ? "ingresar" : req.getParameter("ruta");

        switch (ruta) {
            case "ingresar":
                this.ingresar(req, resp);
                break;
            case "login":
//                this.login(req, resp);
                break;
        }
    }

    private void ingresar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("jsp/login.jsp");
    }
}
