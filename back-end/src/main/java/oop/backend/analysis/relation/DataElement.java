package oop.backend.analysis.relation;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataElement {
    private String marketplace;
    private String collection;
    private double volume;                      // thông tin về volume của nft trên sàn tương ứng
    private String currency;                    // loại tiền tệ (ETH/BNB/USD)
    private int numberOfPost;           // thông tin về content các post về nft này trên twitter
}