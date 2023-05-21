<template>
  <div style="width: 400px">
    <!--top搜索栏-->
    <van-nav-bar
        title="TeammateMatch"
        left-text="返回"
        right-text="按钮"
        left-arrow
        @click-left="onClickLeft"
        @click-right="onClickRight"
    >
      <template #right>
        <van-icon name="search" size="18" />
      </template>
    </van-nav-bar>

<!--    中间展示页-->
    <div style="height: 550px">
    <router-view />
    </div>

<!--    底部导航栏-->
    <van-tabbar route>
      <van-tabbar-item to="/mainPage" icon="home-o" name="home">主页</van-tabbar-item>
      <van-tabbar-item to="/myTeam" icon="friends-o" name="friend">团队</van-tabbar-item>
      <van-tabbar-item to="/homePage" icon="friends-o" name="setting">个人</van-tabbar-item>
      <van-tabbar-item to="/setting" icon="setting-o" name="setting">设置</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup lang="ts">

const onClickLeft = () => history.back();
const onClickRight = () => {};


</script>