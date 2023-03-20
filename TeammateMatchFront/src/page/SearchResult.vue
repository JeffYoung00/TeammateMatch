<template>

  <van-list
      v-model:loading="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="onLoad"
  >
    <van-cell v-for="item in list" :key="item" :title="item" />
  </van-list>

</template>
<script setup>
import {ref} from "vue";

//数据数组---------
const list= ref([]);
const loading = ref(false);//加载状态
const finished = ref(false);//结束加载

const onLoad = () => {

}

</script>