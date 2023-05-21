<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="team.teamName"
          label="队伍名"
          placeholder="长度2~10"
          :rules="[{ required: true, message: '请填写用户名' }]"
      />
      <van-field
          type="digit"
          v-model=team.maxSize
          label="最大队伍人数"
          placeholder="推荐2~20"
          :rules="[{ required: true, message: '请填写最大人数' }]"
      />
      <van-field>
        <template #input>
        <van-radio-group v-model=team.isPublic direction="horizontal">
          <van-radio :name=true>public</van-radio>
          <van-radio :name=false>private</van-radio>
        </van-radio-group>
        </template>
      </van-field>
      <van-field
          v-model="team.password"
          type="password"
          label="密码"
          placeholder="密码"
          :rules="[{ required: team.isPublic, message: '请填写密码' }]"
      />
      <van-field
          v-model=team.description
          rows="2"
          autosize
          label="队伍描述"
          type="textarea"
          placeholder="请输入队伍描述"
      />
      <van-field
          v-model="team.logo"
          label="logo"
          placeholder="暂时只支持传入url"
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

import {onMounted, reactive, ref} from "vue";
import {getGameId} from "../global/global.js";
import axios from "axios";
import {showFailToast, showSuccessToast} from "vant";

const team=ref(
    {
      gameId:getGameId(),
      teamName:"SKT111",
      maxSize:20,
      isPublic:true,
      password:"123456",
      description:"三冠战队",
      tags:null,
      logo:"src/assets/logo2"
    }
)
const onSubmit=()=>{
  axios.post('http://localhost:8080/team/create',team.value)
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