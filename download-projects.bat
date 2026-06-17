@echo off
echo Descargando microservicios Spring Boot...
echo.
echo Descargando eureka.zip...
curl -o eureka.zip "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.5.14&baseDir=eureka&groupId=cl.triskeledu&artifactId=cl-triskeledu-eureka&name=biblioteca-eureka&description=servicio-eureka&packageName=cl.triskeledu.eureka&packaging=jar&javaVersion=21&dependencies=cloud-eureka-server,devtools"
echo.
echo Descargando ms-auth.zip...
curl -o ms-auth.zip "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.5.14&baseDir=ms-auth&groupId=cl.triskeledu&artifactId=cl-triskeledu-auth&name=biblioteca-auth&description=servicio-auth&packageName=cl.triskeledu.auth&packaging=jar&javaVersion=21&dependencies=web,data-jpa,lombok,postgresql,cloud-feign"
echo.
echo Descargando ms-proceso.zip...
curl -o ms-proceso.zip "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.5.14&baseDir=ms-proceso&groupId=cl.triskeledu&artifactId=cl-triskeledu-proceso&name=biblioteca-proceso&description=servicio-proceso&packageName=cl.triskeledu.proceso&packaging=jar&javaVersion=21&dependencies=web,data-jpa,lombok,postgresql,cloud-feign"
echo.
echo Descargando ms-menores.zip...
curl -o ms-menores.zip "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.5.14&baseDir=ms-menores&groupId=cl.triskeledu&artifactId=cl-triskeledu-menores&name=biblioteca-menores&description=servicio-menores&packageName=cl.triskeledu.menores&packaging=jar&javaVersion=21&dependencies=web,data-jpa,lombok,postgresql,cloud-feign"
echo.
echo Descargando ms-reporte.zip...
curl -o ms-reporte.zip "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.5.14&baseDir=ms-reporte&groupId=cl.triskeledu&artifactId=cl-triskeledu-reporte&name=biblioteca-reporte&description=servicio-reporte&packageName=cl.triskeledu.reporte&packaging=jar&javaVersion=21&dependencies=web,data-jpa,lombok,postgresql,cloud-feign"
echo.
echo Descargando ms-auditoria.zip...
curl -o ms-auditoria.zip "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.5.14&baseDir=ms-auditoria&groupId=cl.triskeledu&artifactId=cl-triskeledu-auditoria&name=biblioteca-auditoria&description=servicio-auditoria&packageName=cl.triskeledu.auditoria&packaging=jar&javaVersion=21&dependencies=web,data-jpa,lombok,postgresql,cloud-feign"
echo.
echo Descargando ms-sag.zip...
curl -o ms-sag.zip "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.5.14&baseDir=ms-sag&groupId=cl.triskeledu&artifactId=cl-triskeledu-sag&name=biblioteca-sag&description=servicio-sag&packageName=cl.triskeledu.sag&packaging=jar&javaVersion=21&dependencies=web,data-jpa,lombok,postgresql,cloud-feign"
echo.
echo Descargando ms-pdi.zip...
curl -o ms-pdi.zip "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.5.14&baseDir=ms-pdi&groupId=cl.triskeledu&artifactId=cl-triskeledu-pdi&name=biblioteca-pdi&description=servicio-pdi&packageName=cl.triskeledu.pdi&packaging=jar&javaVersion=21&dependencies=web,data-jpa,lombok,postgresql,cloud-feign"
echo.
echo Descargando ms-notaria.zip...
curl -o ms-notaria.zip "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.5.14&baseDir=ms-notaria&groupId=cl.triskeledu&artifactId=cl-triskeledu-notaria&name=biblioteca-notaria&description=servicio-notaria&packageName=cl.triskeledu.notaria&packaging=jar&javaVersion=21&dependencies=web,data-jpa,lombok,postgresql,cloud-feign"
echo.
echo Descargando ms-monitoreo.zip...
curl -o ms-monitoreo.zip "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.5.14&baseDir=ms-monitoreo&groupId=cl.triskeledu&artifactId=cl-triskeledu-monitoreo&name=biblioteca-monitoreo&description=servicio-monitoreo&packageName=cl.triskeledu.monitoreo&packaging=jar&javaVersion=21&dependencies=web,actuator,lombok,cloud-feign"
echo.
echo Descargando ms-datos.zip...
curl -o ms-datos.zip "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.5.14&baseDir=ms-datos&groupId=cl.triskeledu&artifactId=cl-triskeledu-datos&name=biblioteca-datos&description=servicio-datos&packageName=cl.triskeledu.datos&packaging=jar&javaVersion=21&dependencies=web,data-jpa,lombok,postgresql,cloud-feign"
echo.
echo Descarga completada.
pause
