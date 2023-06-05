public class Link {
    private int from;
    private int to;
    private int value;
    private boolean disabled;
    private int indexInList;

    public Link(int from, int to, int value, int index) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.indexInList = index;
        this.disabled = false;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getIndexInList() {
        return indexInList;
    }

    public void setIndexInList(int index) {
        this.indexInList = index;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
