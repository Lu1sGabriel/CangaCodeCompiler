package model;

public record TokenModel(String name, String code) {

    @Override
    public String toString() {
        return "TokenModel{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}