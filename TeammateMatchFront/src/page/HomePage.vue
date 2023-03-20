<template>
  <van-cell-group inset>
    <img height="80" width="80" v-bind:src="userIn.avatarUrl">
    <van-cell title="昵称" v-bind:value=userIn.userName />
    <van-cell title="性别" v-bind:value=userIn.gender />
    <van-cell title="邮箱" v-bind:value=userIn.email />
    <van-cell title="电话" v-bind:value=userIn.phone />
  </van-cell-group>
  <van-button type="success" to="/editMe">编辑信息</van-button>
</template>
<script setup lang="ts">
import axios from "axios";
import {showFailToast, showSuccessToast} from "vant";
import {onMounted, reactive, ref} from "vue";


let userIn = reactive({
  userName:"*NOT_LOGIN*",
  gender:"",
  email:"",
  phone:"",
  avatarUrl:""
});
const getMe=()=>{
  axios.post('http://localhost:8080/user/current')
      .then(function (response) {
        // 处理成功情况
        if(response.data.code===0){
          showSuccessToast(response.data.code+"\n"+response.data.message);
          userIn.userName=response.data.data.userName;
          userIn.gender=response.data.data.gender;
          userIn.email=response.data.data.email;
          userIn.phone=response.data.data.phone;
          userIn.avatarUrl=response.data.data.avatarUrl;
        }else{
          showFailToast(response.data.code+"\n"+response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      })
}
onMounted(getMe);


</script>