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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Cargar datos necesarios
            List<Categoria> categorias = categoriaDAO.listarPorUsuario(usuario);
            List<Cuenta> cuentas = cuentaDAO.findByUsuario(usuario);
            List<Movimiento> movimientos = movimientoDAO.findByUsuario(usuario);
            
            // Calcular totales
            Map<String, Double> totales = calcularTotales(movimientos);
            
            // Establecer atributos en el request
            request.setAttribute("categorias", categorias);
            request.setAttribute("cuentas", cuentas);
            request.setAttribute("movimientos", movimientos);
            request.setAttribute("totales", totales);
            
            // Redirigir a la vista
            request.getRequestDispatcher("/jsp/verMovimientos.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar los movimientos: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Obtener parámetros de filtro
            Integer cuentaId = getIntParameter(request, "cuenta");
            Integer categoriaId = getIntParameter(request, "categoria");
            Date fechaInicio = getFechaParameter(request, "fechaInicio");
            Date fechaFin = getFechaParameter(request, "fechaFin");
            String tipo = request.getParameter("tipo");
            
            // Aplicar filtros
            List<Movimiento> movimientos = movimientoDAO.findWithFilters(
                usuario, cuentaId, categoriaId, fechaInicio, fechaFin, tipo
            );
            
            // Cargar datos complementarios
            List<Categoria> categorias = categoriaDAO.listarPorUsuario(usuario);
            List<Cuenta> cuentas = cuentaDAO.findByUsuario(usuario);
            Map<String, Double> totales = calcularTotales(movimientos);
            
            // Establecer atributos
            request.setAttribute("categorias", categorias);
            request.setAttribute("cuentas", cuentas);
            request.setAttribute("movimientos", movimientos);
            request.setAttribute("totales", totales);
            request.setAttribute("filtroAplicado", true);
            
            // Mantener los filtros seleccionados
            Map<String, Object> filtros = new HashMap<>();
            filtros.put("cuenta", cuentaId);
            filtros.put("categoria", categoriaId);
            filtros.put("fechaInicio", fechaInicio);
            filtros.put("fechaFin", fechaFin);
            filtros.put("tipo", tipo);
            request.setAttribute("filtros", filtros);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error al aplicar los filtros: " + e.getMessage());
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