package ec.edu.epn.chaucheritaweb.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import ec.edu.epn.chaucheritaweb.model.dao.CuentaDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ec.edu.epn.chaucheritaweb.model.entities.Cuenta;
import jakarta.servlet.http.HttpSession;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;



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
		String ruta = (req.getParameter("ruta") == null) ? "listarCuentas" : req.getParameter("ruta");

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
			case "cargarFormularioEdicion":
				this.cargarFormularioEdicion(req, resp);
				break;
			case "actualizarCuenta":
			default:
				this.actualizarCuenta(req, resp);
				break;
		}
	}


	private void listarCuentas(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");

		if (usuario == null) {
			resp.sendRedirect("/jsp/login.jsp");
			return;
		}
		CuentaDAO cuentaDAO = new CuentaDAO();
		List<Cuenta> cuentas = cuentaDAO.findByUsuario(usuario);

		req.setAttribute("cuentas", cuentas);
		req.getRequestDispatcher("jsp/gestionarCuenta.jsp").forward(req, resp);
	}

	private void crearCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("jsp/gestionarCuenta.jsp");
	}

	private void guardarNueva(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		if (usuario != null) {
			String nombre = req.getParameter("nombre");
			BigDecimal balance = new BigDecimal(req.getParameter("balance"));

			EntityManager em = emf.createEntityManager();
			try {
				em.getTransaction().begin();

				// Asegurar que el usuario está gestionado por JPA
				usuario = em.find(Usuario.class, usuario.getId());

				// Crear la nueva cuenta
				Cuenta cuenta = new Cuenta();
				cuenta.setNombre(nombre);
				cuenta.setBalance(balance);
				cuenta.setUsuario(usuario);  // Asignar el usuario a la cuenta

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
		} else {
			req.setAttribute("mensaje", "Error: Usuario no autenticado.");
			req.getRequestDispatcher("jsp/login.jsp").forward(req, resp);
		}
	}




	private void eliminarCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idCuentaStr = req.getParameter("cuentaId");

		if (idCuentaStr == null) {
			req.setAttribute("mensaje", "Error: ID de cuenta no especificado.");
			req.getRequestDispatcher("jsp/gestionarCuenta.jsp").forward(req, resp);
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

			// Redirigir para listar las cuentas actualizadas después de eliminar
			listarCuentas(req, resp);
		} catch (NumberFormatException e) {
			req.setAttribute("mensaje", "Error: El ID de la cuenta debe ser un número.");
			req.getRequestDispatcher("jsp/gestionarCuenta.jsp").forward(req, resp);
		}
	}





	private void actualizarCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cuentaIdStr = req.getParameter("cuentaId");
		String nombre = req.getParameter("nombre");
		String balanceStr = req.getParameter("balance");

		if (cuentaIdStr != null && !cuentaIdStr.isEmpty() && nombre != null && balanceStr != null) {
			EntityManager em = emf.createEntityManager();
			try {
				em.getTransaction().begin();
				int cuentaId = Integer.parseInt(cuentaIdStr);
				BigDecimal balance = new BigDecimal(balanceStr);

				Cuenta cuenta = em.find(Cuenta.class, cuentaId);
				if (cuenta != null) {
					cuenta.setNombre(nombre);
					cuenta.setBalance(balance);
					em.merge(cuenta);
				}
				em.getTransaction().commit();
			} finally {
				em.close();
			}
		}

		// Redirigir a la vista de listado de cuentas
		resp.sendRedirect(req.getContextPath() + "/GestionarCuentasController?ruta=listarCuentas");
	}





	private void cargarFormularioEdicion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cuentaIdStr = req.getParameter("cuentaId");
		if (cuentaIdStr != null && !cuentaIdStr.isEmpty()) {
			EntityManager em = emf.createEntityManager();
			try {
				int cuentaId = Integer.parseInt(cuentaIdStr);
				Cuenta cuenta = em.find(Cuenta.class, cuentaId);
				req.setAttribute("cuentaSeleccionada", cuenta);
			} finally {
				em.close();
			}
		}
		req.getRequestDispatcher("/jsp/gestionarCuenta.jsp").forward(req, resp);
	}









}