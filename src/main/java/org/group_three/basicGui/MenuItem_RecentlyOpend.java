package org.group_three.basicGui;

import javafx.scene.control.MenuItem;

public class MenuItem_RecentlyOpend extends MenuItem {
    
    private String path;

    public MenuItem_RecentlyOpend(String text, String path)
    {
        super(text);
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }

}
