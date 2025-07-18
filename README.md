HOLA A TODOS

Les presento mi proyecto final del curso LP-2 (lenguaje de programacion 2) 

consiste en una aplicacion web en spring jpa, en el que un usuario administrador puede gestionar horarios de psicologos,
agregar especialidades, profesionales, etc, a una base de datos.

El administrador tendra 2 opciones de reportes, uno para las citas por psicologo, y otro es un reporte de citas por estado, 
para estos reportes he utilizado open pdf.

luego en la parte del usuario, puede registrarse, ingresar con sus credenciales (use bycript y jwt para la autenticacion) 
una vez dentro, puede agendar citas, cancelar citas, elegir profesionales, y verificar el listado de profesionaes disponibles.

para inicializar el proyecto y modificarlo a tu antojo, te recomiendo que hagas un clone del repositorio, y uses intelij o vs code.
luego instala las dependencias necesarias, el jdk que he utilizado es java 21.

tambien cabe mencionar, que solo deberas crear tu base de datos, sin tablas, en mysql, debido a que uso entitys, y este al ejecutar el programa
crea automaticamente las tablas, configura tu acceso a tu base de datos en el archivo aplication properties.

y eso es todo. muchas gracias.

