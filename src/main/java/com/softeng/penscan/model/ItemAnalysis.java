package com.softeng.penscan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Item_Analysis")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemAnalysis {

    @Id
    private String itemanalysisid;
    private String quizid;
    private String itemanalysisname;
    private String itemanalysis;
}
