# 🧭 Convenciones de trabajo con GitFlow y GitHub Actions

Este proyecto utiliza **GitFlow** y un conjunto de **GitHub Actions** para mantener la trazabilidad entre ramas, issues y el tablero Kanban del proyecto.

Con el fin de que las automatizaciones funcionen correctamente, **es obligatorio seguir la convención de nombres descrita a continuación**.

---

## 🚀 Convención de nombres para ramas

Cada vez que se cree una nueva rama para trabajar en una historia de usuario, corrección o mejora, **debes incluir el número del issue correspondiente** en el nombre de la rama.

Esto permite que las *GitHub Actions* identifiquen automáticamente el issue asociado y actualicen su estado en el tablero (por ejemplo: *In Progress*, *In Review* o *Done*).

### ✅ Formato general

Usa el siguiente patrón según el tipo de trabajo:

| Tipo de trabajo | Formato de la rama | Ejemplo |
|-----------------|--------------------|----------|
| Nueva funcionalidad | `feature/#<número>-<nombre>` | `feature/#23-add-login` |
| Corrección de errores | `bugfix/#<número>-<nombre>` | `bugfix/#12-fix-null-pointer` |
| Mejora o refactorización | `improvement/#<número>-<nombre>` | `improvement/#45-optimize-query` |
| Hotfix urgente | `hotfix/#<número>-<nombre>` | `hotfix/#5-fix-deploy-bug` |

---

## 🔢 Importancia del número de issue (`#<número>`)

El número identifica directamente el *issue* en GitHub.  
Las GitHub Actions lo utilizan para mover automáticamente la tarjeta correspondiente dentro del tablero del proyecto:

- Al crear una rama `feature/#23-add-login`, el *issue #23* se mueve automáticamente a la columna **In Progress**.  
- Al crear un *Pull Request* con la frase `Closes #23`, el *issue #23* se moverá automáticamente a la columna **Done**.

---

## ⚠️ Si no sigues la notación correcta

Si el nombre de la rama **no incluye el número del issue**, las automatizaciones no podrán reconocerla.

| Caso | Resultado |
|------|------------|
| ❌ `feature/add-login` | No se detecta el número → la Action no hace nada. |
| ✅ `feature/#23-add-login` | Se detecta el número → issue #23 se mueve a *In Progress*. |

En ese caso, tendrás que mover el issue manualmente en el tablero.

---

##  Buenas prácticas de colaboración

1. Crea siempre tus ramas desde `develop`.  
2. Usa nombres en minúsculas con guiones medios (`-`).  
3. Incluye la referencia del issue en el título o descripción del *Pull Request* (`Closes #xx`).  
4. Nunca trabajes directamente sobre `main` o `develop`.  
5. Si trabajas en equipo, asegúrate de que **todos sigan esta convención** para mantener la trazabilidad y la automatización.

---

## Flujo de trabajo automatizado

| Evento | Acción automática |
|--------|-------------------|
| Se crea una rama con `#<número>` | El issue pasa a **In Progress** |
| Se abre un Pull Request | El issue pasa a **In Review** |
| Se fusiona un Pull Request con `Closes #<número>` | El issue pasa a **Done** |

Este flujo garantiza que el tablero del proyecto refleje en tiempo real el avance del equipo sin necesidad de actualizaciones manuales.

---

## Ejemplo completo

Supongamos que existe un *issue* **#27 - HU: Como usuario quiero iniciar sesión con mi correo y contraseña**.

1. Crea tu rama:
   ```bash
   git checkout develop
   git pull
   git checkout -b feature/#27-add-login
