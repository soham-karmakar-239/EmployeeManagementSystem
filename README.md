EmployeeManagementSystem

---

Overview
Details of employees are stored in the database. Requests are authenticated using email id and password. Authorities are granted to employees using Roles and privileges.

---

Technologies used
Database – MySQL
Development Framework – Spring Boot
Persistence – JPA, Hibernate
Security – Sprint Boot Starter Security

---

API Endpoints
Path		Http method	Function
/		Get		Get details of authenticated user
/employee	Get		Get a list of all employees
/employee/{id}	Get		Get details of an employee having mentioned employee id
/employee	Post		Create new user using details from request body, assigns Basic role if not mentioned otherwise
/employee/{id}	Put		Edits as many fields received in request body in employee details with mentioned employee id
/employee/{id}	Delete		Deletes record of employee with mentioned employee id

/role		Get		Get a list of all roles
/role/{id}	Get		Get details of a role with mentioned role id
/role		Post		Create new role using details from request body
/role/{id}	Put		Edit details (name of role, associated privileges) of role with mentioned role id
/role/{id}	Delete		Deletes record of role with mentioned role id, assigns Basic role to users having that role
/privilege	Get		Get a list of all privileges in the application

---

Roles and Privileges

Privilege 

Represents an atomic action in the application, viz.,
READ_SELF – users with this privilege can fetch the details of their own account from server
READ – fetch details of all employees and roles from server
WRITE_EMPLOYEE – create a new employee record
EDIT_EMPLOYEE – edit details of an existing employee
DELETE_EMPLOYEE – delete an employee record from server
WRITE_ROLE – create a new role record
EDIT_ROLE – edit details of an existing role
DELETE_ROLE – delete a role record from server

Role 

Represents high level role of the employee. Consists of a set of privileges. An employee can be granted a role, and he/she will get the privileges associated with that role. Custom roles can be created with the required privileges. Examples –
Basic – only READ_SELF privilege. A user with this role can only view his/her own account details.
Writer – READ, WRITE_EMPLOYEE, WRITE_ROLE privileges. A user with this role can read and create employee and role records, but not edit or delete existing records.
Owner – User with this role has all privileges available in the application

Privilege Hierarchy
The privileges in the application are arranged in the following hierarchy

DELETE
|
EDIT
|
WRITE
|
READ

So, a user account with EDIT_EMPLOYEE privilege is also automatically gets WRITE_EMPLOYEE and READ privilege. 
Please note that privileges associated with employees and those associated with roles do not belong to the same hierarchy. So, a user account with DELETE_EMPLOYEE privilege will automatically get EDIT_EMPLOYEE, WRITE_EMPLOYEE, READ privileges but NOT EDIT_ROLE, or WRITE_ROLE privileges.
