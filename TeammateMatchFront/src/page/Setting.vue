<template >

  <van-button to="/login" type="success">用户登录</van-button>
  <van-button to="/register" type="default">用户注册</van-button>
  <van-button v-on:click="onClick" type="danger">用户登出</van-button>
</template>
<script setup>

import axios from "axios";
import {showFailToast, showSuccessToast} from "vant";
import {setMe} from "../global/global.js";

const onClick=()=>{
  axios.post('http://localhost:8080/user/logout')
      .then(function (response) {
        // 处理成功情况
        if (response.data.code === 0) {
          showSuccessToast(response.data.code+"\n"+response.data.message);
          setMe(null);
        } else {
          showFailToast(response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
}

</script>
