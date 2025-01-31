<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/styles/styles.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/styles/home.css">
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/jsp/images/dollar.png">
    <script src="https://kit.fontawesome.com/d2aae01839.js" crossorigin="anonymous"></script>
</head>

<body>
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
    <main class="ds-flex jc-sb">
        <nav class="sidemenu bg-light">
            <img src="<%= request.getContextPath() %>/jsp/images/wallet-512px.png" alt="wallet">
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
                    <form action="<%= request.getContextPath() %>/jsp/gestionarCuenta.jsp" method="get">
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
        <div class="main-content">
            <section class="font-primary pd-t-40 pd-x-40">
                <h2 class="font-primary text-dark">Mis Cuentas</h2>
                <!-- Campo de entrada para el filtro -->
                <div class="form-group pd-y-16 pd-x-24 bg-light border-light">
                    <label for="filtro-cuentas" class="font-primary text-dark text-base pd-b-8">Filtrar por
                        nombre:</label>
                    <input type="text" id="filtro-cuentas" class="pd-8 border-light text-base text-dark font-primary"
                        placeholder="Buscar cuenta...">
                </div>
                <div>
                    <table id="tabla-cuentas" class="table border-light mg-y-16">
                        <thead>
                            <tr class="bg-light text-dark">
                                <th class="pd-8">ID</th>
                                <th class="pd-8">Nombre
                                    <button id="ordenar-cuentas" class="button bg-primary text-white pd-8 mg-t-8">
                                        .
                                    </button>
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
                                    <td class="pd-8">
                                        <form action="<%= request.getContextPath() %>/realizarMovimientoController"
                                            method="get">
                                            <input type="hidden" name="ruta" value="realizarMovimiento">
                                            <input type="hidden" name="cuentaId" value="${cuenta.id}">
                                            <button type="submit" class="button bg-primary text-white">Realizar
                                                Movimiento
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </section>
        </div>
    </main>
</body>

</html>