package com.jeff.teammate.mapper;

import com.jeff.teammate.model.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagMapperTest {

    @Autowired
    TagMapper tagMapper;


    @Test
    public void selectTags() {
        System.out.println(tagMapper.selectTags());
    }

    @Test
    public void addTag() {
        Tag tag=new Tag();
        tagMapper.addTag(0,"比尔吉沃特","大区");
        tagMapper.deleteTag(0,"诺克萨斯");
    }

    @Test
    public void deleteTag() {
    }
}