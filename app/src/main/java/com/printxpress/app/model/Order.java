package com.printxpress.app.model;

public class Order {
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_PRINTING = "PRINTING";
    public static final String STATUS_READY = "READY";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    public static final String DELIVERY_PICKUP = "PICKUP";
    public static final String DELIVERY_HOME = "HOME_DELIVERY";

    private long id;
    private long userId;
    private long productId;
    private String productName;        // joined
    private String customerName;       // joined
    private int quantity;
    private String specifications;
    private String customText;
    private String designFileUri;
    private String deliveryType;
    private String deliveryAddress;
    private double totalAmount;
    private String status;
    private long createdAt;
    private long updatedAt;

    public Order() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getSpecifications() { return specifications; }
    public void setSpecifications(String specifications) { this.specifications = specifications; }
    public String getCustomText() { return customText; }
    public void setCustomText(String customText) { this.customText = customText; }
    public String getDesignFileUri() { return designFileUri; }
    public void setDesignFileUri(String designFileUri) { this.designFileUri = designFileUri; }
    public String getDeliveryType() { return deliveryType; }
    public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    /** A customer is allowed to cancel only before printing actually begins. */
    public boolean canBeCancelledByCustomer() {
        return STATUS_PENDING.equals(status) || STATUS_PROCESSING.equals(status);
    }
}
