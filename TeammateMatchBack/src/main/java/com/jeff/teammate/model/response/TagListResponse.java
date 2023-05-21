package com.jeff.teammate.model.response;

import com.jeff.teammate.model.Tag;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TagListResponse implements Serializable {
    String text;
    List<Elem> children;

    public static TagListResponse[] parse(List<Tag> tags){
        Map<String,List<Elem>>map=new HashMap<>();
        for(int i=0;i<tags.size();i++){
            if(!tags.get(i).isDelete()){
                Elem e=new Elem();
                e.id=i;
                e.text=tags.get(i).getTagName();
                List<Elem>list=map.get(tags.get(i).getGroupName());
                if(list==null){
                    list=new ArrayList<>();
                    map.put(tags.get(i).getGroupName(),list);
                }
                list.add(e);
            }
        }
        TagListResponse[]ret=new TagListResponse[map.size()];
        int i=0;
        for(Map.Entry<String,List<Elem>>entry:map.entrySet()){
            ret[i]=new TagListResponse();
            ret[i].text=entry.getKey();
            ret[i].children=entry.getValue();
            i++;
        }
        return ret;
    }

    private static final long serialVersionUID = 234253453L;
}

@Getter
class Elem implements Serializable {
    int id;
    String text;
    private static final long serialVersionUID = 1234324L;
}
