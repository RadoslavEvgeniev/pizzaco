package pizzaco.domain.models.view.ingredients;

import java.math.BigDecimal;

public class SizeViewModel {

    private String size;
    private BigDecimal price;
    private Integer numberOfSlices;

    public SizeViewModel() {
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
