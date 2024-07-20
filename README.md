# Token Orchestrator

Design a server capable of generating, assigning, and managing API keys with specific functionalities. The server should offer various endpoints for interaction:

- **Endpoint to create new keys**: Each generated key has a life of 5 minutes after which it gets deleted automatically if the keep-alive operation is not run for that key (More details mentioned below).
- **Endpoint to retrieve an available key**: Ensuring the key is randomly selected and not currently in use. This key should then be blocked from being served again until its status changes. If no keys are available, a 404 error should be returned.
- **Endpoint to unblock a previously assigned key**: Making it available for reuse.
- **Endpoint to permanently remove a key from the system**.
- **Endpoint for key keep-alive functionality**: Requiring clients to signal every 5 minutes to prevent the key from being deleted.
- **Automatic release of blocked keys within 60 seconds if not unblocked explicitly**.

## Constraints

- Ensuring efficient key management without the need to iterate through all keys for any operation.
- The complexity of endpoint requests should be aimed at O(log n) or O(1) for scalability and efficiency.

## Endpoints

- **POST /keys**: Generate new keys.
  - **Status**: 201

- **GET /keys**: Retrieve an available key for client use.
  - **Status**: 200 / 404 
  - **Response**: 
    ```json
    { 
      "keyId": "<keyID>" 
    }
    ```

- **GET /keys/:id**: Provide information (e.g., assignment timestamps) about a specific key.
  - **Status**: 200 / 404 
  - **Response**: 
    ```json
    { 
      "isBlocked": "<true> / <false>", 
      "blockedAt": "<blockedTime>", 
      "createdAt": "<createdTime>" 
    }
    ```

- **DELETE /keys/:id**: Remove a specific key, identified by :id, from the system.
  - **Status**: 200 / 404

- **PUT /keys/:id**: Unblock a key for further use.
  - **Status**: 200 / 404

- **PUT /keepalive/:id**: Signal the server to keep the specified key, identified by :id, from being deleted.
  - **Status**: 200 / 404
