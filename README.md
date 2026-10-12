# Ejercicio Guía RMI - IMC (LipeRMI)

**Autor del proyecto:** Lewis Velásquez Watts ([@Levelasquez15](https://github.com/Levelasquez15))  
**Basado en la guía del docente:** John Carlos Arrieta Arrieta  
**Materia:** Sistemas Distribuidos / Seminario de Integración  
**Tecnología:** Java SE, RMI (Remote Method Invocation) con LipeRMI, Swing GUI, NetBeans IDE  

---

## 1. Descripción del Proyecto

Este repositorio contiene la implementación completa y paso a paso de una **aplicación cliente-servidor distribuida sobre TCP/IP** para el cálculo del **Índice de Masa Corporal (IMC)**. La comunicación remota se implementa mediante la librería liviana de RMI **LipeRMI** (`lipermi-1.0.1.jar`).

El sistema está desacoplado en tres proyectos independientes estándar de NetBeans (Java with Ant):
1. **`EjemploLipeRmiLibImc`**: Librería compartida que contiene el modelo de datos (DTO serializable) y la interfaz remota del servicio.
2. **`EjemploLipeRmiServidorImc`**: Aplicación de servidor que implementa la lógica de cálculo del IMC, registra el servicio en el invocador remoto y escucha en el puerto TCP `9007`.
3. **`EjemploLipeRmiClienteImc`**: Aplicación cliente con interfaz gráfica de usuario en Swing (`JFrame`), pestañas para gestión de conexión y cálculo del IMC mediante ejecución en segundo plano (hilos).

---

## 2. Estructura del Repositorio

```text
Ejercicio guia RMI - IMC/
├── lib/
│   └── lipermi-1.0.1.jar                 # Librería LipeRMI requerida
├── EjemploLipeRmiLibImc/                 # PROYECTO 1: Java Class Library
│   ├── build.xml
│   ├── manifest.mf
│   ├── nbproject/
│   │   ├── build-impl.xml
│   │   ├── project.properties
│   │   └── project.xml
│   └── src/
│       └── johnarrieta/seminario/imc/rmi/lib/
│           ├── DatosImc.java             # Objeto de transferencia (DTO) Serializable
│           └── IRemotaCalculoImc.java    # Contrato de métodos remotos
├── EjemploLipeRmiServidorImc/            # PROYECTO 2: Servidor RMI
│   ├── build.xml
│   ├── manifest.mf
│   ├── nbproject/
│   │   ├── build-impl.xml
│   │   ├── project.properties
│   │   └── project.xml
│   └── src/
│       └── johnarrieta/seminario/imc/rmi/
│           ├── Principal.java            # Punto de entrada (main) del Servidor
│           └── net/
│               ├── CalculoRmiImcImplem.java # Implementación remota del cálculo IMC
│               └── Servidor.java         # Enlace al CallHandler y Server en puerto 9007
├── EjemploLipeRmiClienteImc/             # PROYECTO 3: Cliente GUI RMI
│   ├── build.xml
│   ├── manifest.mf
│   ├── nbproject/
│   │   ├── build-impl.xml
│   │   ├── project.properties
│   │   └── project.xml
│   └── src/
│       └── johnarrieta/seminario/imc/rmi/
│           ├── Principal.java            # Punto de entrada (main) del Cliente
│           └── vistas/
│               ├── VentanaPrincipal.java # GUI Swing con pestañas y eventos
│               └── VentanaPrincipal.form # Archivo de diseño visual Matisse para NetBeans
├── compilar.bat                          # Script batch para compilar los 3 proyectos
├── ejecutar_servidor.bat                 # Script batch para iniciar el Servidor
├── ejecutar_cliente.bat                  # Script batch para iniciar el Cliente GUI
├── .gitignore
└── README.md
```

---

## 3. Detalle de Componentes y Código Fuente

### 3.1. Librería Compartida (`EjemploLipeRmiLibImc`)

- **`DatosImc.java`**:
  Implementa `java.io.Serializable` para permitir el empaquetado, transporte binario por la red (marshalling/unmarshalling) y reconstrucción en el extremo receptor. Contiene los campos:
  - `peso` (`float`): Peso del usuario en kilogramos.
  - `altura` (`float`): Estatura del usuario en metros.
  - `resultado` (`float`): Valor numérico calculado del IMC.
  - `interpretacion` (`String`): Diagnóstico médico o mensaje de error.

- **`IRemotaCalculoImc.java`**:
  Interfaz pública que define el método de negocio distribuido:
  ```java
  public DatosImc calcularImc(DatosImc datos);
  ```

---

### 3.2. Servidor RMI (`EjemploLipeRmiServidorImc`)

- **`CalculoRmiImcImplem.java`**:
  Implementa `IRemotaCalculoImc`. Aplica la fórmula médica del IMC:
  $$\text{IMC} = \frac{\text{peso}}{\text{altura}^2}$$
  Aplica la escala de valoración:
  - $\text{peso} \le 0 \lor \text{altura} \le 0$: Mensaje de error (`ERROR: El peso y la altura deben ser mayores que 0`).
  - $\text{IMC} < 18.5$: `"Debes consultar un Medico, tu peso es muy bajo"`
  - $18.5 \le \text{IMC} \le 24.9$: `"Estas bien de peso"`
  - $24.9 < \text{IMC} \le 29.9$: `"Debes bajar un poco de peso"`
  - $\text{IMC} > 29.9$: `"Debes consultar un Medico, tu peso es muy alto"`

- **`Servidor.java`**:
  Configura la infraestructura de comunicación de LipeRMI:
  - Instancia `CallHandler invocador` y `Server servidor`.
  - Registra globalmente el objeto de negocio: `invocador.registerGlobal(IRemotaCalculoImc.class, calculoImc)`.
  - Vincula el socket al puerto TCP `9007`: `servidor.bind(puerto, invocador)`.

- **`Principal.java`**:
  Arranca la instancia del servidor y mantiene activo el hilo principal para escuchar permanentemente solicitudes entrantes.

---

### 3.3. Cliente Swing (`EjemploLipeRmiClienteImc`)

- **`VentanaPrincipal.java`**:
  Interfaz gráfica basada en `javax.swing.JFrame` organizada mediante `JTabbedPane`:
  - **Pestaña 1: CONEXIÓN**
    - Dirección IP (`localhost` por defecto) y Puerto de Red (`9007` por defecto).
    - Etiqueta de estado de conexión (`Desconectado` en rojo / `Conectado` en verde).
    - Botón `btnIniciar`: Conecta mediante `net.sf.lipermi.net.Client`, obtiene el proxy remoto vía `client.getGlobal(IRemotaCalculoImc.class)`, o desconecta liberando el socket.
  - **Pestaña 2: CALCULAR IMC**
    - Entradas de texto para `PESO` y `ALTURA`.
    - Botón `CALCULAR` (`btnIniciar1`): Invoca el método remoto.
    - **Concurrencia multihilo**: La llamada remota se ejecuta dentro de un hilo secundario (`new Thread()`) para evitar bloquear el *Event Dispatch Thread (EDT)* de Swing, garantizando que la ventana no se congele durante la comunicación por red.
    - Muestra el resultado numérico y la interpretación diagnóstica devuelta por el servidor.

---

## 4. Instrucciones de Ejecución

### Opción A: Desde Apache NetBeans IDE
1. Abrir NetBeans IDE (versión 8, 12, 17, 25 o superior).
2. Seleccionar **File > Open Project...** y abrir en orden:
   - `EjemploLipeRmiLibImc`
   - `EjemploLipeRmiServidorImc`
   - `EjemploLipeRmiClienteImc`
3. Dar clic derecho en `EjemploLipeRmiLibImc` y seleccionar **Clean and Build**.
4. Dar clic derecho en `EjemploLipeRmiServidorImc` y seleccionar **Run**.
   - En la consola de salida se confirmará la escucha en el puerto `9007`.
5. Dar clic derecho en `EjemploLipeRmiClienteImc` y seleccionar **Run**.
6. En la ventana del cliente:
   - Pestaña **CONEXION**: Presionar **Conectar**. El estado cambiará a `Conectado` (color verde).
   - Pestaña **CALCULAR IMC**: Ingresar peso (ej. `78`) y altura (ej. `1.7`), luego presionar **CALCULAR**. El resultado `26.9896...` y el mensaje `"Debes bajar un poco de peso"` se visualizarán inmediatamente.

### Opción B: Mediante Scripts de Consola (Windows)
1. Ejecutar `compilar.bat` para construir los artefactos JAR de los tres proyectos.
2. Ejecutar `ejecutar_servidor.bat` en una ventana de comandos.
3. Ejecutar `ejecutar_cliente.bat` para abrir la interfaz gráfica Swing.

---

## 5. Actividades Académicas Complementarias

### 5.1. Comparación: RMI Estándar de Java vs. LipeRMI

| Criterio | RMI Estándar de Java (`java.rmi.*`) | LipeRMI (`net.sf.lipermi.*`) |
| :--- | :--- | :--- |
| **Registro de nombres** | Requiere ejecutar un daemon `rmiregistry` (puerto por defecto 1099) o invocar `LocateRegistry.createRegistry()`. | No necesita servicio de directorio ni daemon externo; el servidor expone directamente un socket TCP (`Server.bind`). |
| **Definición de Interfaz** | Obligatorio heredar de `java.rmi.Remote` y declarar `throws RemoteException` en cada método. | Interfaz POJO limpia estándar; no requiere herencia de interfaces especiales ni excepciones propietarias. |
| **Herencia de Implementación** | Tradicionalmente requiere extender de `UnicastRemoteObject` o llamar a `UnicastRemoteObject.exportObject()`. | La clase implementadora es un objeto Java plano (POJO); solo implementa la interfaz de negocio. |
| **Manejo de Red y Puertos** | Tradicionalmente abre puertos TCP dinámicos adicionales para los stubs, dificultando el paso por firewalls y NAT. | Utiliza una conexión TCP limpia y unificada sobre un único puerto configurado (ej. 9007), amigable con firewalls. |
| **Poder y Flexibilidad** | Soporta políticas de seguridad complejas (`SecurityManager`), descarga dinámica de código (Codebase) y activación remota. | Enfoque minimalista (*Lean RMI*), ligero, enfocado estrictamente en RPC sobre sockets TCP directos. |

---

### 5.2. Otras Librerías y Protocolos de Sistemas Distribuidos (RPC)

1. **gRPC (Google Remote Procedure Call)**:
   - Framework moderno de alto rendimiento que opera sobre **HTTP/2**.
   - Utiliza **Protocol Buffers (protobuf)** como lenguaje de definición de interfaces (IDL) y mecanismo de serialización binaria compacta.
   - Soporta múltiples lenguajes (Java, Go, Python, C++, C#) y streaming bidireccional.
   
2. **Apache Thrift (Facebook / Apache)**:
   - Permite definir tipos de datos y servicios en un archivo `.thrift` para generar código en más de 20 lenguajes.
   - Ofrece transportes binarios eficientes para microservicios y sistemas heterogéneos.

3. **Pyro5 (Python Remote Objects)**:
   - Librería para Python similar a Java RMI. Permite publicar objetos remotos (`@Pyro5.api.expose`) y consumirlos transparentemente como si fueran locales en la misma máquina o en red.

4. **CORBA (Common Object Request Broker Architecture)**:
   - Estándar histórico orientado a interoperabilidad entre diferentes plataformas y lenguajes mediante IDL y el bus ORB (Object Request Broker).

---

## 6. Historial de Commits del Repositorio

El proyecto fue estructurado en 4 commits con intervalos cronológicos de 20 minutos:

1. **`feat(lib)`** (14:55:00): Creación de la librería común `EjemploLipeRmiLibImc`, DTO `DatosImc`, interfaz `IRemotaCalculoImc`, configuración Ant de NetBeans y dependencia LipeRMI.
2. **`feat(server)`** (15:15:00): Creación del proyecto servidor `EjemploLipeRmiServidorImc`, clase `CalculoRmiImcImplem`, escucha de red en `Servidor` y punto de entrada `Principal`.
3. **`feat(client)`** (15:35:00): Creación del proyecto cliente `EjemploLipeRmiClienteImc`, diseño visual Swing `VentanaPrincipal`, formulario Matisse `.form`, lógica de conexión y cálculo multihilo.
4. **`docs & tooling`** (15:55:00): Scripts automatizados de compilación/ejecución (`.bat`) y documentación técnica integral en `README.md`.
