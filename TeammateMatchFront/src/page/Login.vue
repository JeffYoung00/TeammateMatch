<template>
  <van-form @submit="onSubmit">
    <van-cell-group>
      <van-field
          v-model=userName
          name="userName"
          label="用户名"
          placeholder="用户名"
          :rules="[{ required: true, message: '请填写用户名'}]"
      />
      <van-field
          v-model=password
          name="password"
          label="密码"
          placeholder="密码"
          :rules="[{ required: true, message: '请填写密码' }]"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>
  <router-link to="/Register">Click to Register</router-link>
</template>
<script setup>
import {ref} from "vue";
import axios from "axios";
import {showSuccessToast,showFailToast} from "vant";
import {useRouter} from "vue-router";

const userName=ref("");
const password=ref("");
const router=useRouter();
const onSubmit=()=> {
  //先简单长度检查
  if (userName.value.length < 2 || userName.value.length > 12) {
    showFailToast("用户名长度需要在2~12");
    return;
  }
  if (password.value.length < 8 || password.value.length > 20) {
    showFailToast("用户密码长度需要在8~20");
    return;
  }
  //发送消息
  axios.post('http://localhost:8080/user/login',
      {userName: userName.value, password: password.value})
      .then(function (response) {
        if (response.data.code === 0) {
          showSuccessToast("welcome," + userName.value);
          showSuccessToast(response.data.code + "\n" + response.data.message);
          //等待一秒之后跳到home
          //setTimeout( "",1000);
          //router.push('/home');
        } else {
          showFailToast(response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
}
</script>