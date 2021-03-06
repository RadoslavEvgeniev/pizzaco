package pizzaco.domain.models.binding.ingredients;

import java.math.BigDecimal;

public class AddSizeBindingModel {

    private String size;
    private BigDecimal price;
    private Integer numberOfSlices;

    public AddSizeBindingModel() {
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNumberOfSlices() {
        return this.numberOfSlices;
    }

    public void setNumberOfSlices(Integer numberOfSlices) {
        this.numberOfSlices = numberOfSlices;
    }
}
