# Sistema de Gestión de Farmacia
# Cambiar esta cosa porfis 
## Requisitos Funcionales

### 1. Gestión de Inventario
- El sistema debe permitir la incorporación de nuevos productos al inventario.  
- Debe permitir la actualización de existencias de productos.  
- Debe registrar la fecha de vencimiento de los productos.  
- Debe permitir la eliminación de productos obsoletos o vencidos.  

---

### 2. Registro de Ventas
- El sistema debe permitir la venta de productos al cliente.  
- Debe calcular automáticamente el precio total de la compra.  
- Debe registrar la información del cliente (nombre, dirección, número de contacto) para ventas con receta.  
- Debe generar un recibo para cada venta.  

---

### 3. Búsqueda y Consulta del Producto
- Debe permitir la búsqueda rápida de productos por nombre, código o categoría.  
- Debe proporcionar información detallada de cada producto, incluyendo precio, existencias y fecha de vencimiento.  

---

### 4. Gestión de Clientes
- Debe permitir la creación y mantenimiento de registros de clientes.  
- Debe proporcionar información sobre las compras anteriores de los clientes.  
- Debe permitir la asignación de descuentos o programas de fidelización a clientes habituales.  
  - *Nota:* El cliente debe contar con un número de **“clienteamigo”** que le permita acceder al programa de descuentos.  

---

### 5. Generación de Reportes
- Debe ser capaz de generar informes de ventas diarias, semanales, mensuales y anuales.  
- Debe generar informes de inventario actualizado.  

## 6. Categorias
-Debe ser capaz de crear categorias.
-Debe ser capaz de buscar las categorias.
-debe ser capaz de modifica, eliminar, y cambiar categorias.

##  Requisitos No Funcionales

### 1. Interfaz de Usuario Intuitiva
- Debe contar con una interfaz de usuario amigable y fácil de usar para el personal de la farmacia.  
- Se construirá utilizando **Java Swing** o **JavaFX**.  
- Se diseñarán las pantallas y formularios para la gestión de productos, ventas, reportes y otras funcionalidades.  

## Priorizacion de Requisitos Funcionales

## 1. Categorias
-Debe ser capaz de crear categorias.
-Debe ser capaz de buscar las categorias.
-debe ser capaz de modifica, eliminar, y cambiar categorias.

### 2. Búsqueda y Consulta del Producto
- Debe permitir la búsqueda rápida de productos por nombre, código o categoría.  
- Debe proporcionar información detallada de cada producto, incluyendo precio, existencias y fecha de vencimiento.  

### 3. Gestión de Clientes
- Debe permitir la creación y mantenimiento de registros de clientes.  
- Debe proporcionar información sobre las compras anteriores de los clientes.  
- Debe permitir la asignación de descuentos o programas de fidelización a clientes habituales.  
  - *Nota:* El cliente debe contar con un número de **“clienteamigo”** que le permita acceder al programa de descuentos.  

---
# Documento de Requisitos Funcionales,

# Priorización y Modelo - Sistema de

# Gestión Farmacéutica (SGF)

## 🎯 Objetivo

Este documento establece los requisitos funcionales y no funcionales del Sistema de Gestión
Farmacéutica, presentando un plan de desarrollo priorizado basado en la
interdependencia de los módulos principales y definiendo la estructura del modelo de
objetos.

## 🚀 Plan de Desarrollo Priorizado (Diseño e

## Implementación)

A continuación, se define el orden de desarrollo de los módulos principales, fundamentado en
la lógica de dependencia funcional del sistema.

### FASE 1: Módulo Categorías (Nivel 1)

* Prioridad: Alta (Estructura de Base)
* Explicación: Este módulo es la base fundamental. Los productos dependen de las categorías 
para su clasificación, por lo que debe ser implementado primero.
Entregables Clave:
- -  Implementación de la clase **Categoria**. 
- -  Funcionalidades CRUD completas (Agregar, Modificar, Listar, Eliminar). 
- -  Desarrollo de un Buscador de Categorías para acceso rápido.

### FASE 2: Módulo Clientes (Nivel 2)

* Prioridad: Media-Alta (Precede a la Venta y la Fidelización)
* Explicación: Se requiere el registro de clientes antes de procesar cualquier venta para poder
asignar descuentos, historial de compras y, especialmente, para ventas con receta médica.
Entregables Clave:
- - Implementación de las clases **Cliente** y **TipoCliente**.
- - Funcionalidades CRUD completas (Agregar, Modificar, Listar, Eliminar).
- - Desarrollo de un Buscador de Clientes para la asignación rápida en el proceso de venta.

### FASE 3: Módulo Productos / Inventario (Nivel 3)

* Prioridad: Media (Depende de Categorías, precede a la Venta)
* Explicación: Una vez definidas las categorías y clientes, se puede poblar el inventario, 
* asociando cada producto a las categorías existentes.
Entregables Clave:
- - Implementación de la clase **Producto**.
- - Funcionalidades CRUD completas (Agregar, Modificar, Listar, Eliminar).
- - Control de existencias, precios y fechas de caducidad.
- - Buscador de Productos con filtros por Categoría.

### FASE 4: Módulo Ventas (Nivel 4)

* Prioridad: Crítica (Flujo de Negocio Principal)
* Explicación: Este es el módulo central. Requiere que los Productos y Clientes ya existan. Aquí
se ejecuta la lógica de negocio, incluyendo la selección de productos y la asignación del
cliente a la transacción.
Entregables Clave:
- - Implementación de la clase **Venta**.
- - Pantalla de **Nueva Venta** para ingresar productos en una lista.
- - Integración con Clientes: Requerimiento de ingresar el cliente para poder finalizar la
venta.
- - Cálculo automático de totales y aplicación de descuentos (si aplica el "clienteamigo").

### FASE 5: Módulo Recibos y Listado de Ventas (Nivel 5)

* Prioridad: Media-Baja (Validación y Auditoría Inmediata)
* Explicación: Depende de una venta completada. Se enfoca en formalizar la transacción y
gestionar su historial.
Entregables Clave:
- - Implementación de la clase **Recibo**.
- - Pantalla para **Listar Ventas** (Historial de Ventas).
- - Funcionalidades de Devolución/Validación y visualización del detalle completo de la
información de la venta/recibo.

### FASE 6: Módulo Reportes (Nivel 6)

* Prioridad: Baja (Inteligencia de Negocio)
* Explicación: Módulo de análisis y auditoría. Requiere que las transacciones (Ventas, Clientes,
Productos, Recibos) ya hayan generado datos históricos para poder generar estadísticas
significativas.
Entregables Clave:
- - Generación de reportes de **Ventas** (Diarias, Semanales, Mensuales, Anuales).
- - Generación de reportes de **Inventario** (Caducidad, Existencias bajas).
- - Generación de reportes de **Clientes**.

### FASE 7: Módulo Usuarios/Sesión (Nivel 7)

* Prioridad: Básico (Seguridad y Administración)
* Explicación: Si bien la autenticación es importante, el desarrollo se enfoca en la funcionalidad
del negocio primero. Se implementa al final para asegurar el acceso y la gestión de roles
sobre un sistema ya funcional.
Entregables Clave:
- - Módulos de Login y Registro de Usuario.
- - Gestión de Perfil de Usuario.

## 📝 Requisitos Funcionales (RF)

Los requisitos funcionales detallados de cada módulo se mantienen como referencia para el
desarrollo.

### NIVEL 1: Gestión de la Base de Datos Estructural (Categorías)

* **Prioridad:** Alta (Base esencial para la definición de productos).
* **RF ID Descripción del Requisito Módulo Asociado
* RF 1.1** El sistema debe permitir el
**Registro/Creación** de
nuevas categorías de
productos (e.g.,
Medicamentos, Higiene
Personal, Misceláneos).
Categorías
* **RF 1.2** El sistema debe permitir la
**visualización y búsqueda**
de todas las categorías
existentes.
Lista de Categorías
* **RF 1.3** El sistema debe permitir la
**modificación** del nombre o
descripción de una
categoría existente.
Editar Categoría
* **RF 1.4** El sistema debe mostrar un
listado de **Productos por
Categoría** para fines de
consulta y organización.
Productos por Categoría

---
### NIVEL 2: Gestión de la Base de Datos de Clientes

* **Prioridad:** Media-Alta (Necesario antes de procesar ventas o descuentos personalizados).

* **RF 2.1** El sistema debe permitir el
  Registro de nuevos
  clientes, incluyendo
  información personal,
  dirección y teléfono, y
  asignación de un tipo de
  cliente.
  Registrar Cliente
* **RF 2.2** El sistema debe generar y
  gestionar un número único
  de 
  cliente, el cual es necesario
  para acceder al programa
  de descuentos y
  fidelización.
  Registrar Cliente / Detalle
  Cliente
* **RF 2.3** El sistema debe permitir la
  visualización, búsqueda y
  filtrado de la lista de
  clientes (e.g., por nombre,
  tipo de cliente).
  Lista de Clientes
* **RF 2.4** El sistema debe permitir la
  edición de la información
  de clientes existentes.
  Editar Cliente
* **RF 2.5** El sistema debe
  proporcionar un Detalle de
  Cliente que incluya su
  perfil completo y un
  Historial de Compras
  (todas las ventas
  relacionadas).
  Detalle de Cliente / Historial
  de Compras

---

### NIVEL 3: Gestión de Inventario y Productos

* **Prioridad:** Media (Depende de Categorías para clasificación, pero precede a Ventas).

* **RF 3.1** El sistema debe permitir la
  Incorporación de nuevos
  productos , registrando:
  nombre, descripción,
  precio, stock inicial, fecha
  de caducidad y asignación
  a una Categoría existente.
  Registrar Producto
* **RF 3.2** El sistema debe permitir la
  actualización de
  existencias de productos
  (entradas/salidas manuales
  por ajuste o devolución).
  Editar Producto
* **RF 3.3** El sistema debe permitir la
  edición de toda la
  información de productos
  existentes.
  Editar Producto
* **RF 3.4** El sistema debe
  proporcionar una Lista de
  Productos con
  capacidades de búsqueda
  rápida (por nombre,
  código, categoría) y filtros
  (por existencia o caducidad
  próxima).
  Lista de Productos /
  Búsqueda
* **RF 3.5** El sistema debe permitir la
  eliminación de productos
  obsoletos o vencidos del
  inventario activo.
  Editar Producto

---

### NIVEL 4: Proceso Central de Venta y Descuentos

* **Prioridad:** Crítica (El módulo principal del negocio. Requiere Clientes y Productos).

* **RF 4.1** El sistema debe permitir el
  inicio de una **Nueva Venta** ,
  incluyendo la selección de
  múltiples productos y
  cantidades.
  Nueva Venta
* **RF 4.2** El sistema debe calcular
  automáticamente el
  subtotal y el precio total
  de la compra.
  Nueva Venta
* **RF 4.3** El sistema debe permitir la
  Aplicación y
  Modificación de
  Descuentos a la venta
  actual, vinculando el
  descuento al número de
  "clienteamigo" si aplica.
  Aplicar Descuentos
* **RF 4.4** El sistema debe validar la
  venta y requerir el registro
  de información de un
  Cliente para ventas que
  requieran Receta Médica.
  Nueva Venta
* **RF 4.5** El sistema debe registrar la
  venta y sus detalles
  completos (productos
  vendidos, cliente, total,
  descuentos) en el historial.
  Nueva Venta
* **RF 4.6** El sistema debe
  proporcionar un Historial
  de Ventas (Lista de
  Ventas) con búsqueda por
  fecha, cliente y producto.
  Lista de Ventas
* **RF 4.7** El sistema debe mostrar el
  Detalle de Venta de una
  transacción específica.
  Detalle de Venta

---

### NIVEL 5: Gestión de Recibos

* **Prioridad:** Media-Baja (Depende de la finalización de una Venta).

* **RF 5.1** El sistema debe generar
  un recibo (comprobante)
  único para cada venta
  finalizada (RF 4.5).
  Nueva Venta
* **RF 5.2** El sistema debe
  proporcionar una Lista de
  Recibos con filtros por
  fecha, tipo de venta y
  cliente.
  Lista de Recibos
* **RF 5.3** El sistema debe mostrar el
  Detalle de Recibo ,
  incluyendo productos,
  descuentos y total final.
  Detalle de Recibo
* **RF 5.4** El sistema debe permitir la
  Validación de Recibo (por
  ejemplo, para devoluciones
  o auditoría).
  Validación de Recibo

---

### NIVEL 6: Generación de Reportes Administrativos

* **Prioridad:** Baja (Módulo de apoyo, no esencial para la operación diaria, pero vital para la administración).
* **RF 6.1** El sistema debe generar un
  **Reporte de Ventas**
  filtrable por rango de
  fechas (Diarias, Semanales,
  Mensuales, Anuales), por
  cliente, por producto o por
  categoría.
  Reporte de Ventas
* **RF 6.2** El sistema debe generar un
  **Reporte de Inventario** que
  muestre: existencias bajas,
  productos vencidos y
  productos próximos a
  caducar.
  Reporte de Inventario
* **RF 6.3** El sistema debe generar un
  Reporte de Clientes que
  liste clientes frecuentes,
  nuevos clientes y
  beneficiarios del programa
  de descuentos
  ("clienteamigo").
  Reporte de Clientes
* **RF 6.4** El sistema debe generar un
  Reporte de Recibos por
  rango de fecha y tipo de
  venta.
  Reporte de Recibos

---

### NIVEL 7: Autenticación y Seguridad (Usuarios/Sesión)

* **Prioridad:** Básico (Necesario para el inicio, pero su complejidad se aborda al final para no bloquear la funcionalidad central).
* RF ID Descripción del Requisito Módulo Asociado
* **RF 7.1** El sistema debe
  implementar un módulo de
  **Login** para la autenticación
  de usuarios mediante
  credenciales.
  Login
* **RF 7.2** El sistema debe permitir el
  **Registro de Nuevos
  Usuarios** con asignación
  de roles (e.g., Empleado,
  Administrador).
  Registro Usuario
* **RF 7.3** El sistema debe permitir al
  usuario autenticado
  consultar y modificar su
  **Perfil de Usuario**.
  Perfil de Usuario


## ⚙ Requisitos No Funcionales (RNF)

RNF ID Descripción del Requisito Categoría
* **RNF 1.1** Interfaz de Usuario
  Intuitiva: El SGF debe
  contar con una interfaz de
  usuario amigable y fácil de
  usar, diseñada para la
  eficiencia del personal de
  la farmacia.
  Usabilidad
* **RNF 1.2** Plataforma de Desarrollo:
  La interfaz gráfica (GUI) se
  construirá utilizando
  tecnologías robustas de
  Java, priorizando JavaFX
  por su modernidad, o como
  alternativa Java Swing.
  Tecnología
* **RNF 1.3** Seguridad de Datos: Las
  credenciales de usuario y
  la información sensible de
  clientes y ventas deben ser
  almacenadas de forma
  segura y cifrada.
  Seguridad
* **RNF 1.4** Rendimiento: Las
  búsquedas de productos y
  clientes (RF 3.4 y RF 2.3)
  deben completarse en
  menos de 1 segundo para
  garantizar una experiencia
  de venta fluida.
  Rendimiento

## 🧩 Modelo de Clases (Plantillas)

Esta sección define las clases centrales del sistema, sus responsabilidades, atributos y
métodos principales, sirviendo como guía para la implementación del código.


### Clase: Categoria

* Elemento Descripción
* Responsabilidad Representa una categoría para distinguir,
  organizar y clasificar productos dentro del
  sistema (ej. Medicamento, Higiene).
  Permite agrupar productos bajo criterios
  comunes.
* Atributos Principales categoriaNombre: String (Nombre
  identificador único), descripcion: String
  (Descripción breve).
* Métodos Principales Categoria(nombre, descripcion),
  setCategoriaNombre(dato),
  setDescripcion(dato),
  getCategoriaNombre(), getDescripcion(),
  toString().
* Principios SOLID Responsabilidad Única (S): Gestión
  exclusiva de la información de la categoría.

---

### Clase: Producto

* Elemento Descripción
* Responsabilidad Representa un artículo gestionado,
  almacenando toda su información
  relevante (inventario, precios, caducidad,
  etc.) y permitiendo clasificarlo en una o
  más categorías.
* Atributos Principales nombre: String, descripcion: String,
  fechaCaducidad: LocalDate, precio:
  double, precioVenta: double, unidadesExt:
  int, codigo: String, categoria:
  ArrayList<Categoria>.
* Métodos Principales Constructores,
  agregarCategoria(Categoria),
  eliminarCategoria(Categoria), Métodos
  setter y getter.
* Principios SOLID Responsabilidad Única (S): Gestión
  exclusiva de la información y
  comportamiento de un solo producto.

---

### Clase: Cliente

* Elemento Descripción
* Responsabilidad Administra los datos personales, tipo de
  cliente, historial de compras y descuentos
  aplicables. Permite segmentar y
  personalizar la atención.
* Atributos Principales idCliente: String, nombre: String, direccion:
  String, numeroContacto: String,
  tipoCliente: TipoCliente, esClienteAmigo:
  boolean, porcentajeDescuento: double,
  historialCompras: ArrayList<Venta>.
* Métodos Principales Constructores, generarID(),
  asignarTipoCliente(),
  agregarCompra(Venta), Métodos setter y
  getter.
* Principios SOLID Responsabilidad Única (S): Gestión
  exclusiva de la información y el
  comportamiento de un cliente individual.

---

### Clase: TipoCliente (Enum)

* Elemento Descripción
* Responsabilidad Enumerar y diferenciar los distintos tipos
  de cliente del sistema (beneficiario, regular,
  nuevo), facilitando la asignación de
  políticas.
* Atributos Principales beneficiario, regular, nuevo (Como valores
  del enum ).
* Relaciones Utilizado por la clase Cliente.
* Principios SOLID Responsabilidad Única (S): Solo existe para
  diferenciar tipos de cliente.

---

### Clase: Venta

* Elemento Descripción
* Responsabilidad Representa una transacción. Almacena
  cliente, productos vendidos, fecha, total, si
  requiere receta, y ofrece métodos para
  calcular totales y aplicar descuentos.
* Atributos Principales idVenta: String, fechaVenta: LocalDate,
  cliente: Cliente, descripcion: String, total:
  double, requiereReceta: boolean, venta:
  ArrayList<Producto>.
* Métodos Principales Constructor, generarIdVenta(),
  agregarProducto(Producto),
  calcularTotal(), generarRecibo(),
  aplicarDescuentoCliente().
* Principios SOLID Responsabilidad Única (S): Gestiona
  únicamente información y lógica propias
  de una venta.

---

### Clase: Recibo

* Elemento Descripción
* Responsabilidad Documento que formaliza una venta.
  Almacena los detalles esenciales de la
  transacción (productos, totales,
  descuentos) y la venta asociada.
* Atributos Principales idRecibo: String, fecha: LocalDate, venta:
  Venta, total: double, descuento: double,
  descuentoAplicado: double, productos:
  ArrayList<Producto>.
* **Métodos Principales** Constructor, listarProductos(),
  generarContenido(), generarIdRecibo(),
  validar().
* **Principios SOLID** Responsabilidad Única (S): Solo gestiona la
  información y operaciones relacionadas
  con la generación y validación de recibos.
