<template>

  <van-search v-model="teamName" placeholder="请输入要查找的队伍名" v-on:click="search" />
  <van-tabs>
    <van-tab v-for="team in teams" :title="team.teamName">
      <van-cell-group inset>
        <van-image
            width="100"
            height="100"
            :src=team.logo
        />
        <van-cell title="队伍id" :value=team.id />
        <van-cell title="队伍名" :value=team.teamName />
        <van-cell title="创建时间" :value=team.createTime />
        <van-cell title="公开" :value=team.isPublic />

        <van-cell title="最大人数" :value=team.maxSize />
        <van-cell title="当前人数" :value=team.currentSize />

        <van-cell title="队伍简介" :value=team.description />
      </van-cell-group>
    </van-tab>

      <van-cell title="加入战队" is-link @click="show=!show" style="background-color: #535bf2"/>
    <van-popup v-model:show="show" :style="{ padding: '64px'}">
      <van-field v-model="password" type="password" label="入队密码: " center/>
      <van-button type="success" v-on:click="join(team.id)" style="margin-top: 30px">确认</van-button>
    </van-popup>

  </van-tabs>

</template>
<script setup>

import axios from "axios";
import {showFailToast, showSuccessToast} from "vant";
import {getGameId} from "../global/global.js";
import {onMounted, ref} from "vue";

const teamName=ref("");
const teams=ref([
  {
    gameId:getGameId(),
    teamName:"SKT111",
    maxSize:20,
    isPublic:"public",
    password:"123456",
    description:"三冠战队",
    tags:null,
    logo:"src/assets/logo2"
  }
]);

const show=ref(false);
const password=ref();

const search=()=>{
  axios.post('http://localhost:8080/team/search?gameId='+getGameId()+'teamName='+teamName)
      .then(function (response) {
        if (response.data.code === 0) {
          showSuccessToast(response.data.code+"\n"+response.data.message);
          teams.value=response.data.data;
        }else{
          showFailToast("页面加载失败\n"+response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
}
const join=(teamId)=>{
  axios.post('http://localhost:8080/team/join',{teamId:teamId,password:password.value})
      .then(function (response) {
        if (response.data.code === 0) {
          showSuccessToast(response.data.code+"\n"+response.data.message);
        }else{
          showFailToast("页面加载失败\n"+response.data.code + "\n" + response.data.message);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
}

// onMounted(()=>{
//   axios.post('http://localhost:8080/team/recommend?gameId='+getGameId())
//       .then(function (response) {
//         if (response.data.code === 0) {
//           showSuccessToast(response.data.code+"\n"+response.data.message);
//           teams.value=response.data.data;
//           for(let i=0;i<teams.value.length;i++){
//             if(teams.value[i].isPublic===true){
//               teams.value[i].isPublic="public";
//             }else{
//               teams.value[i].isPublic="private";
//             }
//           }
//         }else{
//           showFailToast("页面加载失败\n"+response.data.code + "\n" + response.data.message);
//         }
//       })
//       .catch(function (error) {
//         console.log(error);
//       });
// })

</script>