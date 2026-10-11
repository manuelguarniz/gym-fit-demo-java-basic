# GymFit · Registrar pago

Proyecto del **Taller de arquitectura (Curso integrador I, UTP · Semana 9, Sesión 18)**.
Aplicación de escritorio en Java (Swing) organizada en capas **MVC + DAO**, con pruebas **JUnit 5**
y acceso seguro a **MySQL 8**.

## Requisitos

| Herramienta | Versión |
|---|---|
| JDK | 17 o superior |
| Apache NetBeans | 2x (20 o superior) |
| MySQL Server + MySQL Workbench | 8.x |
| Internet | Para que Maven descargue las dependencias la primera vez |

## Cómo ponerlo a funcionar

1. **Base de datos.** Abre MySQL Workbench, entra como `root`, abre `sql/gymfit.sql`
   (File > Open SQL Script) y ejecútalo con el rayo. Crea la base `gymfit`, sus 7 tablas,
   datos de prueba y el usuario `app_gymfit` con permisos mínimos.
2. **Credenciales.** Revisa `src/main/resources/db.properties`: la clave debe ser la misma
   que la del `CREATE USER` del script (`GymFit#2026` en el laboratorio).
   Si clonaste el repositorio y no existe, copia `db.properties.example` como `db.properties`.
3. **Abrir en NetBeans.** File > Open Project… y elige la carpeta `gymfit`
   (NetBeans la reconoce como proyecto Maven por el `pom.xml`).
4. **Descargar dependencias.** Clic derecho en el proyecto > Build with Dependencies.
   Debe terminar con `BUILD SUCCESS`.
5. **Probar.** Clic derecho en el proyecto > Test (Alt+F6). Las 3 pruebas deben pasar.
6. **Ejecutar.** Run > Run Project (F6). Inicia sesión con:

| Usuario | Contraseña | Rol | ¿Puede registrar pagos? |
|---|---|---|---|
| carla | Carla2026* | ASESOR | Sí |
| admin | Admin2026* | ADMIN | Sí |
| pedro | Pedro2026* | ENTRENADOR | No (regla R3) |

Prueba con la cuota **1** y el monto **129.00**. Si la pagas dos veces verás la regla R1;
con otro monto, la regla R2.

## Estructura (una carpeta por capa)

```
src/main/java/pe/gymfit/
├── Main.java                 arma las piezas y arranca
├── modelo/                   Cuota, EstadoCuota, MedioPago, Usuario
├── dao/                      CuotaDAO, UsuarioDAO (interfaces), DatosException
│   └── mysql/                CuotaDAOMySQL, UsuarioDAOMySQL (PreparedStatement)
├── servicio/                 CuotaService (reglas R1 y R2), PagoInvalidoException
├── controlador/              CuotaController (regla R3), LoginController
├── vista/                    CobrarCuotaView (JFrame Form), LoginView
├── seguridad/                Rol, Sesion, Hash (BCrypt), AuthService
└── config/                   Conexion (lee db.properties)
src/main/resources/           db.properties (NO va a Git), db.properties.example
src/test/java/pe/gymfit/
├── dao/CuotaDAOMemoria.java  DAO falso en memoria
└── servicio/CuotaServiceTest.java
sql/gymfit.sql                script de la base de datos
```

## Reglas del negocio

- **R1** Solo se pagan cuotas en estado PENDIENTE → `CuotaService`
- **R2** El monto debe ser igual al de la cuota → `CuotaService`
- **R3** Solo un ASESOR o un ADMIN registran pagos → `CuotaController`

## Seguridad aplicada

- Consultas con `PreparedStatement` (sin concatenar datos del usuario en el SQL).
- Java se conecta con `app_gymfit` (solo SELECT, INSERT, UPDATE), nunca con `root`.
- `db.properties` está en `.gitignore`; se sube `db.properties.example`.
- Contraseñas de usuarios guardadas como hash BCrypt.
- El usuario ve mensajes del negocio; el detalle técnico va solo al log.
