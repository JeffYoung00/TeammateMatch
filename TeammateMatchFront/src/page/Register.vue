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
      <van-field
          v-model=checkPassword
          name="checkPassword"
          label="确认密码"
          placeholder="确认密码"
          :rules="[{ required: true, message: '请重复密码' }]"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>

</template>
<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";
import {showSuccessToast,showFailToast} from "vant";
import {useRouter} from "vue-router";

const userName=ref("");
const password=ref("");
const checkPassword=ref("");
const router=useRouter();
const onSubmit=()=>{
  //基本长度检测
  if(userName.value.length<2||userName.value.length>12){
    showFailToast("用户名长度需要在2~12");
    return;
  }
  if(password.value.length<8||password.value.length>20){
    showFailToast("用户密码长度需要在8~20");
    return;
  }
  if(password.value!=checkPassword.value){
    showFailToast("两次输入的密码不同");
    return;
  }
  //发送
  axios.post('http://localhost:8080/user/register',{ userName:userName.value ,password:password.value,checkPassword:checkPassword.value})
      .then(function (response) {
        // 处理成功情况
        if(response.data.code===0){
          showSuccessToast(response.data.code+"\n"+response.data.message);
          //等待一秒之后跳到login
          setTimeout( "",1000);
          router.push("/login");
        }else{
          showFailToast(response.data.code+"\n"+response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
}
</script>