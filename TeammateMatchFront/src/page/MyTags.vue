<template>
  <van-button type="primary" v-on:click="searchTags()">搜索</van-button>
  <van-row  justify="center" gutter="10">
    <van-col v-for="tag in activeId" >
      <van-tag :show="show" closeable size="medium" type="primary" @close="close(tag)">
        {{tag}}
      </van-tag>
    </van-col>
  </van-row>
  <van-tree-select
      v-model:active-id="activeId"
      v-model:main-active-index="activeIndex"
      :items=items
  />

  <van-button type="primary" v-on:click="save">保存Tags</van-button>
</template>
<script setup>
import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import {showFailToast, showSuccessToast} from "vant";

const activeId = ref([]);
const activeIndex = ref(0);
const items = ref([]);

const show = true;
const close = (tag) => {
  activeId.value=activeId.value.filter(
      (i)=> i!==tag
  );
};

onMounted(()=>{
  axios.get('http://localhost:8080/tags/game?gameName=英雄联盟')
      .then(function (response) {
        // 处理成功情况
        if (response.data.code === 0) {
          showSuccessToast(response.data.code+"\n"+response.data.message);
          items.value=response.data.data;
        } else {
          showFailToast("页面加载失败\n"+response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });

  axios.get('http://localhost:8080/tags/my')
      .then(function (response) {
        // 处理成功情况
        if (response.data.code === 0) {
          showSuccessToast(response.data.code+"\n"+response.data.message);
          if(response.data.data!=="\"[]\""){
            activeId.value=JSON.parse(response.data.data);
          }
        } else {
          showFailToast("页面加载失败\n"+response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
})

const save=()=>{
  axios.post('http://localhost:8080/tags/my',activeId.value)
      .then(function (response) {
        // 处理成功情况
        if (response.data.code === 0) {
          showSuccessToast(response.data.code+"\n"+response.data.message);
        } else {
          showFailToast("页面加载失败\n"+response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
}



</script>