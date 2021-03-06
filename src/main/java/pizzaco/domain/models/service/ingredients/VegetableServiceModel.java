package pizzaco.domain.models.service.ingredients;

import java.math.BigDecimal;

public class VegetableServiceModel {

    private String id;
    private String name;
    private BigDecimal price;

    public VegetableServiceModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
