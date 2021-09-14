<template>
  <div>
    <el-button
      size="mini"
      type="text"
      icon="el-icon-check"
      @click="handleApply"
    >提交申请</el-button>
    <el-button
      size="mini"
      type="text"
      icon="el-icon-edit"
      @click="_handleUpdate"
    >修改</el-button>
    <el-button
      size="mini"
      type="text"
      icon="el-icon-delete"
      @click="_handleDelete"
    >删除</el-button>
  </div>
</template>

<script>
  import request from '@/utils/request'

  export default {
    name: "ApplyBefore",
    props: {
      /* 表格行数据 */
      row: {
        type: Object,
      },
      handleUpdate: {
        type: Function,
      },
      handleDelete: {
        type: Function,
      },
      requestMapping: {
        type: String,
      },
    },
    methods: {
      handleApply: function () {
        const id = this.row.id;
        const requestMapping = this.requestMapping;
        this.$confirm('是否提交ID为"' + id + '"的申请单据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return request({
            url: requestMapping + '/submitApply/' + id,
            method: 'post',
          });
        }).then(() => {
          this.$emit('getList');
          this.msgSuccess("申请成功");
        })
      },
      _handleUpdate: function () {
        this.handleUpdate(this.row);
      },
      _handleDelete: function () {
        this.handleDelete(this.row);
      },
    },
  }
</script>

<style scoped>

</style>
