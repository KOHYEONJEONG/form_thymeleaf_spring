package hello.itemservice.domain.item;

public enum ItemType {//상품종류는 ENUM을 사용!, 설명은 DeliveryCode 클래스에서

    BOOK("도서"),
    FOOD("음식"),
    ETC("기타");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }

    /*중요!! 꺼내서 사용해야하니까!!*/
    public String getDescription() {
        return description;
    }
}
