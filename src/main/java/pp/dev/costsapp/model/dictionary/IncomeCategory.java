package pp.dev.costsapp.model.dictionary;

import lombok.Getter;

@Getter
public enum IncomeCategory {
    SALARY("Зарпплата"),
    BONUS("Премия"),
    FINANCIAL_ASSISTANCE("Материальная помощь"),
    ALIMONY("Алименты"),
    OTHER("Другое");

    private final String categoryValue;


    IncomeCategory(String category) {
        categoryValue = category;
    }
}
