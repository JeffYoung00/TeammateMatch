<template>

  <van-button to="/login" type="success">用户登录</van-button>
  <van-button to="/register" type="default">用户注册</van-button>
  <van-button v-on:click="onClick" type="danger">用户登出</van-button>
</template>
<script setup>

import axios from "axios";
import {showFailToast, showSuccessToast} from "vant";
import {getCurrentInstance} from "vue";
const userIn=getCurrentInstance().appContext.config.globalProperties.$userIn;

const onClick=()=>{
  axios.post('http://localhost:8080/user/logout')
      .then(function (response) {
        // 处理成功情况
        if (response.data.code === 0) {
          showSuccessToast(response.data.code+"\n"+response.data.message);
          userIn.id = -1;
          userIn.userName = "";
          userIn.gender = "";
          userIn.avatarUrl = "";
          userIn.email = "";
          userIn.phone ="";
          userIn.userState = 0;
          userIn.userRole = 0;
          userIn.createTime = "";
        } else {
          showFailToast(response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
}

</script>
