<template>
  <van-button type="primary" v-on:click="searchTags()">搜索</van-button>


  <van-row  justify="center" gutter="10">
    <van-col v-for="tag in activeId" >
      <van-tag :show="showTag" closeable size="medium" type="primary" @close="close(tag)">
        {{tag}}
      </van-tag>
    </van-col>
  </van-row>


  <van-tree-select
      v-model:active-id="activeId"
      v-model:main-active-index="activeIndex"
      :items="state.items"
  />


  <van-button  type="success" to="/editTags">增删Tags</van-button>
  <van-button to="/myTags" type="primary">我的Tags</van-button>


  <p>result list:</p>
    <p v-for="data in state.datas" >
        <img src={{data.avatarUrl}}> {{data.userName}} {{data.gender}}
    </p>


</template>
<script setup>

import axios from "axios";
import {onMounted, reactive, ref} from "vue";
import {showFailToast, showSuccessToast} from "vant";

const activeId = ref([]);
const activeIndex = ref(0);
const state = reactive({
  items: [],
  datas: []
});

onMounted(()=>{
  axios.get('http://localhost:8080/tags/game?gameName=英雄联盟')
      .then(function (response) {
        // 处理成功情况
        if (response.data.code === 0) {
          showSuccessToast(response.data.code+"\n"+response.data.message);
          state.items=response.data.data;
        } else {
          showFailToast("页面加载失败\n"+response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
})

//tag//

const showTag = true;
const close = (tag) => {
  activeId.value=activeId.value.filter(
      (i)=> i!==tag
  );
};

//search//

const searchTags=()=>{
  axios.post('http://localhost:8080/user/tags',{tags:activeId.value})
      .then(function (response) {
        // 处理成功情况
        if (response.data.code === 0) {
          showSuccessToast(response.data.code+"\n"+response.data.message);
          state.datas=response.data.data;
        } else {
          showFailToast("页面加载失败\n"+response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
}


</script>