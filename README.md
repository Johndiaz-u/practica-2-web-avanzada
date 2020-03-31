# practica-jms

Ejecutar primero RabbitMQ mensajería de cola

Compilamos la imagen de rabbit 

docker build -t rabbitmq-project . 

docker run -d -p 5672:5672 -p 15672:15672 rabbitmq-project

 En el archivo src/main/java/edu/pucmm/practica13/ApplicationConfiguration.java editar en la linea 18
 Poner el correo y la contraseña en la linea 19
 
 En el archivo src/main/java/edu/pucmm/practica13/services/AlarmThread.java
 
 Tener python 3.6.x o mas reciente
 
 ejecutar pip install -r requirements.txt
 ejecutar el sensor programado.
 python ./SensorClient.py --host=localhost--exchange=devices --device=DEV-01
 
 y luego correr el proyecto.