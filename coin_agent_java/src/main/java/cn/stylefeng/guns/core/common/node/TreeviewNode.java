package cn.stylefeng.guns.core.common.node;

import java.util.List;

import cn.stylefeng.roses.kernel.model.tree.Tree;

/**
 * jquery ztree 插件的节点
 */
public class TreeviewNode implements Tree {

    /**
     * 附加信息，一般用于存业务id
     */
    private String tags;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 节点名称
     */
    private String text;

    /**
     * 子节点
     */
    private List<TreeviewNode> nodes;

    @Override
    public String getNodeId() {
        return tags;
    }

    @Override
    public String getNodeParentId() {
        return parentId;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void setChildrenNodes(List childrenNodes) {
        this.nodes = childrenNodes;
    }

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<TreeviewNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<TreeviewNode> nodes) {
		this.nodes = nodes;
	}
    
    
}
