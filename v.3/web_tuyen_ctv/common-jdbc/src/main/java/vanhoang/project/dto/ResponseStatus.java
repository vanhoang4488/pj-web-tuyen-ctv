package vanhoang.project.dto;

import lombok.Getter;

@Getter
public enum ResponseStatus {

    SUCCESS(200, "successed"),
    FAIL(500, "failed");

    private int status;
    private String message;

    ResponseStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
