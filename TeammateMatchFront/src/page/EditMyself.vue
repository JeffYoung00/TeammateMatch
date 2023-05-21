<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
<!--      修改头像  -->
<!--      修改在线状态switch-->
<!--      修改性别-->

      <van-field
          v-model=userIn.userName
          name="userName"
          label="用户名"
          placeholder="用户名"
          :rules="[{ required: true, message: '请填写用户名'}]"
      />
      <van-field name="radio" label="性别">
        <template #input>
          <van-radio-group v-model="userIn.gender" direction="horizontal">
            <van-radio name="male"  value="male">男</van-radio>
            <van-radio name="female" value="female">女</van-radio>
          </van-radio-group>
        </template>
      </van-field>
      <van-field
          v-model=userIn.email
          name="email"
          label="邮箱"
          placeholder="邮箱"
      />
      <van-field
          v-model=userIn.phone
          name="phone"
          label="电话"
          placeholder="电话"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>
</template>
<script setup>
import {onMounted} from "vue";
import {ref} from "vue";
import axios from "axios";
import {showFailToast, showSuccessToast} from "vant";
import {getMe, setMe} from "../global/global.js";
import {useRouter} from "vue-router";

const userIn = ref({});

onMounted(()=>{
  let user=getMe();
  userIn.value.userName=user.userName;
  userIn.value.gender=user.gender;
  userIn.value.email=user.email;
  userIn.value.phone=user.phone;
})

const onSubmit=()=> {
  axios.post('http://localhost:8080/user/edit',
      {userName:userIn.value?.userName,gender:userIn.value?.gender,email:userIn.value?.email,phone:userIn.value?.phone})
  .then(function (response) {
    // 处理成功情况
    if (response.data.code === 0) {
      showSuccessToast(response.data.code+"\n"+response.data.message);
      setMe(response.data.data);
    } else {
      showFailToast(response.data.code + "\n" + response.data.message);
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}
</script>