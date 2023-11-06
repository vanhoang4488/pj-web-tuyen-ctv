package vanhoang.project.dto.base;

import lombok.Getter;

@Getter
public enum ResponseStatus {

    SUCCESS(200, "successed");

    private final int status;
    private final String message;

    ResponseStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
