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
import jakarta.servlet.http.HttpSession;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;
import ec.edu.epn.chaucheritaweb.model.dao.CuentaDAO;






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
			req.setAttribute("mensaje", "Debe iniciar sesión para ver sus cuentas.");
			req.getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
			return;
		}

		CuentaDAO cuentaDAO = new CuentaDAO();

		try {
			List<Cuenta> cuentas = cuentaDAO.listarCuentas(usuario);

			req.setAttribute("cuentas", cuentas);
			req.getRequestDispatcher("/jsp/listarCuentas.jsp").forward(req, resp);
		} catch (Exception e) {
			req.setAttribute("mensaje", "Error al listar cuentas: " + e.getMessage());
			req.getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
		}
	}


	private void guardarNueva(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		if (usuario != null) {
			String nombre = req.getParameter("nombre");
			BigDecimal balance = new BigDecimal(req.getParameter("balance"));
			CuentaDAO cuentaDAO = new CuentaDAO();
			try {
				cuentaDAO.create(usuario, nombre, balance);
				req.setAttribute("mensaje", "La cuenta fue guardada correctamente.");
			} catch (Exception e) {
				req.setAttribute("mensaje", "Error: No se pudo guardar la cuenta. " + e.getMessage());
			}

			listarCuentas(req, resp);
		} else {
			req.setAttribute("mensaje", "Error: Usuario no autenticado.");
			req.getRequestDispatcher("jsp/login.jsp").forward(req, resp);
		}
	}




	private void eliminarCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idCuentaStr = req.getParameter("cuentaId");

		if (idCuentaStr == null || idCuentaStr.isEmpty()) {
			req.setAttribute("mensaje", "Error: ID de cuenta no especificado.");
			req.getRequestDispatcher("jsp/gestionarCuenta.jsp").forward(req, resp);
			return;
		}

		try {
			int idCuenta = Integer.parseInt(idCuentaStr);

			CuentaDAO cuentaDAO = new CuentaDAO();

			boolean cuentaEliminada = cuentaDAO.delete(idCuenta);

			if (cuentaEliminada) {
				req.setAttribute("mensaje", "La cuenta fue eliminada correctamente.");
			} else {
				req.setAttribute("mensaje", "Error: No se encontró una cuenta con ese ID.");
			}

			listarCuentas(req, resp);

		} catch (NumberFormatException e) {
			req.setAttribute("mensaje", "Error: El ID de la cuenta debe ser un número válido.");
			req.getRequestDispatcher("jsp/gestionarCuenta.jsp").forward(req, resp);
		} catch (Exception e) {
			req.setAttribute("mensaje", "Error: " + e.getMessage());
			req.getRequestDispatcher("jsp/gestionarCuenta.jsp").forward(req, resp);
		}
	}





	private void actualizarCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cuentaIdStr = req.getParameter("cuentaId");
		String nombre = req.getParameter("nombre");
		String balanceStr = req.getParameter("balance");

		if (cuentaIdStr != null && !cuentaIdStr.isEmpty() && nombre != null && balanceStr != null) {
			try {

				Long cuentaId = Long.parseLong(cuentaIdStr);
				BigDecimal balance = new BigDecimal(balanceStr);
				CuentaDAO cuentaDAO = new CuentaDAO();
				cuentaDAO.update(cuentaId, nombre, balance);

				resp.sendRedirect(req.getContextPath() + "/GestionarCuentasController?ruta=listarCuentas");
			} catch (Exception e) {
				req.setAttribute("mensaje", "Error: " + e.getMessage());
				req.getRequestDispatcher("jsp/gestionarCuenta.jsp").forward(req, resp);
			}
		}
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