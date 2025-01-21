<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Realizar Movimiento</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/styles/styles.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/styles/movimiento.css">
    <script src="<%= request.getContextPath() %>/jsp/js/realizarMovimiento.js"></script>
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
            <img src=""<%= request.getContextPath() %>/jspimages/wallet-512px.png" alt="wallet">
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
                    <form action="" method="get">
                        <input type="hidden" name="ruta" value="">
                        <button class="menu-button pd-8" type="submit">
                            <i class="fa-solid fa-eye text-xl"></i> Movimientos
                        </button>
                    </form>
                </li>
            </ul>
        </nav>
        <div class="container">
            <div class="content-wrapper">
                <div class="ds-flex-column">
                    <!-- Botones de las pestañas -->
                    <div class="tabs ds-flex">
                        <button class="tab-button active" data-target="tab1">Ingreso</button>
                        <button class="tab-button" data-target="tab2">Egreso</button>
                        <button class="tab-button" data-target="tab3">Transferencia</button>
                    </div>

                    <!-- Contenido de las pestañas -->
                    <div class="pd-24">
                        <!-- Formulario de Ingreso -->
                        <div id="tab1" class="tab-pane bg-white">
                            <form id="ingreso-form" data-balance="${cuenta.balance}" class="font-primary"
                                action="<%= request.getContextPath() %>/realizarMovimientoController" method="post">
                                <input type="hidden" name="ruta" value="realizarIngreso">
                                <input type="hidden" name="cuentaId" value="${cuenta.id}">
                                <div class="form-group pd-y-8">
                                    <label for="valor-ingreso">Valor:</label>
                                    <input type="number" id="valor-ingreso" name="valor" class="input text-base"
                                        placeholder="Ingrese el valor" required>
                                </div>
                                <div class="form-group pd-y-8">
                                    <label for="concepto-ingreso">Concepto:</label>
                                    <input id="concepto-ingreso" name="concepto" class="input text-base"
                                        placeholder="Concepto del ingreso">
                                </div>
                                <div class="form-group pd-y-8">
                                    <label for="categoria-ingreso">Categoria:</label>
                                    <select id="categoria-ingreso" name="categoriaId" class="input text-base">
                                        <option value="">Seleccione una categoria</option>
                                        <c:forEach var="categoria" items="${categorias}">
                                            <option value="${categoria.id}">${categoria.nombreCategoria}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="pd-y-8">
                                    <button type="submit" class="button bg-primary text-white">Registrar
                                        Ingreso</button>
                                </div>
                            </form>
                        </div>

                        <!-- Formulario de Egreso -->
                        <div id="tab2" class="tab-pane hidden bg-white">
                            <form id="egreso-form" data-balance="${cuenta.balance}" class="font-primary"
                                  action="<%= request.getContextPath() %>/realizarMovimientoController" method="post">
                                <input type="hidden" name="ruta" value="realizarEgreso">
                                <input type="hidden" name="cuentaId" value="${cuenta.id}">
                                <div class="form-group pd-y-8">
                                    <label for="valor-egreso">Valor:</label>
                                    <input type="number" id="valor-egreso" name="valor" class="input text-base"
                                        placeholder="Ingrese el valor" required>
                                </div>
                                <div class="form-group pd-y-8">
                                    <label for="concepto-egreso">Concepto:</label>
                                    <input id="concepto-egreso" name="concepto" class="input text-base"
                                        placeholder="Concepto del egreso">
                                </div>
                                <div class="form-group pd-y-8">
                                    <label for="categoria-egreso">Categoria:</label>
                                    <select id="categoria-egreso" name="categoriaId" class="input text-base">
                                        <option value="">Seleccione una categoria</option>
                                        <c:forEach var="categoria" items="${categorias}">
                                            <option value="${categoria.id}">${categoria.nombreCategoria}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="pd-y-8">
                                    <button type="submit" class="button bg-primary text-white">Registrar
                                        Egreso</button>
                                </div>
                            </form>
                        </div>

                        <!-- Formulario de Transferencia -->
                        <div id="tab3" class="tab-pane hidden bg-white">
                            <form id="transferencia-form" data-balance="${cuenta.balance}" class="font-primary"
                                  action="<%= request.getContextPath() %>/realizarMovimientoController" method="post">
                                <input type="hidden" name="ruta" value="realizarTransferencia">
                                <input type="hidden" name="cuentaId" value="${cuenta.id}">
                                <div class="form-group pd-y-8">
                                    <label for="valor-transferencia">Valor:</label>
                                    <input type="number" id="valor-transferencia" name="valor"
                                        class="input text-base" placeholder="Ingrese el valor" required>
                                </div>
                                <div class="form-group pd-y-8">
                                    <label for="cuenta-destino">Cuenta Destino:</label>
                                    <select id="cuenta-destino" name="cuentaDestinoId" class="input text-base"
                                        required>
                                        <option value="">Seleccione una cuenta</option>
                                        <c:forEach var="cuenta" items="${cuentas}">
                                            <option value="${cuenta.id}" data-balance="${cuenta.balance}">
                                                ${cuenta.nombre}</option>
                                        </c:forEach>
                                    </select>
                                    <span class="text-sm text-light pd-t-8">Balance de la cuenta destino: <span
                                            id="balance-cuenta-destino">N/A</span></span>
                                </div>
                                <div class="form-group pd-y-8">
                                    <label for="concepto-transferencia">Concepto:</label>
                                    <input id="concepto-transferencia" name="concepto" class="input text-base"
                                        placeholder="Concepto de la transferencia">
                                </div>
                                <div class="form-group pd-y-8">
                                    <label for="categoria-transferencia">Categoria:</label>
                                    <select id="categoria-transferencia" name="categoriaId" class="input text-base">
                                        <option value="">Seleccione una categoria</option>
                                        <c:forEach var="categoria" items="${categorias}">
                                            <option value="${categoria.id}">${categoria.nombreCategoria}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="pd-y-8">
                                    <button type="submit" class="button bg-primary text-white">Realizar
                                        Transferencia
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="account-info">
                    <h2 class="text-dark font-primary">Cuenta: ${cuenta.nombre}</h2>
                    <p class="text-dark font-primary">Balance: ${cuenta.balance}</p>
                </div>
            </div>
        </div>
    </main>
    <script>
        document.getElementById('cuenta-destino').addEventListener('change', function () {
            const selectedOption = this.options[this.selectedIndex];
            const balance = selectedOption.getAttribute('data-balance');
            const balanceDisplay = document.getElementById('balance-cuenta-destino');
            balanceDisplay.textContent = balance ? balance : 'N/A';
        });
    </script>
</body>
</html>