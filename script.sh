curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/problem+json' --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTU0OTE0ODg2OX0.34rIXsrRN4ryMgZ8KE7XJE2GTljga_MYnbhunqqH0qCUs7GObzQpjwQmdXU-8t4-IBj03K1aT7eSsqH28k16rA' -d '{ \
   "direccion": "Mall El Jardín, 3er nivel; Scala Shopping, 2do nivel", \
   "imagen": "string", \
   "imagenContentType": "png", \
   "nombre": "BOCATTO DA FIORENTINO", \
 }' 'http://localhost:8080/api/restaurantes'
