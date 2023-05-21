<template>

  <div v-for="user in userList">
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
      <van-button size="mini">invite to my team</van-button>
      <van-button size="mini">add friend</van-button>
    </template>
  </van-card>
  </div>


</template>
<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {showFailToast, showSuccessToast} from "vant";
import {useRoute} from "vue-router";

const userList=ref([]);
onMounted(
    async () => {
      const route = useRoute();
      let tagIdList = route.query.tagIdList;
      console.log(tagIdList);
      await axios.post('http://localhost:8080/user/searchTags?gameId=' + gameId, tagIdList)
          .then(function (response) {
            // 处理成功情况
            if (response.data.code === 0) {
              showSuccessToast(response.data.code + "\n" + response.data.message);
              userList.value = response.data.data;
              for (let i = 0; i < userList.value.length; i++) {
                let user = userList.value[i];
                if(user.gender==="male"){
                  user.gender="♂"
                }else if(user.gender==="female"){
                  user.gender="♀";
                }
                if(user.userState===1){
                  user.userState="在线";
                }else if(user.userState===0){
                  user.userState="离线";
                }
              }
            } else {
              showFailToast("页面加载失败\n" + response.data.code + "\n" + response.data.message);
            }
          })
          .catch(function (error) {
            console.log(error);
          });
    }
)

</script>