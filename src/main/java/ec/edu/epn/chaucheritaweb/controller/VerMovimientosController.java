package ec.edu.epn.chaucheritaweb.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import ec.edu.epn.chaucheritaweb.model.dao.CategoriaDAO;
import ec.edu.epn.chaucheritaweb.model.dao.MovimientoDAO;
import ec.edu.epn.chaucheritaweb.model.dao.CuentaDAO;
import ec.edu.epn.chaucheritaweb.model.entities.Categoria;
import ec.edu.epn.chaucheritaweb.model.entities.Movimiento;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;
import ec.edu.epn.chaucheritaweb.model.entities.Cuenta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/verMovimientos")
public class VerMovimientosController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private MovimientoDAO movimientoDAO;
    private CategoriaDAO categoriaDAO;
    private CuentaDAO cuentaDAO;

    @Override
    public void init() throws ServletException {
        movimientoDAO = new MovimientoDAO();
        categoriaDAO = new CategoriaDAO();
        cuentaDAO = new CuentaDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ruteador(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ruteador(request, response);
    }

    private void ruteador(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "jsp/login.jsp");
            return;
        }

        // Si es una petición POST, siempre va a filtrar
        if (request.getMethod().equals("POST")) {
            filtrarMovimientos(request, response);
            return;
        }

        // Si es GET, muestra los movimientos normalmente
        mostrarMovimientos(request, response);
    }

    private void mostrarMovimientos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            List<Categoria> categorias = categoriaDAO.listarPorUsuario(usuario);
            List<Cuenta> cuentas = cuentaDAO.findByUsuario(usuario);
            
            // Obtener parámetros de filtro de la URL (para GET)
            Integer cuentaId = getIntParameter(request, "cuenta");
            Integer categoriaId = getIntParameter(request, "categoria");
            Date fechaInicio = getFechaParameter(request, "fechaInicio");
            Date fechaFin = getFechaParameter(request, "fechaFin");
            String tipo = request.getParameter("tipo");

            List<Movimiento> movimientos;
            
            // Si hay algún filtro aplicado
            if (cuentaId != null || categoriaId != null || fechaInicio != null || 
                fechaFin != null || (tipo != null && !tipo.isEmpty())) {
                movimientos = movimientoDAO.findWithFilters(
                    usuario, cuentaId, categoriaId, fechaInicio, fechaFin, tipo);
            } else {
                movimientos = movimientoDAO.findByUsuario(usuario);
            }

            Map<String, Double> totales = calcularTotales(movimientos);

            request.setAttribute("categorias", categorias);
            request.setAttribute("cuentas", cuentas);
            request.setAttribute("movimientos", movimientos);
            request.setAttribute("totales", totales);

        } catch (Exception e) {
            request.setAttribute("mensaje", "Error al cargar los movimientos: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/jsp/verMovimientos.jsp").forward(request, response);
    }

    private void filtrarMovimientos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            Integer cuentaId = getIntParameter(request, "cuenta");
            Integer categoriaId = getIntParameter(request, "categoria");
            Date fechaInicio = getFechaParameter(request, "fechaInicio");
            Date fechaFin = getFechaParameter(request, "fechaFin");
            String tipo = request.getParameter("tipo");

            List<Movimiento> movimientos = movimientoDAO.findWithFilters(
                    usuario, cuentaId, categoriaId, fechaInicio, fechaFin, tipo);
            List<Categoria> categorias = categoriaDAO.listarPorUsuario(usuario);
            List<Cuenta> cuentas = cuentaDAO.findByUsuario(usuario);
            Map<String, Double> totales = calcularTotales(movimientos);

            request.setAttribute("categorias", categorias);
            request.setAttribute("cuentas", cuentas);
            request.setAttribute("movimientos", movimientos);
            request.setAttribute("totales", totales);
            
            // Mantener los filtros en la vista
            request.setAttribute("filtroAplicado", true);
            request.setAttribute("cuentaSeleccionada", cuentaId);
            request.setAttribute("categoriaSeleccionada", categoriaId);
            request.setAttribute("fechaInicio", request.getParameter("fechaInicio"));
            request.setAttribute("fechaFin", request.getParameter("fechaFin"));
            request.setAttribute("tipoSeleccionado", tipo);

        } catch (Exception e) {
            request.setAttribute("mensaje", "Error al aplicar los filtros: " + e.getMessage());
        }

        request.getRequestDispatcher("/jsp/verMovimientos.jsp").forward(request, response);
    }

    private Integer getIntParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        return (value != null && !value.isEmpty()) ? Integer.parseInt(value) : null;
    }

    private Date getFechaParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    private Map<String, Double> calcularTotales(List<Movimiento> movimientos) {
        Map<String, Double> totales = new HashMap<>();
        double totalIngresos = 0;
        double totalEgresos = 0;
        double totalTransferencias = 0;

        for (Movimiento movimiento : movimientos) {
            switch (movimiento.getClass().getSimpleName()) {
                case "Ingreso":
                    totalIngresos += movimiento.getValor().doubleValue();
                    break;
                case "Egreso":
                    totalEgresos += movimiento.getValor().doubleValue();
                    break;
                case "Transferencia":
                    totalTransferencias += movimiento.getValor().doubleValue();
                    break;
            }
        }

        totales.put("ingresos", totalIngresos);
        totales.put("egresos", totalEgresos);
        totales.put("transferencias", totalTransferencias);
        totales.put("balance", totalIngresos - totalEgresos);

        return totales;
    }
}