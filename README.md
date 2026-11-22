# StoreMotos

Aplicación móvil para **Android en Kotlin** enfocada en la gestión de inventario de motocicletas en tiempo real con **Firebase**, acceso biométrico mediante `BiometricPrompt`, interfaz construida con **ViewBinding** y lectura de inclinación con el sensor acelerómetro.

## Características principales
- **Login biométrico (`Biometric`):** Pantalla inicial de acceso con autenticación por huella/biometría, manejo de errores y bloqueo del botón atrás sin autenticar.
- **Menú principal (`Menu`):** Panel con `ScrollView` y tarjetas de acceso a catálogo, registro y sensor, diseño en paleta morada y oscura (`#0B0B0F`, `#1B1724`, `#4A1C85`, `#9B5CFF`) y cierre de sesión.
- **Registro y edición (`Registro`):** Formulario para dar de alta o actualizar motocicletas (`marca`, `modelo`, `cilindraje`, `precio`, `stock`) con validaciones y guardado en la colección `"motos"` de Cloud Firestore.
- **Catálogo en tiempo real (`CatalogoMotos`):** Listado dinámico con `RecyclerView` y `MotoAdapter` sincronizado con Firestore mediante `addSnapshotListener`, con acciones para editar y eliminar.
- **Sensor de inclinación (`Sensor`):** Lectura en tiempo real del acelerómetro (`TYPE_ACCELEROMETER`) para calcular ángulos de inclinación y mostrar el estado de estabilidad.

## Estructura del proyecto
- `app/src/main/java/com/example/storemotos/data/Moto.kt`: Modelo de datos de cada motocicleta.
- `app/src/main/java/com/example/storemotos/ui/adapter/MotoAdapter.kt`: Adaptador del `RecyclerView` (`item_moto.xml`).
- `app/src/main/java/com/example/storemotos/`: `Biometric.kt`, `Menu.kt`, `Registro.kt`, `CatalogoMotos.kt` y `Sensor.kt`.
- `app/src/main/res/layout/`: `biometric.xml`, `menu.xml`, `registro.xml`, `catalogo.xml`, `item_moto.xml` y `sensor.xml`.
- `app/src/main/res/drawable/`: Imágenes y recursos gráficos de la aplicación.

## Capturas de pantalla

| Login Biométrico | Menú Principal | Registro de Moto |
| :---: | :---: | :---: |
| <img width="260" alt="Login Biométrico" src="https://github.com/user-attachments/assets/136ff5f8-5fec-4604-a733-72cd9e1f589b" /> | <img width="260" alt="Menú Principal" src="https://github.com/user-attachments/assets/b2da80af-8ca1-492f-af32-5dabe6c456d3" /> | <img width="260" alt="Registro de Moto" src="https://github.com/user-attachments/assets/563f3084-d558-4279-bfb3-45e8e8993edf" /> |

| Catálogo en Tiempo Real | Sensor de Inclinación |
| :---: | :---: |
| <img width="260" alt="Catálogo" src="https://github.com/user-attachments/assets/3f9152bb-bf38-4748-8082-2521d64009f2" /> | <img width="260" alt="Sensor" src="https://github.com/user-attachments/assets/f58c17f2-abf6-41da-8a4a-370e071cb624" /> |

## Instalación y ejecución
1. Descarga el repositorio y ábrelo en **Android Studio**.
2. Coloca tu archivo `google-services.json` dentro de la carpeta `app/`.
3. Verifica que **Cloud Firestore** esté habilitado y que sus reglas de seguridad permitan las operaciones de prueba.
4. Sincroniza Gradle y ejecuta la aplicación en un dispositivo físico o emulador Android.
