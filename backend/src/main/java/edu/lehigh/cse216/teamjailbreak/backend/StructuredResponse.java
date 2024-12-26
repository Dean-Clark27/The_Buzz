package edu.lehigh.cse216.teamjailbreak.backend;

/**
 * StructuredResponse provides a common format for success and failure messages,
 * with an optional payload of type Object that can be converted into JSON.
 * @param status for applications to determine if response indicates an error
 * @param message only useful when status indicates an error, or when data is null
 * @param data any JSON-friendly object can be referenced here, so a client gets a rich reply
 */
public record StructuredResponse(String status, String message, Object data){
    /**
     * If the status is not provided, set it to "invalid".
     * @param status the status of the response, typically "ok" or "error"
     * @param message the message to go along with an error status
     * @param data an object with additional data to send to the client
     */
    public StructuredResponse {
        status = (status != null) ? status : "invalid";
    }
}
