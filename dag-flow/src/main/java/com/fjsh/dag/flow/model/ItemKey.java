package com.fjsh.dag.flow.model;

/**
 */
public class ItemKey {
    public long itemId;
    public String itemType;
    public long childId;

    public ItemKey(long itemId, String itemType) {
        this(itemId, itemType, 0L);
    }

    public ItemKey(long itemId, String itemType, long childId) {
        this.itemId = itemId;
        this.itemType = itemType;
        this.childId = childId;
    }

    @Override
    public String toString() {
        return itemType + ":" + itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemKey itemKey = (ItemKey) o;

        if (itemId != itemKey.itemId) return false;
        if (childId != itemKey.childId) return false;
        return itemType != null ? itemType.equals(itemKey.itemType) : itemKey.itemType == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (itemId ^ (itemId >>> 32));
        result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
        result = 31 * result + (int) (childId ^ (childId >>> 32));
        return result;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public long getChildId() {
        return childId;
    }

    public void setChildId(long childId) {
        this.childId = childId;
    }
}
