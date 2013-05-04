package net.minecraft.src;

public interface IScrollingGui{
    public int getContentHeight();

    public void scrolled();

    public int getTop();

    public int getBottom();

    public int getLeft();

    public int getRight();
}