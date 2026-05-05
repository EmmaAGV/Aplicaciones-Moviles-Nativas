# RoutinePro 💪 - Tu entrenamiento, tu ritmo

**RoutinePro** es una aplicación de Android nativa diseñada para ayudar a los usuarios a gestionar y visualizar sus rutinas de ejercicio diarias de manera sencilla, elegante y eficiente.

## 🚀 Características

- **Pantalla de Acceso (Login):** Validación de credenciales para un acceso seguro.
- **Dashboard Principal:** Panel de bienvenida personalizado para motivar el inicio del entrenamiento.
- **Gestión de Rutinas:** Visualización clara de grupos musculares y ejercicios (Pecho, Espalda, Piernas, etc.) utilizando componentes modernos como `CardView`.
- **Arquitectura Limpia:** Implementación de **ViewBinding** para un manejo de vistas más seguro y eficiente, evitando errores de nulidad.

## 🛠️ Stack Tecnológico

- **Lenguaje:** [Kotlin](https://kotlinlang.org/)
- **Interfaz Gráfica:** XML con Material Design.
- **Componentes:**
  - ConstraintLayout (Diseños flexibles).
  - ViewBinding (Vinculación de vistas).
  - CardView y LinearLayout.
- **Herramienta de Construcción:** Gradle (Kotlin DSL).

## 📸 Previsualización

| Login | Inicio | Rutinas |
|-------|--------|---------|
| ![Login](https://via.placeholder.com/200x400?text=Login+Screen) | ![Main](https://via.placeholder.com/200x400?text=Main+Screen) | ![Routines](https://via.placeholder.com/200x400?text=Routines+List) |
*(Próximamente capturas de pantalla reales)*

## 📥 Instalación

Para probar este proyecto localmente, sigue estos pasos:

1. **Clonar el repositorio:**
2. **Abrir en Android Studio:**
   Selecciona la carpeta del proyecto y espera a que Gradle sincronice las dependencias.
3. **Ejecutar:**
   Presiona `Run` y selecciona tu emulador o dispositivo físico (Mínimo Android 7.0+).

## 📈 Próximas Mejoras

- [ ] Implementar **Room Database** para persistencia de datos local.
- [ ] Cambiar la lista estática por un **RecyclerView** dinámico.
- [ ] Agregar temporizador para cada serie de ejercicios.
- [ ] Modo oscuro (Dark Mode).

---
