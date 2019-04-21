# Dynamodb Browser 

### Webapp for browsing DynamoDb

##### Build instructions
```
git clone https://github.com/iksoye/dynamodb-browser.git
cd dynamodb-browser
mvn clean package
```

##### Run
```
cd dynamodb-browser-backend/target/
java -jar dynamodb-browser-backend-<version>.jar 
go to localhost:9000
```

##### Binary Releases
[Release v1.0.0] (https://github.com/iksoye/dynamodb-browser/releases/tag/v1.0.0)

## Features

- Remote Access of AWS DynamoDB Service
  - [x] By providing access & secret keys
  - [x] By using .aws credentials
- View
  - Tables view
    - [x] Records view
  - Export to CSV 
- Operation
  - Record
    - [x] Add Record
    - [x] Edit Record
    - [x] Delete Record
  - Scan 
  - Query
    - [x] By Hash key
    - [x] By Range key
    - [ ] By Index
    - [ ] By Attributes
  - Table
     - [ ] Add Table
     - [ ] Edit Table
     - [ ] Delete Table
