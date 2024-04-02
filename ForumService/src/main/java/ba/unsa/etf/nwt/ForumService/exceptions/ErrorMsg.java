package ba.unsa.etf.nwt.ForumService.exceptions;

public class ErrorMsg {
    private String error;
    private String message;

    public ErrorMsg() {
        this.error = "unknown";
        this.message = "unknown";
    }

    public ErrorMsg(String errorType, String msg) {
        this.error = errorType;
        this.message = msg;
    }

    public ErrorMsg(String msg) {
        this.error = "validation";
        this.message = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
