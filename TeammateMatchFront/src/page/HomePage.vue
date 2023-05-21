<template>
  <van-cell-group inset>

    <van-image
        width="100"
        height="100"
        :src=user.avatarUrl

    />
    <van-cell title="昵称" :value=user.userName />
    <van-cell title="性别" :value=user.gender />
    <van-cell title="邮箱" :value=user.email />
    <van-cell title="电话" :value=user.phone />
  </van-cell-group>
  <van-button type="success" v-on:click="toEdit">编辑信息</van-button>
</template>
<script setup>
import {onMounted, ref} from "vue";
import {getMe, not_login_user} from "../global/global.js";
import {showFailToast} from "vant";
import {useRouter} from "vue-router";

const user=ref({});
const router=useRouter();

onMounted(()=>{
      if(getMe()===null){
        user.value=not_login_user;
      }else{
        user.value=getMe();
      }
})

const toEdit=()=>{
  if(getMe()===null){
    showFailToast("请先登录");
  }else{
    router.push("/editMyself");
  }
}
</script>