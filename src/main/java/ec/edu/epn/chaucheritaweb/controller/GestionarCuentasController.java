package ec.edu.epn.chaucheritaweb.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ec.edu.epn.chaucheritaweb.model.entities.Cuenta;

@WebServlet("/GestionarCuentasController")
public class GestionarCuentasController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private EntityManagerFactory emf;

	@Override
	public void init() throws ServletException {
		emf = Persistence.createEntityManagerFactory("ChaucheritaWeb");
	}

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
		req.getRequestDispatcher("jsp/gestionarCuenta.jsp").forward(req, resp);
	}

	private void listarCuentas(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		EntityManager em = emf.createEntityManager();
		List<Cuenta> cuentas = em.createQuery("SELECT c FROM Cuenta c", Cuenta.class).getResultList();
		em.close();

		req.setAttribute("cuentas", cuentas);
		req.getRequestDispatcher("jsp/gestionarCuenta.jsp").forward(req, resp);
	}

	private void crearCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("jsp/gestionarCuenta.jsp");
	}

private void guardarNueva(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String nombre = req.getParameter("nombre");
    BigDecimal balance = new BigDecimal(req.getParameter("balance"));

    Cuenta cuenta = new Cuenta();
    cuenta.setNombre(nombre);
    cuenta.setBalance(balance);

    EntityManager em = emf.createEntityManager();
    try {
        em.getTransaction().begin();
        em.persist(cuenta);
        em.getTransaction().commit();
        req.setAttribute("mensaje", "La cuenta fue guardada correctamente.");
    } catch (Exception e) {
        em.getTransaction().rollback();
        req.setAttribute("mensaje", "Error: No se pudo guardar la cuenta. " + e.getMessage());
    } finally {
        em.close();
    }

    listarCuentas(req, resp);
}




	private void eliminarCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idCuentaStr = req.getParameter("cuentaId");

		if (idCuentaStr == null) {
			req.getRequestDispatcher("jsp/eliminarCuenta.jsp").forward(req, resp);
			return;
		}

		try {
			int idCuenta = Integer.parseInt(idCuentaStr);

			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			Cuenta cuenta = em.find(Cuenta.class, idCuenta);
			if (cuenta != null) {
				em.remove(cuenta);
				em.getTransaction().commit();
				req.setAttribute("mensaje", "La cuenta fue eliminada correctamente.");
			} else {
				req.setAttribute("mensaje", "Error: No se pudo eliminar la cuenta. Verifique el ID.");
			}
			em.close();

			listarCuentas(req, resp);
		} catch (NumberFormatException e) {
			req.setAttribute("mensaje", "Error: El ID de la cuenta debe ser un número.");
			req.getRequestDispatcher("jsp/eliminarCuenta.jsp").forward(req, resp);
		}
		}
}