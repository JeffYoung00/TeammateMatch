package com.jeff.teammate.model.response;

import com.jeff.teammate.model.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 为了和前端二级表的表单对应,在后端做的数据转换
 * 返回的是应该一个ItemOne[]
 */
public class ItemOne implements Serializable {
    //二级选项

    String text;
    List<ItemTwo> children;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ItemTwo> getChildren() {
        return children;
    }

    public void setChildren(List<ItemTwo> children) {
        this.children = children;
    }

    public ItemOne(String text){
        this.text=text;
        children=new ArrayList<>();
    }

    public static List<ItemOne> ItemOnes(List<Tag>tags){
        List<ItemOne> items=new ArrayList<>();
        for(Tag tag:tags){
            if(tag.getIsGroup()==1){
                items.add( new ItemOne(tag.getTagName()) );
            }
        }
        for(Tag tag:tags){
            if(tag.getIsGroup()==1){
                continue;
            }
            for(ItemOne item:items){
                if(item.text.equals(tag.getGroupName())){
                    item.children.add(new ItemTwo(tag.getTagName()));
                    break;
                }
            }
        }
        return items;
    }
    private static final long serialVersionUID = 11L;
}
class ItemTwo implements Serializable{
    String text;
    String id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ItemTwo(String text){
        this.text=text;
        this.id=text;
    }
    private static final long serialVersionUID = 12L;
}
//////////父类继承了序列化,里面的子类?
