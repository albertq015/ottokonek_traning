package id.ottodigital.data.entity.idm.misc;

public class PriceData{
	private String updatedAt;
	private int price;
	private String createdAt;
	private int id;
	private int stockpointId;

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStockpointId() {
		return stockpointId;
	}

	public void setStockpointId(int stockpointId) {
		this.stockpointId = stockpointId;
	}
}
