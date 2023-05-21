<template>
  <van-tabs v-model:active="active">
    <van-tab title="队伍信息">
<!--      -->
      <van-cell-group inset>
        <van-image
            width="100"
            height="100"
            :src=team.logo
        />
        <van-cell title="游戏id" :value=team.gameId />
        <van-cell title="队伍id" :value=team.id />
        <van-cell title="队伍名" :value=team.teamName />
        <van-cell title="创建时间" :value=team.createTime />
        <van-cell title="公开" :value=team.isPublic />

        <van-cell title="最大人数" :value=team.maxSize />
        <van-cell title="当前人数" :value=team.currentSize />

        <van-cell title="队伍简介" :value=team.description />
      </van-cell-group>

    </van-tab>
    <van-tab title="队长信息">
      <van-card
          v-bind:desc=leader.email
          v-bind:title=leader.userName
          v-bind:thumb=leader.avatarUrl
          v-bind:tag=leader.userState
      >
        <template #tags>
          <van-tag plain type="primary">{{leader.gender}}</van-tag>
        </template>
        <template #footer>
          <van-button size="mini">add friend</van-button>

        </template>
      </van-card>
      <van-button type="warning" v-on:click="leave">离开队伍</van-button>
      <van-button type="danger" v-on:click="disband">解散队伍</van-button>

    </van-tab>
    <van-tab title="成员信息">
      <div v-for="user in members">
        <van-card
            v-bind:desc=user.email
            v-bind:title=user.userName
            v-bind:thumb=user.avatarUrl
            v-bind:tag=user.userState
        >
          <template #tags>
            <van-tag plain type="primary">{{user.gender}}</van-tag>
          </template>
          <template #footer>
            <van-button size="mini">add friend</van-button>
            <van-button size="mini" v-on:click="kickOut(user.id)">kick out</van-button>
          </template>
        </van-card>
      </div>
    </van-tab>
  </van-tabs>

</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {showFailToast, showSuccessToast} from "vant";

const team=ref({});
const leader=ref({});
const members=ref([]);

onMounted(()=>{
      axios.post('http://localhost:8080/team/my')
          .then(function (response) {
            // 处理成功情况
            if (response.data.code === 0) {
              showSuccessToast(response.data.code+"\n"+response.data.message);
              team.value=response.data.data.team;
              leader.value=response.data.data.leader;
              members.value=response.data.data.members;
              //todo
              if(team.value.gameId===0){
                team.value.gameId="英雄联盟";
              }
              if(team.value.isPublic===true){
                team.value.isPublic="public";
              }else{
                team.value.isPublic="private";
              }
            } else {
              showFailToast("页面加载失败\n"+response.data.code + "\n" + response.data.message);
            }
          })
          .catch(function (error) {
            console.log(error);
          });
    }
)

const kickOut=(id)=>{
  axios.post('http://localhost:8080/team/kick?userId='+id)
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

const leave=()=>{
  axios.post('http://localhost:8080/team/leave')
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

const disband=()=>{
  axios.post('http://localhost:8080/team/disband')
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

</script>