package com.hxgis.authserver.model;

import java.util.List;

/**
 * Created by tasty on 2016/9/28 0028.
 */
public class TreeNode {

    private String id;
    private String name;
    private String parentId;
    private Boolean open;
    private Boolean checked;
    private String icon;
    private List<TreeNode> children;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    private String nodeId;//对应module表的Id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean check) {
        checked = check;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
