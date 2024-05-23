# bank-api

Bank API service

Stack
===================

* Java 17
* Spring (boot, validation, data JPA)
* Hikari connection pool
* Hibernate
* PostgreSQL
* Lombok

API Endpoints
===================

Authentication

    POST /api/login: Sign in to the system

```json
{
"username": "username",
"password": "password"
}

Bank Account Operations

    POST /api/accounts/{fromUserId}/transfer/{toUserId}: Transfer money between bank accounts
    GET /api/accounts/{bankAccountId}/balance: Get account balance

User Operations

    POST /api/users: Create a new user
       
    
    {
        "username": "username",
        "email": "email@email.ru",
        "password": "password",
        "firstName": "firstName",
        "lastName": "lastName",
        "dateOfBirth": "2020-01-01",
        "phoneNumber": "+77777777777",
        "initialDeposit": "100"
    }

    POST /api/users/{userId}/phone: Add phone number to user
    POST /api/users/{userId}/email: Add email to user
    PUT /api/users/{userId}/phone: Update user's phone number
    PUT /api/users/{userId}/email: Update user's email
    DELETE /api/users/{userId}/phone: Delete user's phone number
    DELETE /api/users/{userId}/email: Delete user's email
    GET /api/users: Search users