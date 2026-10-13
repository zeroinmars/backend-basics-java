package kr.example.basics;

public class TaskTitleValidator {

    public String validate(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
        }
        return title.strip();
    }
}
