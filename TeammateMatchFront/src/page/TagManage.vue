<template>


  <van-form>
    <van-cell-group inset>
      <van-field
          v-model="tagName"
          name="tagName"
          label="tagName"
          placeholder="tagName"
          :rules="[{ required: true, message: '请填写TagName' }]"
      />
      <van-field
          v-model="groupName"
          name="groupName"
          label="groupName"
          placeholder="groupName"
          :rules="[{ required: false, message: '请填写GroupName' }]"
      />
    </van-cell-group>
      <van-button round  type="primary" v-on:click="addTag">
        新增Tag
      </van-button>
      <van-button round type="primary" v-on:click="deleteTag">
        删除Tag
      </van-button>
  </van-form>

</template>
<script setup>

import {ref} from "vue";
import axios from "axios";
import {showFailToast, showSuccessToast} from "vant";
import {getGameId} from "../global/global.js";
let gameId=getGameId();
const tagName=ref("");
const groupName=ref("")

const addTag = () => {
  send("post");
}
const deleteTag = () => {
  send("delete");
}

const send = (methods,url) => {
  axios({method:methods,url:'http://localhost:8080/tags/tag',
      data:{gameId:gameId,tagName: tagName.value, groupName: groupName.value}})
      .then(function (response) {
        if (response.data.code === 0) {
          showSuccessToast(response.data.code + "\n" + response.data.message);
        } else {
          showFailToast(response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
}

</script>