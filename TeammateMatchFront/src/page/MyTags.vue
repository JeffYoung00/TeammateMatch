<template>
  <van-button type="primary" v-on:click="save">保存Tags</van-button>

  <!--  展示选中的tag-->
  <van-row  justify="center" gutter="10">
    <van-col v-for="id in activeId" >
      <van-tag :show="showTag" closeable size="medium" type="primary" @close="close(id)">
        {{getTagName(id)}}
      </van-tag>
    </van-col>
  </van-row>

  <!--tag树-->
  <van-tree-select
      v-model:active-id="activeId"
      v-model:main-active-index="activeIndex"
      :items="state.items"
  />


</template>
<script setup>
import axios from "axios";
import {onMounted, reactive, ref} from "vue";
import {showFailToast, showSuccessToast} from "vant";
import {getGameId} from "../global/global.js";
let gameId=getGameId();
const activeId = ref([]);
const activeIndex = ref(0);
const state = reactive({
  items: [],
  map: []
});
const showTag = true;
const close = (id) => {
  activeId.value=activeId.value.filter(
      (i)=> i!==id
  );
};
const getTagName=(tagId)=> {
  return state.map[tagId];
}
onMounted(()=>{
  axios.get('http://localhost:8080/tags/game?gameId='+gameId)
      .then(function (response) {
        // 处理成功情况
        if (response.data.code === 0) {
          showSuccessToast(response.data.code+"\n"+response.data.message);
          state.items=response.data.data;
        } else {
          showFailToast("页面加载失败\n"+response.data.code + "\n" + response.data.message);
        }
        state.items.forEach((group) => {
          group.children.forEach((tag) => {
            state.map[tag.id] = tag.text;
          });
        });
      })
      .catch(function (error) {
        console.log(error);
      });

  /**
   * 新增下面的内容,减少了SearchTags
   */
  axios.get('http://localhost:8080/user/myTags?gameId='+gameId)
      .then(function (response) {
        // 处理成功情况
        if (response.data.code === 0) {
          showSuccessToast(response.data.code+"\n"+response.data.message);
          activeId.value=response.data.data;
        } else {
          showFailToast("页面加载失败\n"+response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
})

const save=()=>{
  axios.post('http://localhost:8080/user/myTags?gameId='+gameId,activeId.value)
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