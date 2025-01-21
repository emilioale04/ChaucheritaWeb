<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Movimientos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/styles/styles.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/styles/verMovimiento.css">
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/jsp/images/dollar.png">
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
            <form action="" method="post">
                <input type="hidden" name="ruta" value="">
                <button type="submit" class="logout-button">
                    <span class="pd-r-8 font-bold font-primary text-base">Cerrar Sesión</span>
                    <i class="fa-solid fa-right-from-bracket text-white text-xl"></i>
                </button>
            </form>
        </div>
    </header>
    <main class="ds-flex jc-sb">
        <nav class="sidemenu bg-light">
            <img src=" <%= request.getContextPath() %>/jsp/images/wallet-512px.png" alt="wallet">
            <span class="text-dark font-primary text-center pd-b-16">Chaucherita<br>Web</span>
            <ul class="menu ls-none">
                <li>
                    <form action="" method="get">
                        <input type="hidden" name="ruta" value="">
                        <button class="menu-button pd-8" type="submit">
                            <i class="fa-solid fa-house text-xl"></i> Inicio
                        </button>
                    </form>
                </li>
                <li>
                    <form action="" method="get">
                        <input type="hidden" name="ruta" value="">
                        <button class="menu-button pd-8" type="submit">
                            <i class="fa-solid fa-gear text-xl"></i> Cuentas
                        </button>
                    </form>
                </li>
                <li>
                    <form action="" method="get">
                        <input type="hidden" name="ruta" value="">
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
        <div class="pd-x-40 pd-t-40 flex-2">
            <h2 class="font-primary text-dark">Mis Movimientos</h2>

            <!-- Formulario de filtros -->
            <form method="post" action="verMovimientos" class="form-group mg-y-16">
                <div class="ds-flex gap-16">
                    <select name="cuenta" class="input">
                        <option value="">Todas las cuentas</option>
                        <c:forEach var="cuenta" items="${cuentas}">
                            <option value="${cuenta.id}" ${param.cuenta==cuenta.id ? 'selected' : '' }>
                                ${cuenta.nombre}
                            </option>
                        </c:forEach>
                    </select>

                    <select name="categoria" class="input">
                        <option value="">Todas las categorías</option>
                        <c:forEach var="categoria" items="${categorias}">
                            <option value="${categoria.id}" ${param.categoria==categoria.id ? 'selected' : '' }>
                                ${categoria.nombre}
                            </option>
                        </c:forEach>
                    </select>

                    <select name="tipo" class="input">
                        <option value="">Todos los tipos</option>
                        <option value="INGRESO" ${param.tipo=='INGRESO' ? 'selected' : '' }>Ingresos</option>
                        <option value="EGRESO" ${param.tipo=='EGRESO' ? 'selected' : '' }>Egresos</option>
                        <option value="TRANSFERENCIA" ${param.tipo=='TRANSFERENCIA' ? 'selected' : '' }>Transferencias
                        </option>
                    </select>

                    <input type="date" name="fechaInicio" class="input" value="${param.fechaInicio}">
                    <input type="date" name="fechaFin" class="input" value="${param.fechaFin}">

                    <button type="submit" class="button">Filtrar</button>
                    <a href="verMovimientos" class="button bg-light text-dark">Limpiar</a>
                </div>
            </form>

            <!-- Tabla de movimientos -->
            <div class="font-primary">
                <table id="movimientosTable" class="table border-light mg-y-16">
                    <tr>
                        <th>Fecha</th>
                        <th>Concepto</th>
                        <th>Categoría</th>
                        <th>Tipo</th>
                        <th>Valor</th>
                    </tr>
                    <c:forEach var="movimiento" items="${movimientos}">
                        <tr class="${movimiento['class'].simpleName == 'Ingreso' ? 'row-ingreso' : 'row-egreso'}">
                            <td>
                                <fmt:formatDate value="${movimiento.fecha}" pattern="dd/MM/yyyy" />
                            </td>
                            <td>${movimiento.concepto}</td>
                            <td>${movimiento.categoria.nombre}</td>
                            <td>${movimiento['class'].simpleName}</td>
                            <td>
                                <fmt:formatNumber value="${movimiento.valor}" type="currency" />
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </main>
</body>

</html>