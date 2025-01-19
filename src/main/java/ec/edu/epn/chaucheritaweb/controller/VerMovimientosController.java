package ec.edu.epn.chaucheritaweb.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ec.edu.epn.chaucheritaweb.model.dao.CategoriaDAO;
import ec.edu.epn.chaucheritaweb.model.dao.MovimientoDAO;
import ec.edu.epn.chaucheritaweb.model.entities.Categoria;
import ec.edu.epn.chaucheritaweb.model.entities.Movimiento;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;
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

    @Override
    public void init() throws ServletException {
        movimientoDAO = new MovimientoDAO();
        categoriaDAO = new CategoriaDAO();
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

        List<Categoria> categorias = categoriaDAO.listarPorUsuario(usuario);
        List<Movimiento> movimientos = movimientoDAO.findByUsuario(usuario);
        
        request.setAttribute("categorias", categorias);
        request.setAttribute("movimientos", movimientos);
        request.getRequestDispatcher("/jsp/verMovimientos.jsp").forward(request, response);
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
            Integer cuentaId = getIntParameter(request, "cuenta");
            Integer categoriaId = getIntParameter(request, "categoria");
            Date fechaInicio = getFechaParameter(request, "fechaInicio");
            Date fechaFin = getFechaParameter(request, "fechaFin");
            String tipo = request.getParameter("tipo");
            
            List<Movimiento> movimientos = movimientoDAO.findWithFilters(
                usuario, cuentaId, categoriaId, fechaInicio, fechaFin, tipo
            );
            List<Categoria> categorias = categoriaDAO.listarPorUsuario(usuario);
            
            request.setAttribute("categorias", categorias);
            request.setAttribute("movimientos", movimientos);
            request.setAttribute("filtroAplicado", true);
            request.setAttribute("filtros", Map.of(
                "cuenta", cuentaId,
                "categoria", categoriaId,
                "fechaInicio", fechaInicio,
                "fechaFin", fechaFin,
                "tipo", tipo
            ));
            
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
}
