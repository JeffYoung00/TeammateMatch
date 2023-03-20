

<template>
  <div style="height: 400px;width: 400px">


    <!--top搜索栏-->
    <van-nav-bar
        title="标题"
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

    <!--底部导航栏-->
    <router-view />
    <van-tabbar route>

      <!--    <van-tabbar-item to="/search" icon="search" name="search">标签</van-tabbar-item>-->
      <van-tabbar-item to="/main" icon="home-o" name="home">主页</van-tabbar-item>
      <van-tabbar-item to="/friend" icon="friends-o" name="friend">好友</van-tabbar-item>
      <van-tabbar-item to="/home" icon="friends-o" name="setting">个人</van-tabbar-item>
      <van-tabbar-item to="/setting" icon="setting-o" name="setting">设置</van-tabbar-item>

    </van-tabbar>



  </div>




</template>

<script setup lang="ts">

const onClickLeft = () => history.back();
const onClickRight = () => {};


</script>

<style>

</style>