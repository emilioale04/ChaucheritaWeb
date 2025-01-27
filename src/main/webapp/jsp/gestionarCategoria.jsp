<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Categoría</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/styles/styles.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/styles/home.css">
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/jsp/images/dollar.png">
    <script src="https://kit.fontawesome.com/d2aae01839.js" crossorigin="anonymous"></script>
</head>

<body>
    <header class="header bg-primary ds-flex jc-sb pd-y-16 pd-x-24 font-secondary text-white align-center">
        <div></div>
        <div>
            <form action="" method="post">
                <button type="submit" class="logout-button">
                    <span class="pd-r-8 font-bold font-primary text-base">Cerrar Sesión</span>
                    <i class="fa-solid fa-right-from-bracket text-white text-xl"></i>
                </button>
            </form>
        </div>
    </header>
    <main class="ds-flex">
        <nav class="sidemenu bg-light">
            <img src="<%= request.getContextPath() %>/jsp/images/wallet-512px.png" alt="wallet">
            <span class="text-dark font-primary text-center pd-b-16">Chaucherita<br>Web</span>
            <ul class="menu ls-none">
                <li>
                    <button class="menu-button pd-8" onclick="window.location.href='jsp/home.jsp'">
                        <i class="fa-solid fa-house text-xl"></i> Inicio
                    </button>
                </li>
                <li>
                    <button class="menu-button pd-8" onclick="window.location.href='Cuentas'">
                        <i class="fa-solid fa-gear text-xl"></i> Cuentas
                    </button>
                </li>
                <li>
                    <button class="menu-button pd-8" onclick="window.location.href='GestionaCategoria'">
                        <i class="fa-solid fa-tag text-xl"></i> Categorías
                    </button>
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
        <section class="ds-flex-column flex-1 pd-l-32 pd-r-32 pd-t-32">
            <div class="main-content pd-lg">
                <h2 class="font-primary text-dark">Gestionar Categorías</h2>

                <!-- Formulario de Crear/Editar Categoría -->
                <form id="form-categoria" class="font-primary" action="GestionarCategoria" method="post">
                    <input type="hidden" name="ruta" value="${categoria != null ? 'guardarCategoria' : 'guardarCategoria'}">
                    <input type="hidden" name="id" value="${categoria != null ? categoria.id : ''}">
                    <div class="ds-flex">
                        <div class="flex-1 form-group pd-y-16 pd-r-24">
                            <label for="nombre">Nombre de la Categoría:</label>
                            <input type="text" id="nombre" name="nombre" class="input text-base"
                                placeholder="Ingrese el nombre de la categoría"
                                value="${categoria != null ? categoria.nombreCategoria : ''}" required>
                        </div>
                    </div>
                    <div>
                        <button type="submit" class="button bg-primary text-white">
                            ${categoria != null ? 'Guardar Cambios' : 'Crear Categoría'}
                        </button>
                    </div>
                </form>

                <!-- Tabla de Categorías -->
                <h3 class="font-primary text-dark pd-t-40">Lista de Categorías</h3>
                <table class="table border-light">
                    <thead>
                        <tr class="bg-light text-dark">
                            <th class="pd-8">Nombre de la Categoría</th>
                            <th class="pd-8">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="categoria" items="${categorias}">
                            <tr>
                                <td class="pd-8">${categoria.nombreCategoria}</td>
                                <td class="pd-8 ds-flex jc-sa">
                                    <div class="actions-container">
                                        <!-- Botón EDITAR -->
                                        <form action="GestionarCategoria" method="get" style="display: inline;">
                                            <input type="hidden" name="ruta" value="cargarFormularioEdicion">
                                            <input type="hidden" name="id" value="${categoria.id}">
                                            <button type="submit" class="button text-white action-button bg-primary">
                                                Editar
                                            </button>
                                        </form>
                                        <!-- Botón ELIMINAR -->
                                        <form action="GestionarCategoria" method="post" style="display: inline;">
                                            <input type="hidden" name="ruta" value="eliminarCategoria">
                                            <input type="hidden" name="id" value="${categoria.id}">
                                            <button type="submit" class="button bg-error text-white action-button">
                                                Eliminar
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</body>

</html>