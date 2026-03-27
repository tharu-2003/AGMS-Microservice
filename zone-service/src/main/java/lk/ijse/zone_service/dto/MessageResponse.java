package lk.ijse.zone_service.dto;

public class MessageResponse {
    private String message;

    // 1. Default Constructor (Parameter nathi ekka)
    public MessageResponse() {
    }

    // 2. All Arguments Constructor (Parameter thiyena ekka)
    public MessageResponse(String message) {
        this.message = message;
    }

    // 3. Getter
    public String getMessage() {
        return message;
    }

    // 4. Setter
    public void setMessage(String message) {
        this.message = message;
    }
}