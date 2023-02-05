package hello.itemservice.domain.item;

import lombok.Data;

import java.util.List;

@Data
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    private Boolean open; //판매 여부(단일 체크박스, true or false)
    private List<String> regions; //등록 지역(멀티 체크박스, 서울 or 부산 or 제주)

    private ItemType itemType; //상품 종류(라디오버튼, 라디오는 무조건 단일선택이므로 list로 안함. ENUM으로 보냈으니 반환값도 똑같이 ItemType )

    private String deliveryCode; //배송 방식(셀렉트 박스도 한가지 값만 선택하기에 String)

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
