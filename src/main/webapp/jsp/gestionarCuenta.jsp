<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Cuenta</title>
    <link rel="stylesheet" href="styles/styles.css">
    <link rel="stylesheet" href="styles/movimiento.css">
    <link rel="icon" type="image/png" href="images/dollar.png">
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
        <img src="images/wallet-512px.png" alt="wallet">
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
    <div class="ds-flex pd-t-40 pd-x-40 flex-1">
        <div class="flex-1 pd-r-40 mg-r-24">
            <h2 class="font-primary text-dark">Crear Nueva Cuenta</h2>
            
            <form id="crear-cuenta-form" class="font-primary" action="" method="post">
                <input type="hidden" name="ruta" value="">
                <div class="ds-flex">
                    <div class="flex-1 form-group pd-y-16 pd-r-24">
                        <label for="nombre">Nombre de la Cuenta:</label>
                        <input type="text" id="nombre" name="nombre" class="input text-base"
                        placeholder="Ingrese el nombre de la cuenta" required>
                    </div>
                    <div class="flex-1 form-group pd-y-16 pd-l-24">
                        <label for="balance">Balance Inicial:</label>
                        <input type="number" id="balance" name="balance" class="input text-base"
                        placeholder="Ingrese el balance inicial" required>
                    </div>
                </div>
                    <div>
                        <button type="submit" class="button bg-primary text-white">Crear Cuenta</button>
                </div>
            </form>
        </div>
    </div>
</main>
</body>
</html>