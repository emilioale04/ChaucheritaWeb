package ec.edu.epn.chaucheritaweb.controller;

import ec.edu.epn.chaucheritaweb.model.dao.*;
import ec.edu.epn.chaucheritaweb.model.entities.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;

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
        List<Cuenta> cuentas = cuentaDAO.findByUsuario(usuario);

        req.setAttribute("cuenta", cuenta);
        req.setAttribute("cuentas", cuentas);
        req.setAttribute("categorias", categorias);

        req.getRequestDispatcher("jsp/realizarMovimiento.jsp").forward(req, resp);
    }

    private void realizarIngreso(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
            BigDecimal valor = new BigDecimal(req.getParameter("valor"));
            String concepto = req.getParameter("concepto");
            int categoriaId = Integer.parseInt(req.getParameter("categoriaId"));

            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "El valor debe ser mayor a cero.");
                return;
            }

            if (concepto == null || concepto.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "El concepto no puede estar vacío.");
                return;
            }

            CuentaDAO cuentaDAO = new CuentaDAO();
            CategoriaDAO categoriaDAO = new CategoriaDAO();

            Cuenta cuenta = cuentaDAO.findById(cuentaId);
            if (cuenta == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "La cuenta especificada no existe.");
                return;
            }

            Categoria categoria = categoriaDAO.findById(categoriaId);
            if (categoria == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "La categoría especificada no existe.");
                return;
            }

            Movimiento ingreso = new Ingreso();
            ingreso.setValor(valor);
            ingreso.setConcepto(concepto.trim());
            ingreso.setCategoria(categoria);
            ingreso.setCuenta(cuenta);
            ingreso.setFecha(LocalDateTime.now());

            cuentaDAO.realizarMovimiento(cuenta, ingreso);

            req.setAttribute("cuentaId", cuenta.getId());
            req.getRequestDispatcher("realizarMovimientoController?ruta=realizarMovimiento").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros numéricos inválidos.");
        } catch (NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan parámetros obligatorios.");
        }
    }

    private void realizarEgreso(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
            BigDecimal valor = new BigDecimal(req.getParameter("valor"));
            String concepto = req.getParameter("concepto");
            int categoriaId = Integer.parseInt(req.getParameter("categoriaId"));

            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "El valor debe ser mayor a cero.");
                return;
            }

            if (concepto == null || concepto.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "El concepto no puede estar vacío.");
                return;
            }

            CuentaDAO cuentaDAO = new CuentaDAO();
            CategoriaDAO categoriaDAO = new CategoriaDAO();

            Cuenta cuenta = cuentaDAO.findById(cuentaId);
            if (cuenta == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "La cuenta especificada no existe.");
                return;
            }

            Categoria categoria = categoriaDAO.findById(categoriaId);
            if (categoria == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "La categoría especificada no existe.");
                return;
            }

            Movimiento egreso = new Egreso();
            egreso.setValor(valor);
            egreso.setConcepto(concepto.trim());
            egreso.setCategoria(categoria);
            egreso.setCuenta(cuenta);
            egreso.setFecha(LocalDateTime.now());

            cuentaDAO.realizarMovimiento(cuenta, egreso);

            req.setAttribute("cuentaId", cuenta.getId());
            req.getRequestDispatcher("realizarMovimientoController?ruta=realizarMovimiento").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros numéricos inválidos.");
        } catch (NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan parámetros obligatorios.");
        }
    }

    private void realizarTransferencia(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            int cuentaOrigenId = Integer.parseInt(req.getParameter("cuentaId"));
            int cuentaDestinoId = Integer.parseInt(req.getParameter("cuentaDestinoId"));
            BigDecimal valor = new BigDecimal(req.getParameter("valor"));
            String concepto = req.getParameter("concepto");
            int categoriaId = Integer.parseInt(req.getParameter("categoriaId"));

            CuentaDAO cuentaDAO = new CuentaDAO();
            CategoriaDAO categoriaDAO = new CategoriaDAO();

            Cuenta cuentaOrigen = cuentaDAO.findById(cuentaOrigenId);
            Cuenta cuentaDestino = cuentaDAO.findById(cuentaDestinoId);

            if (cuentaOrigen == null || cuentaDestino == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros inválidos o cuentas inexistentes.");
                return;
            }

            Categoria categoria = categoriaDAO.findById(categoriaId);
            if (categoria == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "La categoría especificada no existe.");
                return;
            }

            Transferencia transferencia = new Transferencia();
            transferencia.setValor(valor);
            transferencia.setConcepto(concepto);
            transferencia.setCuenta(cuentaOrigen);
            transferencia.setCuentaDestino(cuentaDestino);
            transferencia.setCategoria(categoria);
            transferencia.setFecha(LocalDateTime.now());

            cuentaDAO.realizarMovimiento(cuentaOrigen, cuentaDestino, transferencia);

            req.setAttribute("cuentaId", cuentaOrigen.getId());
            req.getRequestDispatcher("realizarMovimientoController?ruta=realizarMovimiento").forward(req, resp);
        } catch (NumberFormatException | NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros inválidos.");
        }
    }
}
