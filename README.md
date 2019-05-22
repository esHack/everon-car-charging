# everon-car-charging

Used Spring boot
Used ConcurrentHashMap to acieve thread safety and required time complexity(insertions and retrievals)
Assumptions Made: 
http://localhost:8080/chargingSessions/{id}  id here is UUID,  not the charging station id

run the following command to build the project that creates car-charging-app-0.0.1-SNAPSHOT.jar in target folder

#### mvn clean install -U

Navigate to target folder and from cmd

#### java -jar car-charging-app-0.0.1-SNAPSHOT.jar 

#### Rest End points

#### http://localhost:8080/chargingSessions  POST

Request:
```json
{
"stationId":"ABC-1113232333",
"timestamp":"2019-05-06T19:00:20.529"
}
```
Response:
```json
{
   "id": "483917dc-6ac5-422b-b2de-17361cf201ce",
   "stationId": "ABC-1113232333",
   "startedAt": "2019-05-06T19:00:20.529",
   "status": "IN_PROGRESS"
}
```
#### http://localhost:8080/chargingSessions  GET
Response:
```json
[
      {
      "id": "54714d2e-2cf7-4366-ad2e-67a03a6b4267",
      "stationId": "ABC-1113232333",
      "startedAt": "2019-05-06T19:00:20.529",
      "status": "IN_PROGRESS"
   },
      {
      "id": "483917dc-6ac5-422b-b2de-17361cf201ce",
      "stationId": "ABC-1113232333",
      "startedAt": "2019-05-06T19:00:20.529",
      "status": "IN_PROGRESS"
   }
]
```

#### http://localhost:8080/chargingSessions/54714d2e-2cf7-4366-ad2e-67a03a6b4267  PUT
```json
Response:
{
   "id": "54714d2e-2cf7-4366-ad2e-67a03a6b4267",
   "stationId": "ABC-1113232333",
   "startedAt": "2019-05-06T19:00:20.529",
   "status": "FINISHED"
}
```
#### http://localhost:8080/chargingSessions/summary  GET
Response:
```json
{
   "totalCount": 3,
   "startedCount": 1,
   "stoppedCount": 1
}
```
