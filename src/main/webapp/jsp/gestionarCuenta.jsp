<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Cuenta</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/styles/styles.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/styles/home.css">
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/jsp/images/dollar.png">
    <script src="https://kit.fontawesome.com/d2aae01839.js" crossorigin="anonymous"></script>
    <script src="https://kit.fontawesome.com/d2aae01839.js" crossorigin="anonymous"></script>
</head>

<body>
    <!--
        <div id="toast-container"></div>
        <c:if test="${not empty mensaje}">
            <div id="alerta-overlay">
                <div id="alerta-box">
                    <p>${mensaje}</p>
                    <button>OK</button>
                </div>
            </div>
        </c:if>
        -->
    <header class="header bg-primary ds-flex jc-sb pd-y-16 pd-x-24 font-secondary text-white align-center">
        <div></div>
        <div>
            <form action="<%= request.getContextPath() %>/menuController" method="post">
                <input type="hidden" name="ruta" value="logout">
                <button type="submit" class="logout-button">
                    <span class="pd-r-8 font-bold font-primary text-base">Cerrar Sesión</span>
                    <i class="fa-solid fa-right-from-bracket text-white text-xl"></i>
                </button>
            </form>
        </div>
    </header>
    <main class="ds-flex">
        <nav class="sidemenu bg-light">
            <img src="images/wallet-512px.png" alt="wallet">
            <span class="text-dark font-primary text-center pd-b-16">Chaucherita<br>Web</span>
            <ul class="menu ls-none">
                <li>
                                    <form action="<%= request.getContextPath() %>/menuController" method="get">
                                        <input type="hidden" name="ruta" value="home">
                                        <button class="menu-button pd-8" type="submit">
                                            <i class="fa-solid fa-house text-xl"></i> Inicio
                                        </button>
                                    </form>
                                </li>
                                <li>
                                    <form action="<%= request.getContextPath() %>/GestionarCuentasController" method="get">
                                        <input type="hidden" name="ruta" value="">
                                        <button class="menu-button pd-8" type="submit">
                                            <i class="fa-solid fa-gear text-xl"></i> Cuentas
                                        </button>
                                    </form>
                                </li>
                                <li>
                                    <form action="<%= request.getContextPath() %>/GestionarCategoria" method="get">
                                        <button class="menu-button pd-8" type="submit">
                                            <i class="fa-solid fa-tag text-xl"></i> Categorías
                                        </button>
                                    </form>
                                </li>
                                <li>
                                    <form action="<%= request.getContextPath() %>/verMovimientos" method="get">
                                        <button class="menu-button pd-8" type="submit">
                                            <i class="fa-solid fa-eye text-xl"></i> Movimientos
                                        </button>
                                    </form>
                                </li>
            </ul>
        </nav>
        <section class="ds-flex-column flex-1">
            <div class="ds-flex pd-t-40 pd-x-40">
                <div class="flex-1">
                    <h2 class="font-primary text-dark">Crear Nueva Cuenta</h2>

                    <form id="crear-cuenta-form" class="font-primary" action="<%= request.getContextPath() %>/GestionarCuentasController" method="post">
                        <input type="hidden" name="ruta" value="${cuentaSeleccionada != null ? 'actualizarCuenta' : 'guardarNueva'}">
                        <input type="hidden" name="usuario_id" value="${usuario.id}">
                        <input type="hidden" name="cuentaId" value="${cuentaSeleccionada.id}"> <!-- Para saber si es edición -->
                        <div class="ds-flex">
                            <div class="flex-1 form-group pd-y-16 pd-r-24">
                                <label for="nombre">Nombre de la Cuenta:</label>
                                <input type="text" id="nombre" name="nombre" class="input text-base"
                                       placeholder="Ingrese el nombre de la cuenta" value="${cuentaSeleccionada.nombre}" required>
                            </div>
                            <div class="flex-1 form-group pd-y-16 pd-l-24">
                                <label for="balance">Balance Inicial:</label>
                                <input type="number" id="balance" name="balance" class="input text-base"
                                       placeholder="Ingrese el balance inicial" value="${cuentaSeleccionada.balance}" required>
                            </div>
                        </div>
                        <div>
                            <button type="submit" class="button bg-primary text-white">
                                ${cuentaSeleccionada != null ? 'Actualizar Cuenta' : 'Crear Cuenta'}
                            </button>
                        </div>
                    </form>

                </div>
            </div>

            <div class="ds-flex pd-t-40 pd-x-40">
                <div class="flex-1">
                    <h2 class="font-primary text-dark">Mis Cuentas</h2>
                    <table id="tabla-cuentas" class="table border-light">
                        <thead>
                            <tr class="bg-light text-dark">
                                <th class="pd-8">ID</th>
                                <th class="pd-8">Nombre

                                </th>
                                <th class="pd-8">Balance Actual</th>
                                <th class="pd-8">Acciones</th>
                            </tr>
                        </thead>
                        <tbody id="cuerpo-tabla-cuentas">
                            <c:forEach var="cuenta" items="${cuentas}">
                                <tr>
                                    <td class="pd-8">${cuenta.id}</td>
                                    <td class="pd-8">${cuenta.nombre}</td>
                                    <td class="pd-8">${cuenta.balance}</td>
                                    <td class="pd-8 ds-flex jc-sa">
                                        <form action="<%= request.getContextPath() %>/GestionarCuentasController" method="post">
                                            <input type="hidden" name="ruta" value="eliminarCuenta">
                                            <input type="hidden" name="cuentaId" value="${cuenta.id}">
                                            <button type="submit" class="button bg-primary text-white"> <i
                                                    class="fa-solid fa-trash text-xl"></i>
                                            </button>
                                        </form>

                                        <form action="<%= request.getContextPath() %>/GestionarCuentasController" method="get">
                                            <input type="hidden" name="ruta" value="cargarFormularioEdicion">
                                            <input type="hidden" name="cuentaId" value="${cuenta.id}">
                                            <button type="submit" class="button bg-primary text-white">
                                                <i class="fa-solid fa-pencil text-xl"></i>
                                            </button>
                                        </form>

                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </main>
</body>

</html>