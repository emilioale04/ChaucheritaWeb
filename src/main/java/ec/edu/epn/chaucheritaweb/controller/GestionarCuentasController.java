package Controlador;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Cuenta;

@WebServlet("/GestionarCuentasController")
public class GestionarCuentasController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.ruteador(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.ruteador(req, resp);
	}
	
	
	private void ruteador(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ruta = req.getParameter("ruta") != null ? req.getParameter("ruta") : "inicio";

        switch (ruta) {
            case "crearCuenta":
                this.crearCuenta(req, resp);
                break;

            case "guardarNueva":
                this.guardarNueva(req, resp);
                break;

            case "eliminarCuenta":
                this.eliminarCuenta(req, resp);
                break;
                
            case "listarCuentas":
                this.listarCuentas(req, resp);
                break;

            case "inicio": 
            default:
                this.iniciar(req, resp);
                break;
        }
    
			
				
		
	}
	
	
	private void iniciar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("jsp/gestorCuentas.jsp").forward(req, resp);
    }
	
	
	
	private void listarCuentas(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Cuenta> cuentas =Cuenta.getCuentas();
		
		
		req.setAttribute("cuentas", cuentas);
		req.getRequestDispatcher("jsp/gestorCuentas.jsp").forward(req, resp);
		

	}
	
	

	private void crearCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.sendRedirect("jsp/crearCuenta.jsp");
	
	}

	private void guardarNueva(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
			  
		        int id = Integer.parseInt(req.getParameter("idCuenta")); 
		        String nombre = req.getParameter("nombreCuenta"); 
		        double monto = Double.parseDouble(req.getParameter("monto")); 
		        String categoria = req.getParameter("categoriaCuenta"); 

		        Cuenta cuenta = new Cuenta(id , nombre, monto, categoria); 
		        
		      
		        
		        boolean resultado= Cuenta.create(cuenta);
		        
		    
		        
		        if(resultado ) {
		        	listarCuentas(req,resp);	
		        }else {
		        	req.setAttribute("mensaje", "No se pudo registrar la cuenta nueva ");
		        	req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
		        }
		        
		        
		        
		        
		       
		    
	}

	private void eliminarCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String idCuentaStr = req.getParameter("idCuenta");

	    if (idCuentaStr == null) {
	        
	        req.getRequestDispatcher("jsp/eliminarCuenta.jsp").forward(req, resp);
	        return;
	    }

	    try {
	        int idCuenta = Integer.parseInt(idCuentaStr);

	      
	        boolean resultado = Cuenta.delete(idCuenta);

	        if (resultado) {
	            req.setAttribute("mensaje", "La cuenta fue eliminada correctamente.");
	        } else {
	            req.setAttribute("mensaje", "Error: No se pudo eliminar la cuenta. Verifique el ID.");
	        }

	        listarCuentas(req, resp);
	    } catch (NumberFormatException e) {
	        req.setAttribute("mensaje", "Error: El ID de la cuenta debe ser un número.");
	        req.getRequestDispatcher("jsp/eliminarCuenta.jsp").forward(req, resp);
	    }
	}

}
